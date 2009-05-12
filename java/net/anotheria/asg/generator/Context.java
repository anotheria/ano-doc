package net.anotheria.asg.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.anotheria.asg.generator.meta.MetaDocument;
import net.anotheria.asg.generator.meta.MetaModule;

/**
 * The Context is available at the generation time and contains contents of the context.xml.
 * @author another
 */
public class Context {
	private String packageName;
	private String owner;
	private String applicationName;
	private String servletMapping;
	private String encoding;
	/**
	 * Part of application url path between / and /, for example if you want the cms to be accessible under /xyz/cms/pagexShow the application url path is xyz. 
	 */
	private String applicationURLPath;
	
	private boolean multilanguageSupport;
	
	private List<String> languages;
	private String defaultLanguage;
	
	private GenerationOptions options;
	
	private Map<String, ContextParameter> parameters;
	
	public Context(){
		parameters = new HashMap<String, ContextParameter>();
	}
	
	/**
	 * @deprecated use getPackageName(MetaModule m);
	 * @return
	 */
	public String getPackageName() {
		return packageName;
	}
	
	public String getTopPackageName(){
		return packageName;
	}
	
	public String getJspPackageName(MetaDocument doc){
		return getJspPackageName(doc.getParentModule());
	}
	
	public String getJspPackageName(MetaModule module){
		return getPackageName(module)+".jsp";
	}

	public String getDataPackageName(MetaDocument doc){
		return getDataPackageName(doc.getParentModule());
	}
	
	public String getDataPackageName(MetaModule module){
		return getPackageName(module)+".data";
	}

	public String getServicePackageName(MetaDocument doc){
		return getServicePackageName(doc.getParentModule());
	}
	
	public String getServicePackageName(MetaModule module){
		return getPackageName(module)+".service";
	}
	
	public String getPackageName(MetaModule module){
		return packageName+"."+module.getName().toLowerCase();
	}
	
	public String getPackageName(MetaDocument doc){
		return getPackageName(doc.getParentModule());
	}

	/**
	 * @param string
	 */
	public void setPackageName(String string) {
		packageName = string;
	}

    /**
     * @return Returns the owner.
     */
    public String getOwner() {
        return owner;
    }
    /**
     * @param owner The owner to set.
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }
	/**
	 * @return
	 */
	public String getApplicationName() {
		return applicationName;
	}

	/**
	 * @param string
	 */
	public void setApplicationName(String string) {
		applicationName = string;
	}

	/**
	 * @return
	 */
	public String getServletMapping() {
		return servletMapping;
	}

	/**
	 * @param string
	 */
	public void setServletMapping(String string) {
		servletMapping = string;
	}

	/**
	 * @return
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @param string
	 */
	public void setEncoding(String string) {
		encoding = string;
	}

	public String getDefaultLanguage() {
		return defaultLanguage;
	}

	public void setDefaultLanguage(String defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
	}

	public List<String> getLanguages() {
		return languages;
	}

	public void setLanguages(List<String> languages) {
		this.languages = languages;
	}
	
	public boolean areLanguagesSupported(){
		return multilanguageSupport;
	}
	
	public void enableMultiLanguageSupport(){
		multilanguageSupport = true;
		languages = new ArrayList<String>();
	}
	
	public void addLanguage(String l){
		languages.add(l);
	}
	
	public void addContextParameter(ContextParameter p){
		parameters.put(p.getName(), p);
	}
	
	public void addContextParameter(String name, String value){
		addContextParameter(new ContextParameter(name, value));
	}
	
	public List<ContextParameter> getContextParameters(){
		ArrayList<ContextParameter> ret = new ArrayList<ContextParameter>();
		ret.addAll(parameters.values());
		return ret;
	}
	
	public ContextParameter getContextParameter(String name){
		return parameters.get(name);
	}

	public String getApplicationURLPath() {
		return applicationURLPath == null || applicationURLPath.length() == 0? "" : "/" + applicationURLPath;
	}

	public void setApplicationURLPath(String applicationURLPath) {
		this.applicationURLPath = applicationURLPath;
	}

	public GenerationOptions getOptions() {
		return options;
	}

	public void setOptions(GenerationOptions options) {
		this.options = options;
	}

}
