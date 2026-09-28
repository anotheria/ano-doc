package net.anotheria.asg.generator.transfer;

import net.anotheria.asg.generator.AbstractGenerator;
import net.anotheria.asg.generator.FileEntry;
import net.anotheria.asg.generator.GeneratedClass;
import net.anotheria.asg.generator.GeneratorDataRegistry;
import net.anotheria.asg.generator.IGenerateable;
import net.anotheria.asg.generator.IGenerator;
import net.anotheria.asg.generator.meta.MetaDocument;
import net.anotheria.asg.generator.meta.MetaLink;
import net.anotheria.asg.generator.meta.MetaListProperty;
import net.anotheria.asg.generator.meta.MetaModule;
import net.anotheria.asg.generator.meta.MetaProperty;
import net.anotheria.asg.generator.model.DataFacadeGenerator;
import net.anotheria.asg.generator.model.ServiceGenerator;
import net.anotheria.asg.generator.restapi.RestVOGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates the metadata the document transfer runs on.
 *
 * <p>Per module a {@code <Module>TransferSupport} that can load any of its documents and say what that
 * document points at, and once a {@code TransferSupportRegistrar} that hands all of them to the runtime at
 * startup.
 *
 * <p>This is the only transfer related code that is generated. Which instances documents go to, in what order,
 * what to do about cycles and what to report afterwards is the same for every document type, so it lives in
 * ano-site and works on the snapshots these classes produce - see
 * {@code net.anotheria.anosite.transfer.DocumentTransferService}.
 */
public class TransferSupportGenerator extends AbstractGenerator implements IGenerator {

    /**
     * Runtime package the generated classes build on.
     */
    private static final String RUNTIME_PACKAGE = "net.anotheria.anosite.transfer";

    /**
     * Name of the generated registrar.
     */
    public static final String REGISTRAR_NAME = "TransferSupportRegistrar";

    /**
     * Generates the transfer support of one module.
     *
     * @param gmodule the module
     * @return the generated file
     */
    @Override
    public List<FileEntry> generate(IGenerateable gmodule) {
        List<FileEntry> ret = new ArrayList<>();
        ret.add(new FileEntry(generateSupport((MetaModule) gmodule)));
        return ret;
    }

    /**
     * Generates the class that hands every module's support to the runtime registry.
     *
     * @param modules all modules of the project
     * @return the generated file
     */
    public FileEntry generateRegistrarFile(List<MetaModule> modules) {
        return new FileEntry(generateRegistrar(modules));
    }

    /**
     * Generates the transfer support of one module.
     *
     * @param module module to describe
     * @return the generated class
     */
    private GeneratedClass generateSupport(MetaModule module) {
        GeneratedClass clazz = new GeneratedClass();
        startNewJob(clazz);

        clazz.setPackageName(getPackageName(module));
        clazz.setName(getSupportName(module));
        clazz.setParent("AbstractModuleTransferSupport");

        clazz.addImport("java.util.ArrayList");
        clazz.addImport("java.util.Arrays");
        clazz.addImport("java.util.List");
        clazz.addImport("net.anotheria.anoprise.metafactory.MetaFactory");
        clazz.addImport(RUNTIME_PACKAGE + ".AbstractModuleTransferSupport");
        clazz.addImport(RUNTIME_PACKAGE + ".DocumentKey");
        clazz.addImport(RUNTIME_PACKAGE + ".DocumentSnapshot");
        clazz.addImport(ServiceGenerator.getInterfaceImport(module));
        for (MetaDocument doc : module.getDocuments()) {
            clazz.addImport(DataFacadeGenerator.getDocumentImport(doc));
            clazz.addImport(RestVOGenerator.getVOImport(doc));
        }

        String serviceInterface = ServiceGenerator.getInterfaceName(module);

        startClassBody();
        appendGenerationPoint("generateSupport");

        StringBuilder names = new StringBuilder();
        for (MetaDocument doc : module.getDocuments()) {
            if (names.length() > 0)
                names.append(", ");
            names.append(quote(doc.getName()));
        }
        appendStatement("private static final List<String> DOCUMENT_NAMES = Arrays.asList(", names.toString(), ")");
        emptyline();

        appendString("@Override");
        openFun("public String getModuleName()");
        appendStatement("return ", quote(module.getName()));
        closeBlockNEW();
        emptyline();

        appendString("@Override");
        openFun("public List<String> getDocumentNames()");
        appendStatement("return DOCUMENT_NAMES");
        closeBlockNEW();
        emptyline();

        openFun("private " + serviceInterface + " service() throws Exception");
        appendStatement("return MetaFactory.get(", serviceInterface, ".class)");
        closeBlockNEW();
        emptyline();

        appendString("@Override");
        openFun("public DocumentSnapshot load(String documentName, String id) throws Exception");
        for (MetaDocument doc : module.getDocuments()) {
            appendString("if (", quote(doc.getName()), ".equals(documentName))");
            appendIncreasedStatement("return load" + doc.getName() + "(id)");
        }
        appendStatement("throw new IllegalArgumentException(", quote("Module " + module.getName() + " has no document "), " + documentName)");
        closeBlockNEW();

        for (MetaDocument doc : module.getDocuments()) {
            emptyline();
            generateLoadDocument(module, doc);
        }

        return clazz;
    }

