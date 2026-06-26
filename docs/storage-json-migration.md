# ano-doc storage migration: from serialized `.dat` to git-backed JSON

Design notes / decision record. Captures the current architecture and the agreed path
forward so a future session can continue without re-deriving everything.

Status: **design agreed, not yet implemented.** No code written yet.

---

## 1. What ano-doc is

- ano-doc is a **generator**: it generates a CMS from XML data definitions
  (`datadef-*.xml`). The partner/runtime project is **ano-site**
  (`/Users/another/projects/aos/ano-site`); the generated CMS code and things like
  `CRUDLogListener` live there.
- Historically ano-doc could do more (multi-owner / multi-copy module instances), but
  **today it is used only as a CMS**: single instance, everything owned by one instance,
  no multiple copies.
- A concrete consumer: **houseid** (`/Users/another/projects/myhouseid/houseid-gen`),
  whose `Measures` module is the running example throughout.

## 2. Current storage architecture

Two layers with a clean seam between them — this seam is what we swap.

- **`CommonHashtableModuleStorage`** (`IModuleStorage`) keeps all modules in a
  `Hashtable<String,Module>` **in memory** and serves from RAM. On every `saveModule()`
  it *dissolves* the `Module` into a nested `Hashtable<String,Hashtable>` "container"
  (the `saveObject`/`saveComposite`/`savePlain` recursion) whose leaves are `Property`
  objects, then hands the whole thing to…
- **`FsStorage`** (`IStorage`), which just does
  `ObjectOutputStream.writeObject(hashtable)` → one file (e.g. `measures.dat`).
  Load reverses it; `Module.fillFromContainer` reassembles the tree via `IModuleFactory`.

So a `.dat` file = **one module**, Java-serialized as a single binary blob.

### Module shape (important)
A module **aggregates multiple document types**. `measures.dat` (~211 KB) is the whole
`Measures` module: document types `EcoMeasure`, `SubstanceMeasure`, `ExpenseMeasure`,
`MeasureProject` — all multilingual, with links. Inside the module, **each document type
is stored as a `DocumentList`** (confirmed in generated `MeasuresServiceImpl`:
`module.getEcoMeasures()`, `createEcoMeasure`, `updateEcoMeasure`, `deleteEcoMeasure`).

### Distribution today
- **GCP bucket** for the central CMS; **filesystem** on nodes.
- On node deploy: node receives a **full copy** (files copied).
- After deploy: changes pushed via a **publish API** (a button; automatic on test
  systems). This push channel is the real mechanism now.
- The old "copy one file, FileWatcher picks it up" path is **vestigial** — host-FS
  polling doesn't work reliably in Docker (esp. macOS). The lock-file + 5s `FileWatcher`
  in `CommonHashtableModuleStorage` is effectively legacy.

## 3. Goal

Replace the opaque Java-serialized `.dat` so that:
- **We can tell when something changed** (history / audit) — the primary driver.
- Storage is diffable / versionable instead of a binary blob.

Non-drivers (explicitly downweighted):
- Java-serialization brittleness / deserialization security: **never been a problem in
  20 years live**, so not a motivation.
- "Copy one file to deploy" simplicity: no longer relied upon (push API replaced it).

## 4. Agreed path forward

Split into two concerns with different best answers.

### 4a. Nodes — JSON files written by the publish API
- Nodes are **read-only delivery**: no history, no DB, no polling.
- Publish API writes JSON straight to the node's storage root and triggers an in-process
  reload (`IModuleStorage.load()` → `moduleLoaded` listener). **Drop the FileWatcher** on
  nodes — it's the part that breaks in containers.
- Granularity: **one file per document type** (`Measures/EcoMeasure.json`,
  `Measures/MeasureProject.json`, …) so publish can be incremental (ship only the changed
  type), not the whole module.
- Nodes never have git; they just get JSON.

### 4b. Central — git-backed JSON (recommended)
History lives on the central authoring system. Two options were weighed:

- **Git-backed JSON (recommended).** The storage directory *is* a git working tree; each
  write-through commits the changed file(s). `git log -p Measures/EcoMeasure.json` answers
  "when changed, to what" with zero query code. History format == storage format == node
  format (one representation everywhere). With single-instance / single-writer and no
  authors, the usual git-concurrency caveats basically evaporate.
- **DB on central (escalation only).** The generator already supports `StorageType.DB`
  (JDBC) and `FEDERATION`, so routing a module to a DB is an existing path. Choose this
  only if a module later needs **server-side queries over content**, **many concurrent
  authors**, or hits **git repo-bloat** from very high write volume. None apply to
  editorial modules like Measures today.

**Decision: go git-backed JSON.** Keep DB as a per-module escalation via the existing
`StorageType` seam.

## 5. JSON format design

- **Layout:** one directory per module, one file per document type:
  `…/Measures/EcoMeasure.json`. **No owner/copy in the path** (single instance →
  `makeKey(ownerId,copyId)` is vestigial).
