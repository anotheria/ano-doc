package net.anotheria.anodoc.data;

/**
 * This class represents a boolean property.
 *
 * @since 1.0
 * @author lrosenberg
 * @version $Id: $Id
 */
public class BooleanProperty extends Property{
	/**
	 * svid.
	 */
	private static final long serialVersionUID = -6112656517280319094L;
	
	/**
	 * Creates a new BooleanProperty with given name and value.
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a boolean.
	 */
	public BooleanProperty(String name, boolean value){
		this(name, Boolean.valueOf(value));
	}
	
	/**
	 * Creates a new BooleanProperty with given name and value.
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a {@link java.lang.Boolean} object.
	 */
	public BooleanProperty(String name, Boolean value){
		super(name, value);
	}
	
	/**
	 * Returns the value of this Property as a Boolean object.
	 *
	 * @return a {@link java.lang.Boolean} object.
	 */
	public Boolean getBoolean(){
		return (Boolean)getValue();
	}
	
	/**
	 * Returns the value of this Property as boolean data (primary type).
	 *
	 * @return a boolean.
	 */
	public boolean getboolean(){
		return getBoolean().booleanValue();
	}
	
	/**
	 * Sets the value of this property to the given boolean value.
	 *
	 * @param aValue a boolean.
	 */
	public void setboolean(boolean aValue){
		setBoolean(Boolean.valueOf(aValue));
	}
	
	/**
	 * Sets the value of this property to the given Boolean value.
	 *
	 * @param aValue a {@link java.lang.Boolean} object.
	 */
	public void setBoolean(Boolean aValue){
		super.setValue(aValue);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * Sets the value of this property to the given value, which can be a Boolean or a String.
	 */
	@Override public void setValue(Object o){
		if (o instanceof Boolean){
			super.setValue(o);
			return;
		}
		if (o instanceof String){
			try{
				super.setValue(Boolean.valueOf((String)o));
				return;
			}catch(NumberFormatException nfe){
			}
		}
		throw new RuntimeException(o+" is not a legal value for BooleanProperty"); 
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * Returns the size of this property in bytes (one byte).
	 * @see net.anotheria.anodoc.data.Property#getSizeInBytes()
	 */
	@Override public long getSizeInBytes() {
		return 1;
	}
	
	/** {@inheritDoc} */
	@Override protected Object cloneValue() {
		return getboolean() ? Boolean.TRUE : Boolean.FALSE;
	}
	
	/** {@inheritDoc} */
	@Override public PropertyType getPropertyType(){
		return PropertyType.BOOLEAN;
	}

}
