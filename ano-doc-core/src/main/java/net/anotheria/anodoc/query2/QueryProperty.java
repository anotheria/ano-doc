package net.anotheria.anodoc.query2;

import java.io.Serializable;

/**
 * <p>QueryProperty class.</p>
 *
 * @author another
 * @version $Id: $Id
 */
public class QueryProperty implements Serializable{
	/**
	 * svid.
	 */
	private static final long serialVersionUID = 2030752289719048811L;
	/**
	 * Name of the property.
	 */
	private String name;
	/**
	 * Value of the property.
	 */
	private Object value;
	private boolean unprepaireable;
	
	/**
	 * <p>Constructor for QueryProperty.</p>
	 *
	 * @param aName a {@link java.lang.String} object.
	 * @param aValue a {@link java.lang.Object} object.
	 */
	public QueryProperty(String aName, Object aValue){
		this(aName, aValue, false);
	}
	
	/**
	 * <p>Constructor for QueryProperty.</p>
	 *
	 * @param aName a {@link java.lang.String} object.
	 * @param aValue a {@link java.lang.Object} object.
	 * @param anUnprepaireable a boolean.
	 */
	public QueryProperty(String aName, Object aValue, boolean anUnprepaireable){
		name = aName;
		value = aValue;
		unprepaireable = anUnprepaireable;
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
	 * @return a {@link java.lang.Object} object.
	 */
	public Object getValue() {
		return value;
	}
	/**
	 * <p>Setter for the field <code>value</code>.</p>
	 *
	 * @param value a {@link java.lang.Object} object.
	 */
	public void setValue(Object value) {
		this.value = value;
	}
	
	/** {@inheritDoc} */
	@Override public String toString(){
		return getName() + "=" +getValue();
	}
	
	/**
	 * Returns the comparator operation for this query.
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getComparator(){
		return " = ";
	}
	
	/**
	 * <p>doesMatch.</p>
	 *
	 * @param o a {@link java.lang.Object} object.
	 * @return a boolean.
	 */
	public boolean doesMatch(Object o){
		return o== null ? value == null :
			o.equals(value);
	}
	
	/**
	 * <p>unprepaireable.</p>
	 *
	 * @return a boolean.
	 */
	public boolean unprepaireable(){
		return unprepaireable;
	}
	
	/**
	 * <p>getOriginalValue.</p>
	 *
	 * @return a {@link java.lang.Object} object.
	 */
	protected Object getOriginalValue(){
		return value;
	}
	
}
