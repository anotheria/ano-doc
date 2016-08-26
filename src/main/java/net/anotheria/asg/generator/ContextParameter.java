package net.anotheria.asg.generator;

/**
 * Represents a free parameter attachable to the context.
 *
 * @author another
 * @version $Id: $Id
 */
public class ContextParameter {
	/**
	 * Name of the parameter.
	 */
	private String name;
	/**
	 * Value of the parameter.
	 */
	private String value;
	
	/**
	 * Constant for cms versioning parameter.
	 */
	public static final String CTX_PARAM_CMS_VERSIONING = "cmsversioning";
	
	/**
	 * <p>Constructor for ContextParameter.</p>
	 */
	public ContextParameter(){
		
	}
	
	/**
	 * <p>Constructor for ContextParameter.</p>
	 *
	 * @param aName a {@link java.lang.String} object.
	 * @param aValue a {@link java.lang.String} object.
	 */
	public ContextParameter(String aName, String aValue){
		name = aName;
		value = aValue;
	}
	
	/** {@inheritDoc} */
	@Override public String toString(){
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
	 * @param aName a {@link java.lang.String} object.
	 */
	public void setName(String aName) {
		name = aName;
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
	 * @param aValue a {@link java.lang.String} object.
	 */
	public void setValue(String aValue) {
		value = aValue;
	}
}
