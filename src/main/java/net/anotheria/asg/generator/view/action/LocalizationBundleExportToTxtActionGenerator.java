package net.anotheria.asg.generator.view.action;

import net.anotheria.asg.generator.AbstractGenerator;
import net.anotheria.asg.generator.Context;
import net.anotheria.asg.generator.FileEntry;
import net.anotheria.asg.generator.GeneratedClass;
import net.anotheria.asg.generator.GeneratorDataRegistry;
import net.anotheria.asg.generator.meta.MetaModule;
import net.anotheria.asg.generator.view.CMSMappingsConfiguratorGenerator;
import net.anotheria.util.Date;

import java.util.ArrayList;
import java.util.List;

/**
 * Generator class for localization bundle export.
 *
 * @author asamoilich
 * @version $Id: $Id
 */
public class LocalizationBundleExportToTxtActionGenerator extends AbstractGenerator {


    /**
     * <p>generate.</p>
     *
     * @param context
     * @return a {@link List} object.
     */
    public List<FileEntry> generate(Context context) {
        List<FileEntry> files = new ArrayList<FileEntry>();
        files.add(new FileEntry(generateBaseAction(context)));
        return files;
    }

    /**
     * <p>getIndexPagePackageName.</p>
     *
     * @return a {@link String} object.
     */
    public static String getLocalizationBundleExportPagePackageName() {
        return GeneratorDataRegistry.getInstance().getContext().getPackageName(MetaModule.SHARED) + ".action";
    }


    /**
     * <p>getIndexPageActionName.</p>
     *
     * @return a {@link String} object.
     */
    public static String getLocalizationBundleExportPageActionName() {
        return "LocalizationBundleExportToTxtAction";
    }


    /**
     * <p>getIndexPageFullName.</p>
     *
     * @return a {@link String} object.
     */
    public static String getLocalizationBundleExportPageFullName() {
        return getLocalizationBundleExportPagePackageName() + "." + getLocalizationBundleExportPageActionName();
    }


    /**
     * <p>generateBaseAction.</p>
     *
     * @param context
     * @return a {@link GeneratedClass} object.
     */
    public GeneratedClass generateBaseAction(Context context) {
        GeneratedClass clazz = new GeneratedClass();
        startNewJob(clazz);
        appendGenerationPoint("generateBaseAction");
        clazz.setPackageName(getLocalizationBundleExportPagePackageName());


        clazz.addImport("javax.servlet.http.HttpServletRequest");
        clazz.addImport("javax.servlet.http.HttpServletResponse");
        emptyline();
        clazz.addImport("net.anotheria.maf.action.ActionCommand");
        clazz.addImport("net.anotheria.maf.action.ActionMapping");
        clazz.addImport("net.anotheria.anodoc.data.StringProperty");
        clazz.addImport("net.anotheria.anosite.gen.asresourcedata.data.LocalizationBundleDocument");
        clazz.addImport("net.anotheria.anoprise.metafactory.*");
        clazz.addImport("net.anotheria.anosite.gen.asresourcedata.service.IASResourceDataService");
        clazz.addImport("net.anotheria.anosite.gen.asresourcedata.data.LocalizationBundle");
        clazz.addImport("net.anotheria.maf.bean.FormBean");
        clazz.addImport("java.util.List");


        clazz.setParent("BaseToolsAction");
        clazz.setName(getLocalizationBundleExportPageActionName());

        startClassBody();
        appendString("private IASResourceDataService iasResourceDataService;");
        appendString("public LocalizationBundleExportToTxtAction() {\n" +
                "        try {\n" +
                "            iasResourceDataService = MetaFactory.get(IASResourceDataService.class);\n" +
                "        } catch (MetaFactoryException e) {\n" +
                "            throw new RuntimeException(\"Unable to create service\", e);\n" +
                "        }\n" +
                "    }");

        appendString("protected boolean isAuthorizationRequired() {");
        increaseIdent();
        appendStatement("return true");
        append(closeBlock());
        emptyline();
        appendString("public ActionCommand anoDocExecute(ActionMapping aMapping, FormBean aAf, HttpServletRequest aReq, HttpServletResponse aRes) throws Exception {");
        appendString("String locale = aReq.getParameter(\"locale\");");
        appendString("try {");
        increaseIdent();
        appendString("List<LocalizationBundle> localizationBundles = iasResourceDataService.getLocalizationBundles();");
        appendString(" StringBuilder sb = new StringBuilder();\n" +
                "            for (LocalizationBundle bundle : localizationBundles) {\n" +
                "                if (bundle instanceof LocalizationBundleDocument) {\n" +
                "                    LocalizationBundleDocument bundleDocument = (LocalizationBundleDocument) bundle;\n" +
                "                    StringProperty stringProperty = bundleDocument.getStringProperty(\"messages_\" + locale);\n" +
                "                    if (stringProperty == null || stringProperty.getString() == null) continue;\n" +
                "                    String messages = stringProperty.getString();\n" +
                "                    String[] values = messages.split(\"\\n\");\n" +
                "\n" +
                "                    for (String row : values) {\n" +
                "                        row = row.trim();\n" +
                "                        if (!row.isEmpty()) {\n" +
                "                            sb.append(bundle.getId()).append(\".\").append(row).append(\"\\n\");\n" +
                "                        }\n" +
                "                    }\n" +
                "                }\n" +
                "            }");
        appendString("aRes.getOutputStream().write((sb.toString()).getBytes(\"UTF-8\"));");
        appendString("aRes.getOutputStream().flush();");
        appendString("} catch (Exception e) {e.printStackTrace();}");
        decreaseIdent();
        appendString("return null;");

        append(closeBlock());
        emptyline();

        appendString("@Override");
        increaseIdent();
        appendStatement("protected String getTitle() {");
        appendStatement("return \"LocalizationBundleExportToTxt\"");
        append(closeBlock());
        emptyline();

        appendString("@Override");
        appendString("protected String getCurrentDocumentDefName() {");
        increaseIdent();
        appendStatement("return null");
        closeBlock("getCurrentDocumentDefName");
        emptyline();

        appendString("@Override");
        appendString("protected String getCurrentModuleDefName() {");
        increaseIdent();
        appendStatement("return null");
        closeBlock("getCurrentModuleDefName");
        emptyline();

        return clazz;
    }

}
