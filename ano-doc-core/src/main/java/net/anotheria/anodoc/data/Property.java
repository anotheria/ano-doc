package net.anotheria.anodoc.data;

import net.anotheria.util.xml.XMLAttribute;
import net.anotheria.util.xml.XMLNode;

/**
 * This class represents a single data entry which can be stored.
 * It is of a plain kind (can't contain any subproperties, except
 * the ListProperty).
 *
 * @author lro
 * @since 1.0
 * @version $Id: $Id
 */
public abstract class Property
	 extends DataHolder implements IPlainDataObject,Cloneable{
	 
	/**
	 * svid.
	 */
	private static final long serialVersionUID = -4170023770710073469L;

	/**
	 * The saveable value of the object.
	 */
	private Object value;
	
	/**
	 * Creates new Property without a value (null).
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	protected Property(String name){
		this(name, null);
	}
	
	/**
	 * Creates a new Property with a given name and given value.
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param aValue a {@link java.lang.Object} object.
	 */
	protected Property(String name, Object aValue){
		super(name);
		value = aValue;	
	}

	/**
	 * <p>Getter for the field <code>value</code>.</p>
	 *
	 * @return the value of this property.
	 */
	public Object getValue(){
		return value;
	
	}
	
	/**
	 * Sets the value of this Property.
	 *
	 * @param o object to set
	 */
	public void setValue(Object o){
		this.value = o;
	}
	
	/** {@inheritDoc} */
	@Override public String toString(){
		return getPropertyType().getIndicator()+getName()+"="+value;
	}
	
	/**
	 * Since the can't be two equally named properties in a Document,
	 * the storageId of a Property is its name.
	 *
	 * @see net.anotheria.anodoc.data.IBasicStoreableObject#getStorageId()
	 * @return a {@link java.lang.String} object.
	 */
	public String getStorageId() {
		return getName();
	}
	
	/** {@inheritDoc} */
	@Override public boolean equals(Object o){
		if (!(o instanceof Property))
			return false;
		Property anotherProperty = (Property)o;
		return getName().equals(anotherProperty.getName()) && 
			   value.equals(anotherProperty.value); 
	}
	
	/**
	 * <p>getName.</p>
	 *
	 * @return the name of the property
	 */
	protected String getName(){
		return getId();
	}
	
	/** {@inheritDoc} */
	@Override public Object clone() throws CloneNotSupportedException{
		Property newP = (Property)super.clone();
		newP.setValue(cloneValue());
		return newP;
	}
	/**
	 * Creates a copy of this property with a new name.
	 *
	 * @param newName a {@link java.lang.String} object.
	 * @return created property with new name
	 * @throws java.lang.CloneNotSupportedException if any.
	 */
	public Property cloneAs(String newName) throws CloneNotSupportedException{
		Property newP = (Property)super.clone();
		newP.setValue(cloneValue());
		newP.setId(newName);
		return newP;
	}
	
	/**
	 * <p>cloneValue.</p>
	 *
	 * @return a {@link java.lang.Object} object.
	 * @throws java.lang.CloneNotSupportedException if any.
	 */
	protected abstract Object cloneValue() throws CloneNotSupportedException;
	
	/**
	 * Creates an xml node for export.
	 *
	 * @return new XMLNode object
	 */
	public XMLNode toXMLNode(){
		XMLNode ret = new XMLNode("property");
		ret.addAttribute(new XMLAttribute("name", getName()));
		ret.addAttribute(new XMLAttribute("type", getPropertyType().toString()));
		ret.setContent(""+getValue());
		return ret;
	}
	
	/**
	 * <p>getPropertyType.</p>
	 *
	 * @return a {@link net.anotheria.anodoc.data.PropertyType} object.
	 */
	public abstract PropertyType getPropertyType();
	
}
