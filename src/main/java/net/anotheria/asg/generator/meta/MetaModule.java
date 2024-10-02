package net.anotheria.asg.generator.meta;

import net.anotheria.asg.generator.GenerationOptions;
import net.anotheria.asg.generator.GeneratorDataRegistry;
import net.anotheria.asg.generator.IGenerateable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Representation of a module definition.
 *
 * @author another
 * @version $Id: $Id
 */
public class MetaModule implements IGenerateable{

	/**
	 * This is a predefined module used to generate package and class names for shared stuff.
	 */
	public static final MetaModule SHARED = new MetaModule("Shared");
	
	/**
	 * This is a predefined module used to generate package and class names for user settings stuff.
	 */
	public static final MetaModule USER_SETTINGS = new MetaModule("UserSettings");
	

	/**
	 * Unique name of the module. Each module has a name which is used whenever someone refers to it.
	 */
	private String name;
	/**
	 * List of the documents in this module.
	 */
	private List<MetaDocument> documents;
	/**
	 * Module listeners which can be attached to the generated service.
	 */
	private List<String> listeners;
	/**
	 * Type of the storge for this module.
	 */
	private StorageType storageType;
	
	private Map<String, ModuleParameter> parameters;
	
	/**
	 * Generation options which can enable or disable generation of some artefacts.
	 */
	private GenerationOptions moduleOptions;
		 
	/**
	 * Creates a new empty module.
	 */
	public MetaModule(){
		this(null);
	}
	
	/**
	 * Creates a new module with the given name.
	 *
	 * @param name name of the module.
	 */
	public MetaModule(String name){
		this.name = name;
		documents = new ArrayList<MetaDocument>();
		listeners = new ArrayList<String>();
		storageType = StorageType.CMS;
		parameters = new HashMap<String, ModuleParameter>();
	}
	
	/**
	 * Adds a document definition to the module.
	 *
	 * @param aDocument a {@link net.anotheria.asg.generator.meta.MetaDocument} object.
	 */
	public void addDocument(MetaDocument aDocument){
		documents.add(aDocument);
		aDocument.setParentModule(this);
	}
	
	/**
	 * Returns true if an option is enabled. For example 'rmi' is an option which can be enabled.
	 *
	 * @param key a {@link java.lang.String} object.
	 * @return true if an option is enabled
	 */
	public boolean isEnabledByOptions(String key){
		if (moduleOptions!=null){
			if (moduleOptions.isEnabled(key))
				return true;
		}
		
		return GeneratorDataRegistry.getInstance().getOptions().isEnabled(key);
		
	}
	
	/** {@inheritDoc} */
	@Override public String toString(){
		return "module "+name+" storage: "+storageType+" documents: "+documents;
	}
	/**
	 * <p>Getter for the field <code>documents</code>.</p>
	 *
	 * @return contained documents
	 */
	public List<MetaDocument> getDocuments() {
		return documents;
	}

	/**
	 * <p>Getter for the field <code>name</code>.</p>
	 *
	 * @return name of the module
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * <p>getModuleClassName.</p>
	 *
	 * @return the name for the module implementation class in the cms storage
	 */
	public String getModuleClassName(){
		return "Module"+getName();
	}
	
	/**
	 * <p>getFactoryClassName.</p>
	 *
	 * @return the class name of the generated module factory
	 */
	public String getFactoryClassName(){
		return getModuleClassName()+"Factory";
	}

	/**
	 * <p>Setter for the field <code>documents</code>.</p>
	 *
	 * @param list a {@link java.util.List} object.
	 */
	public void setDocuments(List<MetaDocument> list) {
		documents = list;
	}

	/**
	 * <p>Setter for the field <code>name</code>.</p>
	 *
	 * @param string a {@link java.lang.String} object.
	 */
	public void setName(String string) {
		name = string;
	}
	
	/**
	 * Returns the id of the module. Id is basically name.toLowerCase().
	 *
	 * @return the id of the module
	 */
	public String getId(){
		return getName().toLowerCase();
	}

