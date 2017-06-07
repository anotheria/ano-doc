package net.anotheria.asg.generator.apputil;

import net.anotheria.asg.generator.*;
import net.anotheria.asg.generator.meta.MetaDocument;
import net.anotheria.asg.generator.meta.MetaModule;
import net.anotheria.asg.generator.view.action.ModuleActionsGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ykalapusha
 */
public class BasicServiceUtilGenerator extends AbstractGenerator {

    public List<FileEntry> generate(List<MetaModule> modules) {
        List<FileEntry> entries = new ArrayList<>();
        entries.add(new FileEntry(generateParsingUtilService(modules)));
        entries.add(new FileEntry(generateEnumModules(modules)));
        entries.add(new FileEntry(generateEnumDocuments(modules)));
        return entries;
    }

    private GeneratedClass generateEnumModules(List<MetaModule> modules) {
        GeneratedClass clazz = new GeneratedClass();
        startNewJob(clazz);
        appendGenerationPoint("generateEnumModules");

        clazz.setPackageName(GeneratorDataRegistry.getInstance().getContext().getPackageName(MetaModule.SHARED)+".util");
        clazz.addImport("net.anotheria.util.StringUtils");
        clazz.setType(TypeOfClass.ENUM);
        clazz.setName("ModuleName");
        startClassBody();

        for(MetaModule module: modules) {
            appendString("SERVICE_" + module.getName().toUpperCase() + "(\"" + module.getName() + "\"),");
        }
        appendStatement();
        emptyline();
        appendStatement("private String name");
        emptyline();
        appendString("ModuleName (String aName) {");
        increaseIdent();
        appendStatement("name = aName");
        closeBlockNEW();
        emptyline();
        appendString("public static ModuleName byValue (final String value) {");
        increaseIdent();
        appendString("if (StringUtils.isEmpty(value))");
        increaseIdent();
        appendStatement("throw new IllegalArgumentException(\"Value is not valid\")");
        decreaseIdent();
        appendString("for (ModuleName moduleName: ModuleName.values()) {");
        increaseIdent();
        appendString("if (moduleName.name.equals(value)) {");
        increaseIdent();
        appendStatement("return moduleName");
        closeBlockNEW();
        closeBlockNEW();
        appendStatement("throw new IllegalArgumentException(\"No such value in DataType\")");
        closeBlockNEW();

        return clazz;
    }

    private GeneratedClass generateEnumDocuments (List<MetaModule> modules) {
        GeneratedClass clazz = new GeneratedClass();
        startNewJob(clazz);
        appendGenerationPoint("generateEnumDocuments");

        clazz.setPackageName(GeneratorDataRegistry.getInstance().getContext().getPackageName(MetaModule.SHARED)+".util");
        clazz.addImport("net.anotheria.util.StringUtils");
        clazz.setType(TypeOfClass.ENUM);
        clazz.setName("DocumentName");
        startClassBody();

        for (MetaModule module: modules) {
            for (MetaDocument doc: module.getDocuments()) {
                appendString("DOCUMENT_" + module.getName().toUpperCase() + "_" + doc.getName().toUpperCase() + "(\"" + module.getName() + "_" + doc.getName() + "\"),");
            }
        }
        appendStatement();
        emptyline();
        appendStatement("private String name");
        emptyline();
        appendString("DocumentName (String aName) {");
        increaseIdent();
        appendStatement("name = aName");
        closeBlockNEW();
        emptyline();
        appendString("public static DocumentName byValue (final String value) {");
        increaseIdent();
        appendString("if (StringUtils.isEmpty(value))");
        increaseIdent();
        appendStatement("throw new IllegalArgumentException(\"Value is not valid\")");
        decreaseIdent();
        appendString("for (DocumentName documentName: DocumentName.values()) {");
        increaseIdent();
        appendString("if (documentName.name.equals(value)) {");
        increaseIdent();
        appendStatement("return documentName");
        closeBlockNEW();
        closeBlockNEW();
        appendStatement("throw new IllegalArgumentException(\"No such value in DataType\")");
        closeBlockNEW();

        return clazz;
    }

    private GeneratedClass generateParsingUtilService(List<MetaModule> modules) {

        GeneratedClass clazz = new GeneratedClass();
        startNewJob(clazz);
        appendGenerationPoint("generateParsingUtilService");

        clazz.setPackageName(GeneratorDataRegistry.getInstance().getContext().getPackageName(MetaModule.SHARED)+".util");
        clazz.addImport("net.anotheria.anosite.gen.shared.service.BasicService");
        clazz.addImport("net.anotheria.asg.exception.ASGRuntimeException");
        clazz.addImport("org.codehaus.jettison.json.JSONObject");
        clazz.addImport("org.codehaus.jettison.json.JSONArray");
        clazz.addImport("org.codehaus.jettison.json.JSONException");

        clazz.setName("ParserUtilService");
        clazz.setParent("BasicService");
        startClassBody();

        appendStatement("private static final ParserUtilService instance = new ParserUtilService()");
        emptyline();
        appendString("private ParserUtilService() { }");
        emptyline();
        appendString("public static ParserUtilService getInstance() {");
        increaseIdent();
        appendStatement("return instance");
        closeBlockNEW();
        emptyline();
        appendString("public void executeParsingDocuments (final JSONArray data) throws ASGRuntimeException, JSONException {");
        increaseIdent();
        appendString("for (int i = 0; i < data.length(); i++) {");
        increaseIdent();
        appendStatement("executeParsingDocument(data.getJSONObject(i))");
        closeBlockNEW();
        closeBlockNEW();
        emptyline();
        appendString("private void executeParsingDocument(final JSONObject data) throws ASGRuntimeException {");
        increaseIdent();
        appendStatement("final ModuleName moduleName");
        appendStatement("final DocumentName documentName");
        openTry();
        appendStatement("moduleName = ModuleName.byValue(data.getString(\"service\"))");
        appendStatement("documentName = DocumentName.byValue(data.getString(\"document\"))");
        appendCatch("JSONException");
        appendStatement("throw new ASGRuntimeException(\"Occurred problems with getting document metadata from json:\", e)");
        closeBlockNEW();
        appendStatement("executeParsingInServiceByName(moduleName, documentName, data)");
        closeBlockNEW();
        emptyline();
        appendString("protected void executeParsingInServiceByName(final ModuleName moduleName, final DocumentName documentName, final JSONObject dataObject) throws ASGRuntimeException {");
        increaseIdent();
        appendString("switch (moduleName) {");
        increaseIdent();
        for (MetaModule module: modules) {
            appendString("case SERVICE_" + module.getName().toUpperCase() + ":");
            increaseIdent();
            appendStatement(ModuleActionsGenerator.getServiceGetterCall(module) + ".executeParsingForDocument(documentName, dataObject)");
            appendStatement("break");
            decreaseIdent();
        }
        appendString("default:");
        increaseIdent();
        appendStatement("log.error(\"There is no needed module\")");
        appendStatement("throw new ASGRuntimeException(\"No such module\")");
        closeBlockNEW();
        closeBlockNEW();

        return clazz;
    }
}