    /**
     * Generates the loader of one document: read it, collect what it points at and wrap it in a snapshot.
     *
     * @param module the document's module
     * @param doc    document to describe
     */
    private void generateLoadDocument(MetaModule module, MetaDocument doc) {
        String var = doc.getVariableName();

        openFun("private DocumentSnapshot load" + doc.getName() + "(String id) throws Exception");
        appendStatement(doc.getName(), " ", var, " = service().get", doc.getName(), "(id)");
        appendStatement("List<DocumentKey> references = new ArrayList<>()");
        appendReferences(module, doc, var);
        appendStatement("List<String> files = new ArrayList<>()");
        appendFiles(doc, var);
        appendStatement("return new DocumentSnapshot(new DocumentKey(", quote(module.getName()), ", ",
                quote(doc.getName()), ", id), ", quote(getRestPath(module, doc)), ", ",
                RestVOGenerator.getVOName(doc), ".from(", var, "), references, files)");
        closeBlockNEW();
    }

    /**
     * Appends the collection of every link of a document, single links and link lists alike.
     *
     * @param module the document's module
     * @param doc    document to describe
     * @param var    name of the document variable in the generated method
     */
    private void appendReferences(MetaModule module, MetaDocument doc, String var) {
        for (MetaProperty property : doc.getLinks())
            appendLink(module, doc, (MetaLink) property, var, false);

        for (MetaProperty property : doc.getProperties()) {
            if (!(property instanceof MetaListProperty))
                continue;

            MetaListProperty list = (MetaListProperty) property;
            if (!list.getContainedProperty().isLinked())
                continue;

            //the list carries the multilinguality and the accesser names, the contained link carries the target.
            appendLink(module, doc, (MetaLink) list.getContainedProperty(), var, true, list);
        }
    }

    private void appendLink(MetaModule module, MetaDocument doc, MetaLink link, String var, boolean multiple) {
        appendLink(module, doc, link, var, multiple, link);
    }

    /**
     * Appends one link.
     *
     * @param module   the document's module
     * @param doc      document to describe
     * @param link     the link, carrying the target
     * @param var      name of the document variable in the generated method
     * @param multiple true if the accesser returns a list of ids
     * @param accesser the property the accesser names and the multilinguality come from: the link itself for a
     *                 single link, the list property for a link list
     */
    private void appendLink(MetaModule module, MetaDocument doc, MetaLink link, String var, boolean multiple,
                            MetaProperty accesser) {
        MetaModule targetModule = link.isRelative()
                ? module
                : GeneratorDataRegistry.getInstance().getModule(link.getTargetModuleName());
        if (targetModule == null)
            throw new RuntimeException("Can't resolve link target " + link.getLinkTarget() + " of "
                    + doc.getFullName() + "." + link.getName());

        String method = multiple ? "addReferences" : "addReference";
        String target = quote(targetModule.getName()) + ", " + quote(link.getTargetDocumentName());

        if (accesser.isMultilingual() && GeneratorDataRegistry.getInstance().getContext().areLanguagesSupported()) {
            for (String lang : GeneratorDataRegistry.getInstance().getContext().getLanguages())
                appendStatement(method, "(references, ", target, ", ", var, ".get", accesser.getAccesserName(lang), "())");
            return;
        }

        appendStatement(method, "(references, ", target, ", ", var, ".get", accesser.getAccesserName(), "())");
    }