	/**
	 * <p>getDocumentByName.</p>
	 *
	 * @param aName a {@link java.lang.String} object.
	 * @return a {@link net.anotheria.asg.generator.meta.MetaDocument} object.
	 */
	public MetaDocument getDocumentByName(String aName){
	    for (int i=0; i<documents.size(); i++){
	        MetaDocument d = documents.get(i);
	        if (d.getName().equals(aName))
	            return d;
	    }
	    throw new RuntimeException("No such document: "+aName + " in module "+getName());
	}

	/** {@inheritDoc} */
	@Override public boolean equals(Object o){
		return o instanceof MetaModule ? 
			((MetaModule)o).name.equals(name) : false;
	}
	
	/** {@inheritDoc} */
	@Override public int hashCode(){
		return name == null ? 42 : name.hashCode();
	}

	/**
	 * <p>Getter for the field <code>listeners</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<String> getListeners() {
		return listeners;
	}

	/**
	 * <p>Setter for the field <code>listeners</code>.</p>
	 *
	 * @param listeners a {@link java.util.List} object.
	 */
	public void setListeners(List<String> listeners) {
		this.listeners = listeners;
	}
	
	/**
	 * <p>addListener.</p>
	 *
	 * @param listenerClass a {@link java.lang.String} object.
	 */
	public void addListener(String listenerClass){
		listeners.add(listenerClass);
	}
	
	/**
	 * <p>removeListener.</p>
	 *
	 * @param listenerClass a {@link java.lang.String} object.
	 */
	public void removeListener(String listenerClass){
		listeners.remove(listenerClass);
	}

	/**
	 * <p>Getter for the field <code>storageType</code>.</p>
	 *
	 * @return a {@link net.anotheria.asg.generator.meta.StorageType} object.
	 */
	public StorageType getStorageType() {
		return storageType;
	}

	/**
	 * <p>Setter for the field <code>storageType</code>.</p>
	 *
	 * @param aStorageType a {@link net.anotheria.asg.generator.meta.StorageType} object.
	 */
	public void setStorageType(StorageType aStorageType) {
		storageType = aStorageType;
	}
	
	/**
	 * <p>addModuleParameter.</p>
	 *
	 * @param p a {@link net.anotheria.asg.generator.meta.ModuleParameter} object.
	 */
	public void addModuleParameter(ModuleParameter p){
		parameters.put(p.getName(), p);
	}
	
	/**
	 * <p>getModuleParameter.</p>
	 *
	 * @param aName a {@link java.lang.String} object.
	 * @return a {@link net.anotheria.asg.generator.meta.ModuleParameter} object.
	 */
	public ModuleParameter getModuleParameter(String aName){
		return parameters.get(aName);
	}
	
	/**
	 * <p>isParameterEqual.</p>
	 *
	 * @param aName a {@link java.lang.String} object.
	 * @param aValue a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public boolean isParameterEqual(String aName, String aValue){
		ModuleParameter p = getModuleParameter(aName);
		return p == null ? false : p.getValue().equals(aValue);
	}

	/**
	 * <p>Getter for the field <code>moduleOptions</code>.</p>
	 *
	 * @return a {@link net.anotheria.asg.generator.GenerationOptions} object.
	 */
	public GenerationOptions getModuleOptions() {
		return moduleOptions;
	}

	/**
	 * <p>Setter for the field <code>moduleOptions</code>.</p>
	 *
	 * @param someModuleOptions a {@link net.anotheria.asg.generator.GenerationOptions} object.
	 */
	public void setModuleOptions(GenerationOptions someModuleOptions) {
		moduleOptions = someModuleOptions;
	}
	
	/**
	 * <p>isContainsAnyMultilingualDocs.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isContainsAnyMultilingualDocs(){
		for(MetaDocument doc:documents)
			if (GeneratorDataRegistry.hasLanguageCopyMethods(doc))
				return true;
		
		return false; 
	}
}
