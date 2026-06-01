package net.anotheria.asg.data;

import net.anotheria.util.xml.XMLNode;

/**
 * <p>Abstract AbstractFederatedVO class.</p>
 *
 * @author another
 * @version $Id: $Id
 */
public abstract class AbstractFederatedVO implements DataObject{
	
	/**
	 * <p>toXMLNode.</p>
	 *
	 * @return a {@link net.anotheria.util.xml.XMLNode} object.
	 */
	public XMLNode toXMLNode(){
		return new XMLNode("NotImplemented "+getId());
	}
	
	/**
	 * <p>getObjectInfo.</p>
	 *
	 * @return a {@link net.anotheria.asg.data.ObjectInfo} object.
	 */
	public ObjectInfo getObjectInfo(){
		ObjectInfo ret = new ObjectInfo(this);
		ret.setAuthor("none");
		return ret;
	}
	
	/**
	 * <p>clone.</p>
	 *
	 * @return a {@link java.lang.Object} object.
	 * @throws java.lang.CloneNotSupportedException if any.
	 */
	public abstract Object clone() throws CloneNotSupportedException;
	
	/** {@inheritDoc} */
	@Override public int hashCode(){
		return getId().hashCode();
	}

}