    /**
     * Appends the collection of the files a document points into the file storage with. They are not part of
     * the transfer; the engine reports them so nobody assumes they came along.
     *
     * @param doc document to describe
     * @param var name of the document variable in the generated method
     */
    private void appendFiles(MetaDocument doc, String var) {
        for (MetaProperty property : doc.getProperties()) {
            if (property instanceof MetaListProperty) {
                MetaListProperty list = (MetaListProperty) property;
                if (list.getContainedProperty().getType() == MetaProperty.Type.IMAGE)
                    appendFile(list, var, true);
                continue;
            }

            if (property.getType() == MetaProperty.Type.IMAGE)
                appendFile(property, var, false);
        }
    }

    private void appendFile(MetaProperty property, String var, boolean multiple) {
        String method = multiple ? "addFiles" : "addFile";

        if (property.isMultilingual() && GeneratorDataRegistry.getInstance().getContext().areLanguagesSupported()) {
            for (String lang : GeneratorDataRegistry.getInstance().getContext().getLanguages())
                appendStatement(method, "(files, ", var, ".get", property.getAccesserName(lang), "())");
            return;
        }

        appendStatement(method, "(files, ", var, ".get", property.getAccesserName(), "())");
    }

    /**
     * Generates the class that puts every module's support into the runtime registry.
     *
     * @param modules all modules of the project
     * @return the generated class
     */
    private GeneratedClass generateRegistrar(List<MetaModule> modules) {
        GeneratedClass clazz = new GeneratedClass();
        startNewJob(clazz);

        clazz.setPackageName(getRegistrarPackageName());
        clazz.setName(REGISTRAR_NAME);

        clazz.addImport(RUNTIME_PACKAGE + ".TransferSupportRegistry");
        for (MetaModule module : modules)
            clazz.addImport(getSupportImport(module));

        startClassBody();
        appendGenerationPoint("generateRegistrar");

        appendStatement("private static boolean registered = false");
        emptyline();

        openFun("private " + REGISTRAR_NAME + "()");
        closeBlockNEW();
        emptyline();

        appendComment("Registers every module of this project for document transfer. Called at startup, idempotent.");
        openFun("public static synchronized void registerAll()");
        appendString("if (registered)");
        appendIncreasedStatement("return");
        emptyline();
        for (MetaModule module : modules)
            appendStatement("TransferSupportRegistry.register(new ", getSupportName(module), "())");
        appendStatement("registered = true");
        closeBlockNEW();

        return clazz;
    }

    /**
     * Path of a document's rest collection, relative to the api base. Mirrors the {@code @Path} the rest
     * resource is generated with.
     *
     * @param module the document's module
     * @param doc    the document
     * @return e.g. {@code asresourcedata/localizationbundle}
     */
    private String getRestPath(MetaModule module, MetaDocument doc) {
        return module.getName().toLowerCase() + "/" + doc.getName().toLowerCase();
    }

    public static String getSupportName(MetaModule module) {
        return module.getName() + "TransferSupport";
    }

    public static String getPackageName(MetaModule module) {
        return GeneratorDataRegistry.getInstance().getContext().getPackageName(module) + ".transfer";
    }

    public static String getSupportImport(MetaModule module) {
        return getPackageName(module) + "." + getSupportName(module);
    }

    public static String getRegistrarPackageName() {
        return GeneratorDataRegistry.getInstance().getContext().getPackageName(MetaModule.SHARED) + ".transfer";
    }

    public static String getRegistrarFullName() {
        return getRegistrarPackageName() + "." + REGISTRAR_NAME;
    }
}
