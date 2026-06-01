package net.anotheria.asg.generator.meta;

/**
 * A property of enumeration type.
 *
 * @author another
 * @version $Id: $Id
 */
public class MetaEnumerationProperty extends MetaProperty{
	
	/**
	 * Name of the enumeration.
	 */
	private String enumeration;
	
	/**
	 * Creates a new enumeration property.
	 *
	 * @param aName a {@link java.lang.String} object.
	 * @param aType a {@link net.anotheria.asg.generator.meta.MetaProperty.Type} object.
	 */
	public MetaEnumerationProperty(String aName, MetaProperty.Type aType){
		super(aName, aType);
	}

	/**
	 * <p>Getter for the field <code>enumeration</code>.</p>
	 *
	 * @return name of the enumeration
	 */
	public String getEnumeration() {
		return enumeration;
	}

	/**
	 * <p>Setter for the field <code>enumeration</code>.</p>
	 *
	 * @param string name of enumeration
	 */
	public void setEnumeration(String string) {
		enumeration = string;
	}

}