- **File content:** an **id-keyed object** (not a positional array). Editing one document
  changes exactly one key's subtree; add = one key added; no reordering churn. Safe
  because the service already sorts on read (`StaticQuickSorter` + `SortType`), so storage
  order isn't load-bearing. (Exception: if a list is ever hand-curated for display order,
  keep that one as an array — Measures isn't.)
- **Multilingual** fields nest by locale instead of flat `name_en` / `name_de` suffixes.
- **Typed numbers stay bare** (`"threshold": 0.3`) — no type tags on disk. The generator
  knows each field's type and which fields are multilingual, so **schema-driven
  (de)serialization** restores `FloatProperty` etc. on load. Keep a self-describing
  fallback in the generic `Document.fillFromContainer` path for any untyped/legacy data.
- **Drop `author` at storage level entirely** (author is always "the system"). "Last
  edited by", if wanted, is just a normal ano-site field, not a storage concept.
- Use **Jackson** with sorted keys / deterministic output so diffs are minimal.

### Example `Measures/EcoMeasure.json`
```json
{
  "1": {
    "type": "insulation",
    "threshold": 0.3,
    "impact": 12.5,
    "name":        { "en": "Roof insulation", "de": "Dachdämmung" },
    "subsidy":     { "en": "…", "de": "…" },
    "description": { "en": "…", "de": "…" },
    "measureProject": "7"
  },
  "2": { "…": "…" }
}
```

## 6. Git mechanics (central)

- Literally: **write the JSON file, then `git add` + `git commit`.** No git-object surgery,
  no per-keystroke magic. The storage dir is just a git working tree.
- **Commit unit = one `saveModule()` = one CRUD op** ("every change is a commit"). A bulk
  op (`updateEcoMeasures(list)`) = one `saveModule` = one commit covering several docs.
  Not per-field / per-keystroke.
- **Write only changed files:** `saveModule` hands you the whole module; re-serialize each
  type and write only files whose bytes changed → a single edit touches one file → tight
  commit. No change-tracking needed in the model.
- **Driver:** prefer **JGit** (in-process) over shelling out to the `git` CLI.
- **Reads never touch git.** `load()` just reads JSON, exactly like `FsStorage` reads
  `.dat`. Git is a pure history sidecar; remove it and the CMS still works.
- **Commit messages:** derive meaningful messages (`"Measures: update EcoMeasure 7"`) by
  feeding the existing **`CRUDLogListener`** event (op + document id) into the commit.
- **Cadence** is a knob: per-save (recommended, finest history, periodic `git gc`) vs
  batched (coarser, only if write volume is high).
- **Perf:** start with synchronous commit (local commit ≈ ms); move off-thread only if
  measured.
- GCS is **not** a git remote: keep the working repo on the central node and push to a
  real git remote (Cloud Source Repo / GitHub); bucket becomes a mirror or is retired.

## 7. Deployment / publish to nodes

One model, two granularities, both derived from git commits; both write the same JSON
format into the node root and end in the same `load()` + listener reload.

### Full deploy ("copy the cms root")
- Build the artifact from a commit: `git archive --format=tar.gz -o cms-<sha>.tgz <commit>`.
  `git archive` = consistent snapshot, **no `.git`** inside (clean JSON tree), and the
  **artifact identity is the commit SHA** → "this node runs `abc123`" is checkable and
  reproducible.
- On node: extract to a **staging dir** → **atomic swap** into the storage root
  (rename / `current` symlink flip) → `load()` rebuilds the in-memory graph → write a
  `VERSION` marker with the SHA.

### Incremental publish (button / auto-on-test)
- Node reports its `VERSION` (SHA).
- CMS computes `git diff --name-only <nodeSha>..HEAD` → exact changed files → ship just
  those (small tgz or direct PUTs) → node overwrites → reload affected module(s) → update
  `VERSION`.
- Fallback to a full `git archive` if the node's SHA is unknown or too far behind.

| Flow        | Trigger              | Payload             | Built from              |
|-------------|----------------------|---------------------|-------------------------|
| Full deploy | node bootstrap/resync| whole root, tgz     | `git archive <sha>`     |
| Incremental | button / auto (test) | changed files only  | `git diff <nodeSha>..HEAD` |

## 8. Implementation approach / rollout

- New `JsonGitModuleStorage implements IModuleStorage`, selected via the existing
  ConfigureMe / `StorageType` seam (`StorageFactory`, `anodoc.storage` config) — **no
  big bang**.
- **Pilot on the `Measures` module only.** One-shot migrator: load via existing
  `CommonHashtableModuleStorage` (reads `.dat`), write the keyed-per-type JSON tree,
  commit. Leave every other module on `.dat`. Validate diffs / history / publish, then
  roll module by module.
- Keep a `.dat` fallback (read `.dat` if no JSON present) for safe transition.
- Generator angle: emit per-document-type `toJson` / `fromJson` so on-disk JSON stays
  clean and type knowledge lives in generated code; generic fallback stays self-describing.

