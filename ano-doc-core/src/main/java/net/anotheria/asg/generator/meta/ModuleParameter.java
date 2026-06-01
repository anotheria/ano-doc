package net.anotheria.asg.generator.meta;

/**
 * <p>ModuleParameter class.</p>
 *
 * @author another
 * @version $Id: $Id
 */
public class ModuleParameter {
	private String name;
	private String value;
	
	/** Constant <code>MODULE_DB_CONTEXT_SENSITIVE="db_context_sensitive"</code> */
	public static final String MODULE_DB_CONTEXT_SENSITIVE = "db_context_sensitive";
	
	/**
	 * <p>Constructor for ModuleParameter.</p>
	 */
	public ModuleParameter(){
		
	}
	
	/**
	 * <p>Constructor for ModuleParameter.</p>
	 *
	 * @param aName a {@link java.lang.String} object.
	 * @param aValue a {@link java.lang.String} object.
	 */
	public ModuleParameter(String aName, String aValue){
		name = aName;
		value = aValue;
	}
	
	/**
	 * <p>toString.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toString(){
		return "Parameter "+name+" = "+value;
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
	 * <p>Getter for the field <code>value</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * <p>Setter for the field <code>value</code>.</p>
	 *
	 * @param value a {@link java.lang.String} object.
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
