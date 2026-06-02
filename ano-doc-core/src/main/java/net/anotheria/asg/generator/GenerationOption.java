package net.anotheria.asg.generator;

/**
 * An option used by the generator to enable or disable some generation features.
 * For example generation of rmi or inmemory services is triggered by generation options.
 *
 * @author another
 * @version $Id: $Id
 */
public class GenerationOption {
	/**
	 * Name of the option.
	 */
	private String name;
	/**
	 * Value of the option.
	 */
	private String value;
	/**
	 * Creates a new GenerationOption.
	 *
	 * @param aName a {@link java.lang.String} object.
	 * @param aValue a {@link java.lang.String} object.
	 */
	public GenerationOption(String aName, String aValue){
		name = aName;
		value = aValue;
	}
	/**
	 * Creates a new GenerationOption with empty name and value.
	 */
	public GenerationOption(){
		this("","");
	}
	
	/** {@inheritDoc} */
	@Override public String toString(){
		return name+" = "+value;
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
	
	/**
	 * Returns true if the options value is equal to 'true'.
	 *
	 * @return a boolean.
	 */
	public boolean isTrue(){
		return value!=null && value.equalsIgnoreCase("true");
	}
	/**
	 * Returns isSet &amp;&amp; !true.
	 *
	 * @return a boolean.
	 */
	public boolean isFalse(){
		return value!=null && !(value.equalsIgnoreCase("true"));
	}
	/**
	 * Returns true if a value is set.
	 *
	 * @return a boolean.
	 */
	public boolean isSet(){
		return value!=null;
	}

}
