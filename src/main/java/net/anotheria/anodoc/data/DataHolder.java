package net.anotheria.anodoc.data;

import java.io.Serializable;

import net.anotheria.util.xml.XMLAttribute;
import net.anotheria.util.xml.XMLNode;

/**
 * This class represents the root class for all objects that can hold information
 * and therefore data. In particular its Document, DocumentList and Property.
 *
 * @since 1.0
 * @author lrosenberg
 * @version $Id: $Id
 */
public abstract class DataHolder implements Serializable, Cloneable{
	/**
	 * svid.
	 */
	private static final long serialVersionUID = -145899051333223122L;
 
	/**
	 * The name of the DataHolder. The id should be unique in a hash context (i.e. Document).
	 * In lists a name can be used repeatedly.  
	 */
	private String id;
	
	/**
	 * Creates a new DataHolder object with the given id.
	 *
	 * @param anId a {@link java.lang.String} object.
	 */
	protected DataHolder(String anId){
		this.id = anId;
	}
	
	/**
	 * Returns the name of this DataHolder.
	 *
	 * @return the name of this DataHolder object.
	 */
	public String getId(){
		return id;
	}
	
	/**
	 * Sets the name of this DataHolder to the given value.<br>
	 * <b>Warning:</b> Only use this method, if you know what you do.
	 *
	 * @param anId a {@link java.lang.String} object.
	 */
	protected void setId(String anId){
		this.id = anId;
	}

	/**
	 * The 'size' of a DataHolder is needed for the quota calculation.<br>
	 * The size of an atomic DataHolder like a Property should be the real amount of
	 * bytes it uses, whether the size of complex objects should be cumulated.<br>
	 * The overhead for administration shouldn't be counted (i.e. the size of an IntProperty
	 * is the amount of bytes needed to store the int value itself, not
	 * the amount of bytes needed to store the DataHolder object).
	 *
	 * @return a long.
	 */
	public abstract long getSizeInBytes();
	
	/**
	 * <p>clone.</p>
	 *
	 * @return a {@link java.lang.Object} object.
	 * @throws java.lang.CloneNotSupportedException if any.
	 */
	public Object clone() throws CloneNotSupportedException{
		return super.clone();
	}
	
	/**
	 * <p>toXMLNode.</p>
	 *
	 * @return a {@link net.anotheria.util.xml.XMLNode} object.
	 */
	public XMLNode toXMLNode(){
		XMLNode e = new XMLNode("dataholder");
		e.addAttribute( new XMLAttribute("id", getId()));
		return e;
	}
	
	/** {@inheritDoc} */
	@Override public int hashCode(){
		return getId().hashCode();
	}

}
