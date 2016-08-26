package net.anotheria.anodoc.data;

/**
 * This property represents a long value.
 *
 * @since 1.0
 * @author another
 * @version $Id: $Id
 */
public class LongProperty extends Property{
	
	/**
	 * svid.
	 */
	private static final long serialVersionUID = 2177663057004436401L;
	
	/**
	 * Creates a new LongProperty with given name and value.
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a long.
	 */
	public LongProperty(String name, long value){
		this(name, Long.valueOf(value));
	}
	
	/**
	 * Creates a new LongProperty with given name and value.
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a {@link java.lang.Long} object.
	 */
	public LongProperty(String name, Long value){
		super(name, value);
	}
	
	/**
	 * Returns the value of this property as Long object.
	 *
	 * @return a {@link java.lang.Long} object.
	 */
	public Long getLong(){
		return (Long)getValue();
	}
	
	/**
	 * Returns the value of this property as long.
	 *
	 * @return a long.
	 */
	public long getlong(){
		return getLong().longValue();
	}

	/**
	 * Returns the value of this property as long.
	 *
	 * @return a long.
	 */
	public long longValue(){
		return getLong().longValue();
	}
	
	/**
	 * Sets the value of this property to the given value.
	 *
	 * @param aValue a long.
	 */
	public void setLong(long aValue){
		setLong(Long.valueOf(aValue));
	}
	
	/**
	 * Sets the value of this property to the given Long object.
	 *
	 * @param aValue a {@link java.lang.Long} object.
	 */
	public void setLong(Long aValue){
		super.setValue(aValue);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * Sets the value of this property to the given value.
	 */
	public void setValue(Object o){
		if (o instanceof Long){
			super.setValue(o);
			return;
		}
		if (o instanceof String){
			try{
				super.setValue(Long.valueOf((String)o));
				return;
			}catch(NumberFormatException nfe){
			}
		}
		throw new RuntimeException(o+" is not a legal value for LongProperty"); 
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * Returns the size in bytes (8bytes = 64 bit datatype).
	 * @see net.anotheria.anodoc.data.Property#getDataSize()
	 */
	@Override public long getSizeInBytes() {
		return 8;
	}
	
	/* (non-Javadoc)
	 * @see net.anotheria.anodoc.data.Property#cloneValue()
	 */
	/** {@inheritDoc} */
	@Override protected Object cloneValue() {
		return new Long(getlong());
	}

	/** {@inheritDoc} */
	@Override public PropertyType getPropertyType(){
		return PropertyType.LONG;
	}

}
