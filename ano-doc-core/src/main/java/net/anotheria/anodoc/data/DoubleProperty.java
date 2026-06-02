package net.anotheria.anodoc.data;

/**
 * This class represents a double property (the mapping for double attributes).
 *
 * @since 1.1
 * @author another
 * @version $Id: $Id
 */
public class DoubleProperty extends Property{
	/**
	 * svid.
	 */
	private static final long serialVersionUID = 5268122271480317179L;
	
	/**
	 * Creates a new DoubleProperty with given name and value.
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a double.
	 */
	public DoubleProperty(String name, double value){
		this(name, new Double(value));
	}
	
	/**
	 * Creates a new DoubleProperty with given name and value.
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a {@link java.lang.Double} object.
	 */
	public DoubleProperty(String name, Double value){
		super(name, value);
	}

	/**
	 * <p>getDouble.</p>
	 *
	 * @return the value of this property as Float object.
	 */
	public Double getDouble(){
		return (Double)getValue();
	}
	
	/**
	 * <p>getdouble.</p>
	 *
	 * @return the value of this property as float.
	 */
	public double getdouble(){
		return getDouble().doubleValue();
	}
	
	/**
	 * Sets the value of this property to given float value.
	 *
	 * @param aValue a double.
	 */
	public void setFloat(double aValue){
		setDouble(new Double(aValue));
	}
	
	/**
	 * Sets the value of this property to given Float value.
	 *
	 * @param aValue a {@link java.lang.Double} object.
	 */
	public void setDouble(Double aValue){
		super.setValue(aValue);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * Sets the value of this property to given value, whether the value
	 * can be a Float or a String.
	 */
	@Override public void setValue(Object o){
		if (o instanceof Double){
			super.setValue(o);
			return;
		}
		if (o instanceof String){
			try{
				super.setValue(new Double( (String)o));
				return;
			}catch(NumberFormatException nfe){
			}
		}
		throw new RuntimeException(o+" is not a legal value for DoubleProperty"); 
	}

	/** {@inheritDoc} */
	@Override public long getSizeInBytes() {
		return 16;
	}
	
	/* (non-Javadoc)
	 * @see net.anotheria.anodoc.data.Property#cloneValue()
	 */
	/** {@inheritDoc} */
	@Override protected Object cloneValue() {
		return new Double(getdouble());
	}
	 
	/** {@inheritDoc} */
	@Override public PropertyType getPropertyType(){
		return PropertyType.DOUBLE;
	}
}


