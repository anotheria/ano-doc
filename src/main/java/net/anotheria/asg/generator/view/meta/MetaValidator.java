package net.anotheria.asg.generator.view.meta;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>MetaValidator class.</p>
 *
 * @author another
 * @version $Id: $Id
 */
public class MetaValidator implements Cloneable {
	
	/** Array containing classNames of validators that have custom handling in ano-maf*/
	private static List<String> predefinedMafValidators = new ArrayList<String>();
	static {
		predefinedMafValidators.add("net.anotheria.maf.validation.annotations.ValidateNotEmpty");
		predefinedMafValidators.add("net.anotheria.maf.validation.annotations.ValidateNumber");
	}
	/**
	 * The name of a validator.
	 */
	private String name;
	/**
	 * The class name of the validator.
	 */
	private String className;
	/**
	 * The key to look error text under.
	 */
	private String key;
	/**
	 * The default error message if localized is not present.
	 */
	private String defaultError;
	/**
	 * JS code (optional) that can validate value on client-side.
	 */
	private String jsValidation;

	/**
	 * Creates a new metavalidator.
	 */
	public MetaValidator(){
		
	}
	/**
	 * Creates a new meta metavalidator.
	 *
	 * @param aName a {@link java.lang.String} object.
	 * @param aClassName a {@link java.lang.String} object.
	 */
	public MetaValidator(String aName, String aClassName){
		name = aName;
		className = aClassName;
	}
	
	/**
	 * <p>Getter for the field <code>name</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * <p>Setter for the field <code>name</code>.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * <p>Getter for the field <code>className</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getClassName() {
		return className;
	}
	
	/**
	 * <p>Setter for the field <code>className</code>.</p>
	 *
	 * @param className a {@link java.lang.String} object.
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	
	/**
	 * <p>Getter for the field <code>key</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getKey() {
		return key;
	}
	
	/**
	 * <p>Setter for the field <code>key</code>.</p>
	 *
	 * @param key a {@link java.lang.String} object.
	 */
	public void setKey(String key) {
		this.key = key;
	}
	
	/**
	 * <p>Getter for the field <code>defaultError</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getDefaultError() {
		return defaultError;
	}
	
	/**
	 * <p>Setter for the field <code>defaultError</code>.</p>
	 *
	 * @param defaultError a {@link java.lang.String} object.
	 */
	public void setDefaultError(String defaultError) {
		this.defaultError = defaultError;
	}
	
	/** {@inheritDoc} */
	@Override public Object clone(){
		try{
			return super.clone();
		}catch(Exception e){
			throw new AssertionError("Can't happen.");
		}
		
	}

	/** {@inheritDoc} */
	@Override public String toString(){
		return name+" = "+className;
	}
	
	/** {@inheritDoc} */
	@Override public boolean equals(Object o){
		return o instanceof MetaValidator ?
			((MetaValidator)o).getName().equals(name) : false; 
	}
	
	/** {@inheritDoc} */
	@Override public int hashCode(){
		return name == null ? 0 : name.hashCode();
	}
	
	/**
	 * Returns the name of the class of the metaValidator without a package.
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getClassNameOnly(){
		if (className.lastIndexOf('.')==-1)
			return className;
		return className.substring(className.lastIndexOf('.')+1);
	}
	
	/**
	 * Returns true if form field should get @ValidateCustom() annotation, e.g. custom project validator
	 *
	 * @return a boolean.
	 */
	public boolean isCustomValidator() {
		return !predefinedMafValidators.contains(className);
	}
	/**
	 * <p>isNumericValidator.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isNumericValidator() {
		return className.equals("net.anotheria.maf.validation.annotations.ValidateNumber");
	}
	/**
	 * <p>Setter for the field <code>jsValidation</code>.</p>
	 *
	 * @param jsValidation a {@link java.lang.String} object.
	 */
	public void setJsValidation(String jsValidation) {
		this.jsValidation = jsValidation;
	}
	/**
	 * <p>Getter for the field <code>jsValidation</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getJsValidation() {
		return jsValidation;
	}
	
}
