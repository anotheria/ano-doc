package net.anotheria.anodoc.data;

/**
 * This class represents a float property (the mapping for float attributes)
 *
 * @since 1.0
 * @author another
 * @version $Id: $Id
 */
public class FloatProperty extends Property{
	
	/**
	 * svid.
	 */
	private static final long serialVersionUID = -8097948895367514824L;

	/**
	 * Creates a new FloatProperty with given name and value.
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a float.
	 */
	public FloatProperty(String name, float value){
		this(name, new Float(value));
	}
	
	/**
	 * Creates a new FloatProperty with given name and value.
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a {@link java.lang.Float} object.
	 */
	public FloatProperty(String name, Float value){
		super(name, value);
	}

	/**
	 * Returns the value of this property as Float object.
	 *
	 * @return a {@link java.lang.Float} object.
	 */
	public Float getFloat(){
		return (Float)getValue();
	}
	
	/**
	 * Returns the value of this property as float.
	 *
	 * @return a float.
	 */
	public float getfloat(){
		return getFloat().floatValue();
	}
	
	/**
	 * Sets the value of this property to given float value.
	 *
	 * @param aValue a float.
	 */
	public void setFloat(float aValue){
		setFloat(new Float(aValue));
	}
	
	/**
	 * Sets the value of this property to given Float value.
	 *
	 * @param aValue a {@link java.lang.Float} object.
	 */
	public void setFloat(Float aValue){
		super.setValue(aValue);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * Sets the value of this property to given value, whether the value
	 * can be a Float or a String.
	 */
	@Override public void setValue(Object o){
		if (o instanceof Float){
			super.setValue(o);
			return;
		}
		if (o instanceof String){
			try{
				super.setValue(new Float( (String)o));
				return;
			}catch(NumberFormatException nfe){
			}
		}
		throw new RuntimeException(o+" is not a legal value for FloatProperty"); 
	}

	/**
	 * {@inheritDoc}
	 *
	 * Returns the size needed to hold a float value in bytes (8).
	 * @see net.anotheria.anodoc.data.Property#getSizeInBytes()
	 */
	@Override public long getSizeInBytes() {
		return 8;
	}
	
	/* (non-Javadoc)
	 * @see net.anotheria.anodoc.data.Property#cloneValue()
	 */
	/** {@inheritDoc} */
	@Override protected Object cloneValue() {
		return new Float(getfloat());
	}

	/** {@inheritDoc} */
	@Override public PropertyType getPropertyType(){
		return PropertyType.FLOAT;
	}

}
