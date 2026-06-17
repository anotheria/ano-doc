package net.anotheria.asg.generator.mcp;

import net.anotheria.asg.generator.AbstractGenerator;
import net.anotheria.asg.generator.Context;
import net.anotheria.asg.generator.FileEntry;
import net.anotheria.asg.generator.GeneratedClass;
import net.anotheria.asg.generator.GeneratorDataRegistry;
import net.anotheria.asg.generator.IGenerateable;
import net.anotheria.asg.generator.IGenerator;
import net.anotheria.asg.generator.meta.MetaContainerProperty;
import net.anotheria.asg.generator.meta.MetaDocument;
import net.anotheria.asg.generator.meta.MetaModule;
import net.anotheria.asg.generator.meta.MetaProperty;
import net.anotheria.asg.generator.model.DataFacadeGenerator;
import net.anotheria.asg.generator.model.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Generates one {Module}McpTools.java per module.
 * Each document in the module gets three inner static classes: List, Get, Update.
 * All classes implement McpTool — register only those relevant to your use case.
 */
public class McpToolsGenerator extends AbstractGenerator implements IGenerator {

    @Override
    public List<FileEntry> generate(IGenerateable gmodule) {
        MetaModule module = (MetaModule) gmodule;
        List<FileEntry> ret = new ArrayList<>();
        ret.add(new FileEntry(generateMcpTools(module)));
        return ret;
    }

    private GeneratedClass generateMcpTools(MetaModule module) {
        Context context = GeneratorDataRegistry.getInstance().getContext();
        String className = getClassName(module);
        String serviceInterface = ServiceGenerator.getInterfaceName(module);

        GeneratedClass clazz = new GeneratedClass();
        startNewJob(clazz);

        clazz.setPackageName(getPackageName(module));
        clazz.setName(className);

        clazz.addImport("java.util.ArrayList");
        clazz.addImport("java.util.List");
        clazz.addImport("net.anotheria.anoprise.metafactory.MetaFactory");
        clazz.addImport("net.anotheria.anosite.cms.mcp.McpTool");
        clazz.addImport("org.codehaus.jettison.json.JSONArray");
        clazz.addImport("org.codehaus.jettison.json.JSONException");
        clazz.addImport("org.codehaus.jettison.json.JSONObject");
        clazz.addImport("org.slf4j.Logger");
        clazz.addImport("org.slf4j.LoggerFactory");
        clazz.addImport(ServiceGenerator.getInterfaceImport(module));

        for (MetaDocument doc : module.getDocuments()) {
            clazz.addImport(DataFacadeGenerator.getDocumentImport(doc));
        }

        startClassBody();
        appendGenerationPoint("generateMcpTools");

        appendStatement("private static final Logger LOG = LoggerFactory.getLogger(", className, ".class)");
        emptyline();

        openFun("private static " + serviceInterface + " service() throws Exception");
        appendStatement("return MetaFactory.get(", serviceInterface, ".class)");
        closeBlockNEW();

        for (MetaDocument doc : module.getDocuments()) {
            emptyline();
            generateDocumentTools(doc, module, context);
        }

        emptyline();
        generateBundleMethods(module);

        return clazz;
    }

    private void generateDocumentTools(MetaDocument doc, MetaModule module, Context context) {
        String moduleLower = module.getName().toLowerCase();
        String docName = doc.getName();
        String multipleName = doc.getMultiple();
        String serviceInterface = ServiceGenerator.getInterfaceName(module);
        List<MetaProperty> simpleProps = simpleProperties(doc);

        appendString("// ===================== ", docName, " =====================");
        emptyline();

        generateListTool(moduleLower, docName, multipleName, simpleProps);
        generateGetTool(doc, moduleLower, docName, context, simpleProps);
        generateUpdateTool(doc, moduleLower, docName, serviceInterface, context, simpleProps);
    }

    private void generateListTool(String moduleLower, String docName,
                                  String multipleName, List<MetaProperty> simpleProps) {
        String toolName = moduleLower + "_list_" + multipleName.toLowerCase();
        String className = "List" + multipleName;

        appendString("public static class ", className, " implements McpTool {");
        increaseIdent();

        appendString("@Override public String name() { return ", quote(toolName), "; }");
        appendString("@Override public String description() { return ", quote("List all " + docName + " documents."), "; }");
        emptyline();

        openFun("@Override public JSONObject inputSchema() throws JSONException");
        appendStatement("return new JSONObject()",
                "    .put(\"type\", \"object\")",
                "    .put(\"properties\", new JSONObject())",
                "    .put(\"required\", new JSONArray())");
        closeBlockNEW();
        emptyline();

        openFun("@Override public String execute(JSONObject args) throws Exception");
        appendStatement("StringBuilder sb = new StringBuilder()");
        appendStatement("for (", docName, " doc : service().get", multipleName, "()) {");
        increaseIdent();

        MetaProperty nameField = findNameField(simpleProps);
        if (nameField != null) {
            String getter = "get" + nameField.getAccesserName() + "()";
            appendStatement("sb.append(doc.getId()).append(\"  \").append(doc.", getter, ").append(\"\\n\")");
        } else {
            appendStatement("sb.append(doc.getId()).append(\"\\n\")");
        }

        decreaseIdent();
        appendString("}");
        appendStatement("return sb.isEmpty() ? \"No ", multipleName, " found.\" : sb.toString().trim()");
        closeBlockNEW();

        decreaseIdent();
        appendString("}");
        emptyline();
    }

