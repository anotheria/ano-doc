package net.anotheria.asg.generator.restapi;

import net.anotheria.asg.generator.AbstractGenerator;
import net.anotheria.asg.generator.Context;
import net.anotheria.asg.generator.FileEntry;
import net.anotheria.asg.generator.GeneratedClass;
import net.anotheria.asg.generator.GeneratorDataRegistry;
import net.anotheria.asg.generator.IGenerateable;
import net.anotheria.asg.generator.IGenerator;
import net.anotheria.asg.generator.meta.MetaDocument;
import net.anotheria.asg.generator.meta.MetaModule;
import net.anotheria.asg.generator.meta.MetaProperty;
import net.anotheria.asg.generator.meta.MetaTableProperty;
import net.anotheria.asg.generator.model.DataFacadeGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Generates a REST VO (value object / DTO) for a document.
 * The VO implements DataObject but does not expose the underlying document implementation.
 *
 * <p>It carries everything that makes up the document's content: simple properties, list properties (lists of
 * values as well as lists of links), the columns of table properties and links. That completeness is what the
 * document transfer rests on - a VO that dropped a field would publish a document that silently lost it.
 */
public class RestVOGenerator extends AbstractGenerator implements IGenerator {

    @Override
    public List<FileEntry> generate(IGenerateable gdoc) {
        MetaDocument doc = (MetaDocument) gdoc;
        List<FileEntry> ret = new ArrayList<>();
        ret.add(new FileEntry(generateVO(doc)));
        return ret;
    }

