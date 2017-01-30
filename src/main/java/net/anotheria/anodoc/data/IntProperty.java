package net.anotheria.anodoc.data;

/**
 * This class represents an integer property (the mapping for int or integer attributes)
 *
 * @since 1.0
 * @author another
 * @version $Id: $Id
 */
public class IntProperty extends Property{
	
	/**
	 * svid.
	 */
	private static final long serialVersionUID = -3202712780342418714L;
	
	/**
	 * Creates a new IntProperty with given name and value.
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a int.
	 */
	public IntProperty(String name, int value){
		this(name, Integer.valueOf(value));
	}
	
	/**
	 * Creates a new IntProperty with given name and value.
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a {@link java.lang.Integer} object.
	 */
	public IntProperty(String name, Integer value){
		super(name, value);
	}
	
	/**
	 * Returns the value of this property as Integer object.
	 *
	 * @return a {@link java.lang.Integer} object.
	 */
	public Integer getInteger(){
		return (Integer)getValue();
	}
	
	/**
	 * Returns the value of this property as int.
	 *
	 * @return a int.
	 */
	public int getInt(){
		return getInteger().intValue();
	}
	
	/**
	 * Sets the value of this property to the given int value.
	 *
	 * @param aValue a int.
	 */
	public void setInt(int aValue){
		setInteger(Integer.valueOf(aValue));
	}
	
	/**
	 * Sets the value of this property to the given Integer value.
	 *
	 * @param aValue a {@link java.lang.Integer} object.
	 */
	public void setInteger(Integer aValue){
		super.setValue(aValue);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * Sets the value of this property to the value of the given object.
	 * o can be an Integer or a String representing an integer.
	 */
	@Override public void setValue(Object o){
		if (o instanceof Integer){
			super.setValue(o);
			return;
		}
		if (o instanceof String){
			try{
				super.setValue(Integer.valueOf((String)o));
				return;
			}catch(NumberFormatException nfe){
			}
		}
		throw new RuntimeException(o+" is not a legal value for IntProperty"); 
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * Returns the amount of bytes needed to hold an integer value - 4.
	 */
	@Override public long getSizeInBytes() {
		return 4;
	}

	/* (non-Javadoc)
	 * @see net.anotheria.anodoc.data.DataHolder#cloneValue()
	 */
	/** {@inheritDoc} */
	@Override protected Object cloneValue() {
		return new Integer(getInt());
	}
 
	/** {@inheritDoc} */
	@Override public PropertyType getPropertyType(){
		return PropertyType.INT;
	}

}
