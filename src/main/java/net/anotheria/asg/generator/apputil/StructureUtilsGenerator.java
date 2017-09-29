package net.anotheria.asg.generator.apputil;

import net.anotheria.asg.generator.AbstractGenerator;
import net.anotheria.asg.generator.FileEntry;
import net.anotheria.asg.generator.GeneratedClass;
import net.anotheria.asg.generator.GeneratorDataRegistry;
import net.anotheria.asg.generator.TypeOfClass;
import net.anotheria.asg.generator.meta.MetaDocument;
import net.anotheria.asg.generator.meta.MetaModule;
import net.anotheria.asg.generator.view.meta.MetaView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class StructureUtilsGenerator extends AbstractGenerator {

    public List<FileEntry> generateModules(List<MetaModule> modules) {
        List<FileEntry> entries = new ArrayList<>();
        entries.add(new FileEntry(generateEnumModules(modules)));
        entries.add(new FileEntry(generateEnumDocuments(modules)));
        return entries;
    }

    public List<FileEntry> generateViews(List<MetaView> views) {
        List<FileEntry> entries = new ArrayList<>();
        entries.add(new FileEntry(generateEnumViews(views)));
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

    private GeneratedClass generateEnumViews(List<MetaView> views) {
        GeneratedClass clazz = new GeneratedClass();
        startNewJob(clazz);
        appendGenerationPoint("generateEnumViews");

        clazz.setPackageName(GeneratorDataRegistry.getInstance().getContext().getPackageName(MetaModule.SHARED)+".util");
        clazz.addImport("net.anotheria.util.StringUtils");
        clazz.setType(TypeOfClass.ENUM);
        clazz.setName("ViewName");
        startClassBody();

        for(MetaView view: views) {
            appendString("VIEW_" + view.getName().toUpperCase() + "(\"" + view.getName() + "\"),");
        }
        appendStatement();
        emptyline();
        appendStatement("private String name");
        emptyline();
        appendString("ViewName (String aName) {");
        increaseIdent();
        appendStatement("name = aName");
        closeBlockNEW();
        emptyline();
        appendString("public static ViewName byValue (final String value) {");
        increaseIdent();
        appendString("if (StringUtils.isEmpty(value))");
        increaseIdent();
        appendStatement("throw new IllegalArgumentException(\"Value is not valid\")");
        decreaseIdent();
        appendString("for (ViewName viewName: ViewName.values()) {");
        increaseIdent();
        appendString("if (viewName.name.equals(value)) {");
        increaseIdent();
        appendStatement("return viewName");
        closeBlockNEW();
        closeBlockNEW();
        appendStatement("throw new IllegalArgumentException(\"No such value in DataType\")");
        closeBlockNEW();
        emptyline();

        appendString("public String getName() {");
        increaseIdent();
        appendStatement("return name");
        closeBlockNEW();

        return clazz;
    }
}