    private void generateGetTool(MetaDocument doc, String moduleLower, String docName,
                                 Context context, List<MetaProperty> simpleProps) {
        String toolName = moduleLower + "_get_" + docName.toLowerCase();
        String className = "Get" + docName;

        appendString("public static class ", className, " implements McpTool {");
        increaseIdent();

        appendString("@Override public String name() { return ", quote(toolName), "; }");
        appendString("@Override public String description() { return ", quote("Get a " + docName + " by id, returning all fields."), "; }");
        emptyline();

        openFun("@Override public JSONObject inputSchema() throws JSONException");
        appendStatement("JSONObject props = new JSONObject()");
        appendStatement("props.put(\"id\", new JSONObject().put(\"type\", \"string\").put(\"description\", \"Document id\"))");
        appendStatement("return new JSONObject()",
                "    .put(\"type\", \"object\")",
                "    .put(\"properties\", props)",
                "    .put(\"required\", new JSONArray().put(\"id\"))");
        closeBlockNEW();
        emptyline();

        openFun("@Override public String execute(JSONObject args) throws Exception");
        appendStatement(docName, " doc = service().get", docName, "(args.getString(\"id\"))");
        appendStatement("StringBuilder sb = new StringBuilder()");
        appendStatement("sb.append(\"id: \").append(doc.getId()).append(\"\\n\")");

        for (MetaProperty p : simpleProps) {
            if (p.isMultilingual() && context.areLanguagesSupported()) {
                for (String lang : context.getLanguages()) {
                    appendStatement("sb.append(", quote(p.getName(lang) + ": "),
                            ").append(doc.get", p.getAccesserName(lang), "()).append(\"\\n\")");
                }
            } else {
                appendStatement("sb.append(", quote(p.getName() + ": "),
                        ").append(doc.get", p.getAccesserName(), "()).append(\"\\n\")");
            }
        }
        for (MetaProperty link : doc.getLinks()) {
            if (link.isMultilingual() && context.areLanguagesSupported()) {
                for (String lang : context.getLanguages()) {
                    appendStatement("sb.append(", quote(link.getName(lang) + ": "),
                            ").append(doc.get", link.getAccesserName(lang), "()).append(\"\\n\")");
                }
            } else {
                appendStatement("sb.append(", quote(link.getName() + ": "),
                        ").append(doc.get", link.getAccesserName(), "()).append(\"\\n\")");
            }
        }

        appendStatement("return sb.toString().trim()");
        closeBlockNEW();

        decreaseIdent();
        appendString("}");
        emptyline();
    }

