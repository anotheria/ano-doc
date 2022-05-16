package net.anotheria.asg.generator.view.action;

import net.anotheria.asg.generator.AbstractGenerator;
import net.anotheria.asg.generator.Context;
import net.anotheria.asg.generator.FileEntry;
import net.anotheria.asg.generator.GeneratedClass;
import net.anotheria.asg.generator.GeneratorDataRegistry;
import net.anotheria.asg.generator.meta.MetaModule;
import net.anotheria.asg.generator.view.CMSMappingsConfiguratorGenerator;
import net.anotheria.util.Date;
import net.anotheria.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Generator class for the index page action in cms.
 *
 * @author abolbat
 * @version $Id: $Id
 */
public class LocalizationBundleExportViewActionGenerator extends AbstractGenerator {


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
        return "LocalizationBundleExportMafAction";
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
        clazz.addImport("net.anotheria.anosite.gen.shared.service.*");
        clazz.addImport("net.anotheria.maf.bean.FormBean");
        clazz.addImport("net.anotheria.webutils.bean.NavigationItemBean");
        clazz.addImport("net.anotheria.asg.util.DocumentChange");
        clazz.addImport("net.anotheria.asg.util.CmsChangesTracker");
        clazz.addImport(CMSMappingsConfiguratorGenerator.getClassName());
        clazz.addImport("java.util.Collections");
        clazz.addImport("java.util.Collection");
        clazz.addImport("java.util.List");
        clazz.addImport("java.util.ArrayList");
        clazz.addImport(Date.class);


        clazz.setParent("BaseToolsAction");
        clazz.setName(getLocalizationBundleExportPageActionName());

        startClassBody();
        appendString("protected boolean isAuthorizationRequired() {");
        increaseIdent();
        appendStatement("return true");
        append(closeBlock());
        emptyline();
        appendString("public ActionCommand anoDocExecute(ActionMapping aMapping, FormBean aAf, HttpServletRequest aReq, HttpServletResponse aRes) throws Exception {");
        increaseIdent();
        String langUtil = StringUtils.capitalize(context.getApplicationName()) + "LanguageUtils";
        appendString("List<String> languages = " + langUtil + ".getSupportedLanguages();");
        appendString("aReq.setAttribute(\"languages\", languages);");
        appendString("aReq.setAttribute(\"selectedLanguage\", \"AT\");");
        appendStatement("return aMapping.success()");
        append(closeBlock());
        emptyline();

        appendString("@Override");
        increaseIdent();
        appendStatement("protected String getTitle() {");
        appendStatement("return \"LocalizationBundleExport\"");
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
