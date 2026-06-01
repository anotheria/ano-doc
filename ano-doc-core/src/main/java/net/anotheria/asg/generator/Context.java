package net.anotheria.asg.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.anotheria.asg.generator.meta.MetaDocument;
import net.anotheria.asg.generator.meta.MetaModule;

/**
 * The Context is available at the generation time and contains contents of the context.xml.
 *
 * @author another
 * @version $Id: $Id
 */
public class Context {
	/**
	 * Name of the root package for the generated file.
	 */
	private String packageName;
	/**
	 * Owner of the application (used as db user name etc).
	 */
	private String owner;
	/**
	 * Configured application name.
	 */
	private String applicationName;
	/**
	 * Mapping for the cms servlet.
	 */
	private String servletMapping;
	/**
	 * Default encoding.
	 */
	private String encoding;
	/**
	 * Part of application url path between / and /, for example if you want the cms to be accessible under /xyz/cms/pagexShow the application url path is xyz. 
	 */
	private String applicationURLPath;
	
	/**
	 * If true support for multilanguage is enabled.
	 */
	private boolean multilanguageSupport;
	/**
	 * List of supported languages.
	 */
	private List<String> languages;
	/**
	 * Default language.
	 */
	private String defaultLanguage;
	/**
	 * Additional generation options.
	 */
	private GenerationOptions options;
	/**
	 * Special context parameters.
	 */
	private Map<String, ContextParameter> parameters;
	
	/**
	 * Allows generation of the CMS 1.0 (struts implementation)
	 */
	private boolean cmsVersion1 = true;
	
	/**
	 * Allows generation of CMS 2.0 (ano-maf implementation)
	 */
	private boolean cmsVersion2 = false;
	
	/**
	 * <p>Constructor for Context.</p>
	 */
	public Context(){
		parameters = new HashMap<String, ContextParameter>();
	}
	
	/**
	 * <p>Getter for the field <code>packageName</code>.</p>
	 *
	 * @deprecated use getPackageName(MetaModule m);
	 * @return a {@link java.lang.String} object.
	 */
	public String getPackageName() {
		return packageName;
	}
	
	/**
	 * <p>getTopPackageName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTopPackageName(){
		return packageName;
	}
	
	/**
	 * <p>getJspPackageName.</p>
	 *
	 * @param doc a {@link net.anotheria.asg.generator.meta.MetaDocument} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String getJspPackageName(MetaDocument doc){
		return getJspPackageName(doc.getParentModule());
	}
	
	/**
	 * <p>getJspPackageName.</p>
	 *
	 * @param module a {@link net.anotheria.asg.generator.meta.MetaModule} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String getJspPackageName(MetaModule module){
		return getPackageName(module)+".jsp";
	}

	/**
	 * <p>getDataPackageName.</p>
	 *
	 * @param doc a {@link net.anotheria.asg.generator.meta.MetaDocument} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String getDataPackageName(MetaDocument doc){
		return getDataPackageName(doc.getParentModule());
	}
	
	/**
	 * <p>getDataPackageName.</p>
	 *
	 * @param module a {@link net.anotheria.asg.generator.meta.MetaModule} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String getDataPackageName(MetaModule module){
		return getPackageName(module)+".data";
	}

	/**
	 * <p>getServicePackageName.</p>
	 *
	 * @param doc a {@link net.anotheria.asg.generator.meta.MetaDocument} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String getServicePackageName(MetaDocument doc){
		return getServicePackageName(doc.getParentModule());
	}
	
	/**
	 * <p>getServicePackageName.</p>
	 *
	 * @param module a {@link net.anotheria.asg.generator.meta.MetaModule} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String getServicePackageName(MetaModule module){
		return getPackageName(module)+".service";
	}
	
	/**
	 * <p>Getter for the field <code>packageName</code>.</p>
	 *
	 * @param module a {@link net.anotheria.asg.generator.meta.MetaModule} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String getPackageName(MetaModule module){
		return packageName+"."+module.getName().toLowerCase();
	}
	
	/**
	 * <p>Getter for the field <code>packageName</code>.</p>
	 *
	 * @param doc a {@link net.anotheria.asg.generator.meta.MetaDocument} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String getPackageName(MetaDocument doc){
		return getPackageName(doc.getParentModule());
	}

	/**
	 * <p>Setter for the field <code>packageName</code>.</p>
	 *
	 * @param string a {@link java.lang.String} object.
	 */
	public void setPackageName(String string) {
		packageName = string;
	}

    /**
     * <p>Getter for the field <code>owner</code>.</p>
     *
     * @return Returns the owner.
     */
    public String getOwner() {
        return owner;
    }
    /**
     * <p>Setter for the field <code>owner</code>.</p>
     *
     * @param owner The owner to set.
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }
	/**
	 * <p>Getter for the field <code>applicationName</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getApplicationName() {
		return applicationName;
	}

	/**
	 * <p>Setter for the field <code>applicationName</code>.</p>
	 *
	 * @param string a {@link java.lang.String} object.
	 */
	public void setApplicationName(String string) {
		applicationName = string;
	}