    private void generateUpdateTool(MetaDocument doc, String moduleLower, String docName,
                                    String serviceInterface, Context context, List<MetaProperty> simpleProps) {
        String toolName = moduleLower + "_update_" + docName.toLowerCase();
        String className = "Update" + docName;
        String fieldList = buildFieldList(doc, simpleProps, context);

        appendString("public static class ", className, " implements McpTool {");
        increaseIdent();

        appendString("@Override public String name() { return ", quote(toolName), "; }");
        appendString("@Override public String description() { return ",
                quote("Update a field on a " + docName + ". Fields: " + fieldList), "; }");
        emptyline();

        openFun("@Override public JSONObject inputSchema() throws JSONException");
        appendStatement("JSONObject props = new JSONObject()");
        appendStatement("props.put(\"id\", new JSONObject().put(\"type\", \"string\").put(\"description\", \"Document id\"))");
        appendStatement("props.put(\"field\", new JSONObject().put(\"type\", \"string\").put(\"description\", \"Field: " + fieldList + "\"))");
        appendStatement("props.put(\"value\", new JSONObject().put(\"type\", \"string\").put(\"description\", \"New value as string\"))");
        appendStatement("return new JSONObject()",
                "    .put(\"type\", \"object\")",
                "    .put(\"properties\", props)",
                "    .put(\"required\", new JSONArray().put(\"id\").put(\"field\").put(\"value\"))");
        closeBlockNEW();
        emptyline();

        openFun("@Override public String execute(JSONObject args) throws Exception");
        appendStatement("String id = args.getString(\"id\")");
        appendStatement("String field = args.getString(\"field\")");
        appendStatement("String value = args.getString(\"value\")");
        appendStatement(serviceInterface, " svc = service()");
        appendStatement(docName, " doc = svc.get", docName, "(id)");
        appendString("switch (field) {");
        increaseIdent();

        for (MetaProperty p : simpleProps) {
            if (p.isMultilingual() && context.areLanguagesSupported()) {
                for (String lang : context.getLanguages()) {
                    appendStatement("case ", quote(p.getName(lang)), " -> doc.set",
                            p.getAccesserName(lang), "(", parseConversion(p, "value"), ")");
                }
            } else {
                appendStatement("case ", quote(p.getName()), " -> doc.set",
                        p.getAccesserName(), "(", parseConversion(p, "value"), ")");
            }
        }
        for (MetaProperty link : doc.getLinks()) {
            if (link.isMultilingual() && context.areLanguagesSupported()) {
                for (String lang : context.getLanguages()) {
                    appendStatement("case ", quote(link.getName(lang)), " -> doc.set",
                            link.getAccesserName(lang), "(value)");
                }
            } else {
                appendStatement("case ", quote(link.getName()), " -> doc.set",
                        link.getAccesserName(), "(value)");
            }
        }
        appendStatement("default -> throw new IllegalArgumentException(\"Unknown field: \" + field)");

        closeBlockNEW(); // switch
        appendStatement("svc.update", docName, "(doc)");
        appendStatement("return \"Updated \" + field + \" on " + docName + " \" + id");
        closeBlockNEW(); // execute

        decreaseIdent();
        appendString("}");
        emptyline();
    }

    private void generateBundleMethods(MetaModule module) {
        appendString("// ===================== Bundles =====================");
        emptyline();

        for (MetaDocument doc : module.getDocuments()) {
            String multipleName = doc.getMultiple();
            String docName = doc.getName();
            openFun("public static List<McpTool> all" + multipleName + "()");
            appendStatement("return List.of(",
                    "new List" + multipleName + "(), ",
                    "new Get" + docName + "(), ",
                    "new Update" + docName + "()",
                    ")");
            closeBlockNEW();
            emptyline();
        }

        openFun("public static List<McpTool> all()");
        appendStatement("List<McpTool> tools = new ArrayList<>()");
        for (MetaDocument doc : module.getDocuments()) {
            appendStatement("tools.addAll(all", doc.getMultiple(), "())");
        }
        appendStatement("return tools");
        closeBlockNEW();
        emptyline();
    }

    private String parseConversion(MetaProperty p, String var) {
        MetaProperty.Type t = p.getType();
        if (t == MetaProperty.Type.FLOAT)   return "Float.parseFloat(" + var + ")";
        if (t == MetaProperty.Type.INT)     return "Integer.parseInt(" + var + ")";
        if (t == MetaProperty.Type.LONG)    return "Long.parseLong(" + var + ")";
        if (t == MetaProperty.Type.DOUBLE)  return "Double.parseDouble(" + var + ")";
        if (t == MetaProperty.Type.BOOLEAN) return "Boolean.parseBoolean(" + var + ")";
        return var;
    }

    private String buildFieldList(MetaDocument doc, List<MetaProperty> simpleProps, Context context) {
        StringJoiner sj = new StringJoiner(", ");
        for (MetaProperty p : simpleProps) {
            if (p.isMultilingual() && context.areLanguagesSupported()) {
                for (String lang : context.getLanguages()) {
                    sj.add(p.getName(lang));
                }
            } else {
                sj.add(p.getName());
            }
        }
        for (MetaProperty link : doc.getLinks()) {
            if (link.isMultilingual() && context.areLanguagesSupported()) {
                for (String lang : context.getLanguages()) {
                    sj.add(link.getName(lang));
                }
            } else {
                sj.add(link.getName());
            }
        }
        return sj.toString();
    }

    private MetaProperty findNameField(List<MetaProperty> simpleProps) {
        for (MetaProperty p : simpleProps) {
            if ("name".equalsIgnoreCase(p.getName())) {
                return p;
            }
        }
        return null;
    }

    static List<MetaProperty> simpleProperties(MetaDocument doc) {
        List<MetaProperty> result = new ArrayList<>();
        for (MetaProperty p : doc.getProperties()) {
            if (!(p instanceof MetaContainerProperty)) {
                result.add(p);
            }
        }
        return result;
    }

    public static String getClassName(MetaModule module) {
        return module.getName() + "McpTools";
    }

    public static String getPackageName(MetaModule module) {
        return GeneratorDataRegistry.getInstance().getContext().getPackageName(module) + ".mcp";
    }
}