    private GeneratedClass generateVO(MetaDocument doc) {
        MetaModule module = doc.getParentModule();
        Context context = GeneratorDataRegistry.getInstance().getContext();

        GeneratedClass clazz = new GeneratedClass();
        startNewJob(clazz);

        clazz.setPackageName(getPackageName(module));
        clazz.setName(getVOName(doc));
        clazz.addInterface("DataObject");

        clazz.addImport("java.util.List");
        clazz.addImport("com.fasterxml.jackson.annotation.JsonIgnore");
        clazz.addImport("com.fasterxml.jackson.annotation.JsonIgnoreProperties");
        clazz.addImport("net.anotheria.asg.data.DataObject");
        clazz.addImport("net.anotheria.asg.data.ObjectInfo");
        clazz.addImport("net.anotheria.util.xml.XMLNode");
        clazz.addImport(DataFacadeGenerator.getDocumentImport(doc));

        //a VO travels between instances that are not necessarily on the same build. A field the sender knows
        //and the receiver doesn't must not fail the whole document - it is content the receiver can't use yet,
        //not a broken request.
        clazz.addAnnotation("@JsonIgnoreProperties(ignoreUnknown = true)");

        startClassBody();
        appendGenerationPoint("generateVO");

        appendStatement("private String id");
        emptyline();

        List<MetaProperty> properties = voProperties(doc);
        for (MetaProperty p : properties) {
            if (p.isMultilingual() && context.areLanguagesSupported()) {
                for (String lang : context.getLanguages()) {
                    appendStatement("private ", p.toJavaType(), " ", p.getName(lang));
                }
            } else {
                appendStatement("private ", p.toJavaType(), " ", p.getName());
            }
        }
        for (MetaProperty link : doc.getLinks()) {
            if (link.isMultilingual() && context.areLanguagesSupported()) {
                for (String lang : context.getLanguages()) {
                    appendStatement("private String ", link.getName(lang));
                }
            } else {
                appendStatement("private String ", link.getName());
            }
        }
        emptyline();

        appendString("public ", getVOName(doc), "() {}");
        emptyline();

        // id
        openFun("public String getId()");
        appendStatement("return id");
        closeBlockNEW();
        emptyline();
        openFun("public void setId(String id)");
        appendStatement("this.id = id");
        closeBlockNEW();
        emptyline();

        // property getters/setters
        for (MetaProperty p : properties) {
            if (p.isMultilingual() && context.areLanguagesSupported()) {
                for (String lang : context.getLanguages()) {
                    generateGetterSetter(p.toJavaType(), p.getName(lang), p.getAccesserName(lang));
                }
            } else {
                generateGetterSetter(p.toJavaType(), p.getName(), p.getAccesserName());
            }
        }
        for (MetaProperty link : doc.getLinks()) {
            if (link.isMultilingual() && context.areLanguagesSupported()) {
                for (String lang : context.getLanguages()) {
                    generateGetterSetter("String", link.getName(lang), link.getAccesserName(lang));
                }
            } else {
                generateGetterSetter("String", link.getName(), link.getAccesserName());
            }
        }

        // static factory: document → VO
        openFun("public static " + getVOName(doc) + " from(" + doc.getName() + " doc)");
        appendStatement(getVOName(doc), " vo = new ", getVOName(doc), "()");
        appendStatement("vo.setId(doc.getId())");
        for (MetaProperty p : properties) {
            if (p.isMultilingual() && context.areLanguagesSupported()) {
                for (String lang : context.getLanguages()) {
                    appendStatement("vo.set", p.getAccesserName(lang), "(doc.get", p.getAccesserName(lang), "())");
                }
            } else {
                appendStatement("vo.set", p.getAccesserName(), "(doc.get", p.getAccesserName(), "())");
            }
        }
        for (MetaProperty link : doc.getLinks()) {
            if (link.isMultilingual() && context.areLanguagesSupported()) {
                for (String lang : context.getLanguages()) {
                    appendStatement("vo.set", link.getAccesserName(lang), "(doc.get", link.getAccesserName(lang), "())");
                }
            } else {
                appendStatement("vo.set", link.getAccesserName(), "(doc.get", link.getAccesserName(), "())");
            }
        }
        appendStatement("return vo");
        closeBlockNEW();
        emptyline();

        // DataObject interface stubs.
        // They are @JsonIgnore'd on purpose: they are getters, so jackson would serialize them as fields of
        // the document, but nothing can read them back - they have no setters. A VO that carried them would
        // not survive its own round trip, which is exactly what a document transfer does.
        appendString("@JsonIgnore");
        appendString("@Override public Object getPropertyValue(String propertyName) {");
        increaseIdent();
        appendStatement("return null");
        closeBlockNEW();
        emptyline();

        appendString("@JsonIgnore");
        appendString("@Override public String getDefinedName() {");
        increaseIdent();
        appendStatement("return ", quote(doc.getName()));
        closeBlockNEW();
        emptyline();

        appendString("@JsonIgnore");
        appendString("@Override public String getDefinedParentName() {");
        increaseIdent();
        appendStatement("return ", quote(module.getName()));
        closeBlockNEW();
        emptyline();

        appendString("@JsonIgnore");
        appendString("@Override public XMLNode toXMLNode() {");
        increaseIdent();
        appendStatement("return new XMLNode(", quote(getVOName(doc)), ")");
        closeBlockNEW();
        emptyline();

        appendString("@JsonIgnore");
        appendString("@Override public ObjectInfo getObjectInfo() {");
        increaseIdent();
        appendStatement("return new ObjectInfo(this)");
        closeBlockNEW();
        emptyline();

        appendString("@Override public Object clone() throws CloneNotSupportedException {");
        increaseIdent();
        appendStatement("return super.clone()");
        closeBlockNEW();

        return clazz;
    }

    private void generateGetterSetter(String type, String fieldName, String accesserName) {
        openFun("public " + type + " get" + accesserName + "()");
        appendStatement("return ", fieldName);
        closeBlockNEW();
        emptyline();

        openFun("public void set" + accesserName + "(" + type + " value)");
        appendStatement("this.", fieldName, " = value");
        closeBlockNEW();
        emptyline();
    }

    /**
     * The document's properties as the VO carries them: simple properties and list properties as they are,
     * table properties flattened into their columns, which is how the document facade exposes them too.
     *
     * @param doc document to describe
     * @return properties to generate a VO field for, in declaration order
     */
    static List<MetaProperty> voProperties(MetaDocument doc) {
        List<MetaProperty> result = new ArrayList<>();
        for (MetaProperty p : doc.getProperties()) {
            if (p instanceof MetaTableProperty) {
                result.addAll(((MetaTableProperty) p).getColumns());
                continue;
            }
            result.add(p);
        }
        return result;
    }

    public static String getVOName(MetaDocument doc) {
        return doc.getName() + "VO";
    }

    public static String getPackageName(MetaModule module) {
        return GeneratorDataRegistry.getInstance().getContext().getPackageName(module) + ".rest";
    }

    public static String getVOImport(MetaDocument doc) {
        return getPackageName(doc.getParentModule()) + "." + getVOName(doc);
    }
}