	/**
	 * <p>Getter for the field <code>servletMapping</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getServletMapping() {
		return servletMapping;
	}

	/**
	 * <p>Setter for the field <code>servletMapping</code>.</p>
	 *
	 * @param string a {@link java.lang.String} object.
	 */
	public void setServletMapping(String string) {
		servletMapping = string;
	}

	/**
	 * <p>Getter for the field <code>encoding</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * <p>Setter for the field <code>encoding</code>.</p>
	 *
	 * @param string a {@link java.lang.String} object.
	 */
	public void setEncoding(String string) {
		encoding = string;
	}

	/**
	 * <p>Getter for the field <code>defaultLanguage</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getDefaultLanguage() {
		return defaultLanguage;
	}

	/**
	 * <p>Setter for the field <code>defaultLanguage</code>.</p>
	 *
	 * @param defaultLanguage a {@link java.lang.String} object.
	 */
	public void setDefaultLanguage(String defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
	}

	/**
	 * <p>Getter for the field <code>languages</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<String> getLanguages() {
		return languages;
	}

	/**
	 * <p>Setter for the field <code>languages</code>.</p>
	 *
	 * @param languages a {@link java.util.List} object.
	 */
	public void setLanguages(List<String> languages) {
		this.languages = languages;
	}
	
	/**
	 * <p>areLanguagesSupported.</p>
	 *
	 * @return a boolean.
	 */
	public boolean areLanguagesSupported(){
		return multilanguageSupport;
	}
	
	/**
	 * <p>enableMultiLanguageSupport.</p>
	 */
	public void enableMultiLanguageSupport(){
		multilanguageSupport = true;
		languages = new ArrayList<String>();
	}
	
	/**
	 * <p>addLanguage.</p>
	 *
	 * @param l a {@link java.lang.String} object.
	 */
	public void addLanguage(String l){
		languages.add(l);
	}
	
	/**
	 * <p>addContextParameter.</p>
	 *
	 * @param p a {@link net.anotheria.asg.generator.ContextParameter} object.
	 */
	public void addContextParameter(ContextParameter p){
		parameters.put(p.getName(), p);
	}
	
	/**
	 * <p>addContextParameter.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a {@link java.lang.String} object.
	 */
	public void addContextParameter(String name, String value){
		addContextParameter(new ContextParameter(name, value));
	}
	
	/**
	 * <p>getContextParameters.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<ContextParameter> getContextParameters(){
		ArrayList<ContextParameter> ret = new ArrayList<ContextParameter>();
		ret.addAll(parameters.values());
		return ret;
	}
	
	/**
	 * <p>getContextParameter.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link net.anotheria.asg.generator.ContextParameter} object.
	 */
	public ContextParameter getContextParameter(String name){
		return parameters.get(name);
	}

	/**
	 * <p>Getter for the field <code>applicationURLPath</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getApplicationURLPath() {
		return applicationURLPath == null || applicationURLPath.length() == 0? "" : "/" + applicationURLPath;
	}

	/**
	 * <p>Setter for the field <code>applicationURLPath</code>.</p>
	 *
	 * @param applicationURLPath a {@link java.lang.String} object.
	 */
	public void setApplicationURLPath(String applicationURLPath) {
		this.applicationURLPath = applicationURLPath;
	}

	/**
	 * <p>Getter for the field <code>options</code>.</p>
	 *
	 * @return a {@link net.anotheria.asg.generator.GenerationOptions} object.
	 */
	public GenerationOptions getOptions() {
		return options;
	}

	/**
	 * <p>Setter for the field <code>options</code>.</p>
	 *
	 * @param options a {@link net.anotheria.asg.generator.GenerationOptions} object.
	 */
	public void setOptions(GenerationOptions options) {
		this.options = options;
	}

	/**
	 * <p>isCmsVersion1.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isCmsVersion1() {
		return cmsVersion1;
	}

	/**
	 * <p>Setter for the field <code>cmsVersion1</code>.</p>
	 *
	 * @param cmsVersion1Generator a boolean.
	 */
	public void setCmsVersion1(boolean cmsVersion1Generator) {
		this.cmsVersion1 = cmsVersion1Generator;
	}

	/**
	 * <p>isCmsVersion2.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isCmsVersion2() {
		return cmsVersion2;
	}

	/**
	 * <p>Setter for the field <code>cmsVersion2</code>.</p>
	 *
	 * @param cmsVersion2Generator a boolean.
	 */
	public void setCmsVersion2(boolean cmsVersion2Generator) {
		this.cmsVersion2 = cmsVersion2Generator;
	}

}
