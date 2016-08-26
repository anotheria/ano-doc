package net.anotheria.anodoc.data;

/**
 * This property represents a string value.
 *
 * @author another
 * @version $Id: $Id
 */
public class StringProperty extends Property{
	/**
	 * svid.
	 */
	private static final long serialVersionUID = -7506812728258106500L;
	
	/**
	 * Creates a new StringProperty with given name and null value.
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public StringProperty(String name){
		this(name, null);
	}
	
	/**
	 * Creates a new StringProperty with given name and value.
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a {@link java.lang.String} object.
	 */
	public StringProperty(String name, String value){
		super(name, value);
	}
	
	/**
	 * Returns the value of this property as String.
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getString(){
		return (String) getValue();
	}
	
	/**
	 * Sets the value of this property to the given String.
	 *
	 * @param aString a {@link java.lang.String} object.
	 */
	public void setString(String aString){
		setValue(aString);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * Sets the value of this property to the String representation
	 * of the given Object o. If o is null, the value of this property will
	 * be 'null'.
	 */
	@Override public void setValue(Object o){
		super.setValue(""+o);			
	}

	/**
	 * {@inheritDoc}
	 *
	 * Returns true if the given obj is a StringProperty and the name, value tuples are equal.
	 */
	@Override public boolean equals(Object obj) {
		if(obj instanceof StringProperty){
			StringProperty p = (StringProperty)obj;
			return p.getName().equals(getName()) && p.getValue().equals(getValue());
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 *
	 * Returns the amount of bytes needed to save this property.
	 * @see net.anotheria.anodoc.data.Property#getSizeInBytes()
	 */
	@Override public long getSizeInBytes() { 
		String s = getString();
		return s==null ? 0 : s.length()*2;
	}
	
	/** {@inheritDoc} */
	@Override protected Object cloneValue() {
		return getString() != null? new String(getString()): null;
	}
	
	/** {@inheritDoc} */
	@Override public PropertyType getPropertyType(){
		return PropertyType.STRING;
	}

	

}