## 8b. Migration path (phased, no big bang)

Incremental migration is possible because **both central and nodes reduce to the same
in-memory `Module` graph**; storage format is just a load/save adapter (`IModuleStorage`)
around that graph. So format is a per-tier, per-module choice and the two tiers can run
different formats at once — **provided the publish/deploy layer serializes from the loaded
module, not from central's raw files.** Today deploy = "copy the actual files," which
couples the node format to central's on-disk format; changing that step to "produce the
node payload from the in-memory module" (for `.dat`, the existing `FsStorage.save` over the
dissolved hashtable) decouples them. That is the one contained prerequisite — small, not a
node change.

Two orthogonal phasing axes: **tier** (central vs nodes adopt JSON independently) and
**module** (Measures first via the `StorageType`/factory seam). Sequence, each step
independently shippable and reversible:

- **Phase 0 — Codec parity harness (no prod impact).** Implement JSON ↔ `Module` graph;
  test `measures.dat` → graph → JSON → graph′ and assert `graph == graph′`. Validates the
  serializer before anything depends on it.
- **Phase 1 — Central dual-write, still reads `.dat`.** Each save writes `.dat` (primary,
  unchanged) *and* JSON + git commit. Serving still reads `.dat`; nodes untouched. Starts
  accumulating real git history while nothing relies on JSON. Purely additive / reversible.
- **Phase 2 — Central reads JSON+git; nodes still `.dat`.** Flip central primary store to
  `JsonGitModuleStorage` for Measures. Deploy serializes graph → `.dat` for nodes, so nodes
  get **byte-identical payloads** and the fleet is untouched. This is the
  *"change central, keep nodes"* state — recommended pause point: delivers the primary goal
  (history on central) with zero delivery-tier risk.
- **Phase 3 — Nodes adopt JSON.** Switch node `IModuleStorage` to JSON; publish ships JSON
  (full = `git archive` tgz, incremental = `git diff` delta); drop the node FileWatcher
  (reload from the publish endpoint). Roll node-by-node / test-first — central emits
  whichever format the node advertises (`.dat` for old builds, JSON for new), so node
  rollout is gradual too.
- **Phase 4 — Roll remaining modules; retire `.dat`.** Migrate other modules via the
  `StorageType` seam; once all-JSON, delete the `.dat`/convert paths.

The whole Measures pilot can run 0→3 before any other module is touched.

**"Vice versa" (nodes first, central stays `.dat`):** possible but low value — central would
convert graph → JSON on publish yet still have no history, i.e. work without the payoff.
Only worth leading with to de-risk node JSON/reload mechanics first. **Central-first is the
better order** because it front-loads the actual goal.

## 9. Key code references

ano-doc-core (`ano-doc-core/src/main/java/net/anotheria/…`):
- `anodoc/util/CommonHashtableModuleStorage.java` — current `IModuleStorage`; in-memory
  hashtable, `save()`/`load()`, dissolve/reassemble, FileWatcher (legacy).
- `anodoc/util/storage/IStorage.java`, `FsStorage.java`, `StorageFactory.java`,
  `StorageType.java` — the `IStorage` backend seam (FS / S3 / GCS).
- `anodoc/service/IModuleStorage.java` — interface to implement for JSON storage.
- `anodoc/service/IModuleFactory.java` — used to reassemble modules/documents on load.
- `anodoc/data/Module.java` — `fillFromContainer`, module shape, `getStorageId`.
- `anodoc/data/Document.java` — typed properties, multilingual fields, `PROP_AUTHOR` /
  `PROP_LAST_UPDATE`.
- `anodoc/data/Property.java`, `IHelperConstants.java` — property model / key encoding.
- `asg/util/listener/IModuleListener.java` — `moduleLoaded` reload hook.
- `asg/generator/meta/StorageType.java` — generator storage types: `CMS`, `DB`,
  `FEDERATION`.

Elsewhere:
- `net.anotheria.anosite.cms.listener.CRUDLogListener` — in **ano-site**; existing
  change-log hook, source of commit messages.
- `/Users/another/projects/myhouseid/houseid-gen/etc/def/datadef-measures.xml` — Measures
  definition (the running example).
- Generated `…/gen/measures/service/MeasuresServiceImpl.java` (under houseid-gen
  `target/generated-sources`) — confirms per-type `DocumentList` storage.
- `measures.dat` (repo working dir) — example serialized module to migrate/test against.

## 10. Open decisions / next steps

- Confirm git remote target for central (Cloud Source Repo / GitHub) and how the working
  repo relates to the existing GCS bucket (mirror vs retire).
- Decide commit cadence default (per-save recommended) and sync-vs-async commit.
- Build the `Measures` pilot: `JsonGitModuleStorage` + one-shot migrator; eyeball a real
  per-field commit diff from a sample edit.
- Then wire deploy (`git archive` tgz) and incremental publish (`git diff` delta) into the
  node apply + reload path.
