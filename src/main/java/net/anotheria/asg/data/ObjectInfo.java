package net.anotheria.asg.data;

import net.anotheria.util.NumberUtils;
import net.anotheria.util.xml.XMLAttribute;
import net.anotheria.util.xml.XMLNode;

/**
 * An object which contains the meta information about an object.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class ObjectInfo {
	
	
	/**
	 * The id of the object. It can be a VO or a Document.
	 */
	private String id;
	
	/**
	 * The author of the document or vo.
	 */
	private String author;
	
	/**
	 * The last change timestamp of the document or vo.
	 */
	private long lastChangeTimestamp;

	/**
	 * The footprint of the document (built over all attribute-values in all languages).
	 */
	private String footprint;

	/**
	 * The type of the document or vo.
	 */
	private String type;

	/**
	 * Creates a new ObjectInfo.
	 */
	public ObjectInfo(){
	}
	/**
	 * Creates a new ObjectInfo for a DataObject.
	 *
	 * @param object the data object to create the info for.
	 */
	public ObjectInfo(DataObject object){
		setId(object.getId());
		setLastChangeTimestamp(object.getLastUpdateTimestamp());
		setFootprint(object.getFootprint());
		setType(object.getDefinedName());
	}

	
	/**
	 * <p>Getter for the field <code>author</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * <p>Setter for the field <code>author</code>.</p>
	 *
	 * @param author a {@link java.lang.String} object.
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * <p>Getter for the field <code>lastChangeTimestamp</code>.</p>
	 *
	 * @return a long.
	 */
	public long getLastChangeTimestamp() {
		return lastChangeTimestamp;
	}

	/**
	 * <p>Setter for the field <code>lastChangeTimestamp</code>.</p>
	 *
	 * @param lastChangeTimestamp a long.
	 */
	public void setLastChangeTimestamp(long lastChangeTimestamp) {
		this.lastChangeTimestamp = lastChangeTimestamp;
	}

	/**
	 * <p>Getter for the field <code>footprint</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getFootprint() {
		return footprint;
	}

	/**
	 * <p>Setter for the field <code>footprint</code>.</p>
	 *
	 * @param footprint a {@link java.lang.String} object.
	 */
	public void setFootprint(String footprint) {
		this.footprint = footprint;
	}
	
	/** {@inheritDoc} */
	@Override public String toString(){
		return "Id: "+getId()+", Ts: "+getLastChangeTimestamp()+", Footprint: "+getFootprint()+", Author: "+getAuthor()+", IsoTs: "+NumberUtils.makeISO8601TimestampString(getLastChangeTimestamp());
	}

	/**
	 * <p>Getter for the field <code>id</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getId() {
		return id;
	}

	/**
	 * <p>Setter for the field <code>id</code>.</p>
	 *
	 * @param id a {@link java.lang.String} object.
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Creates the XMLNode for xml representation of this object info.
	 *
	 * @return created XMLNode
	 */
	public XMLNode toXML(){
		XMLNode ret = new XMLNode("objectinfo");
		ret.addAttribute(new XMLAttribute("id",getId()));
		ret.addChildNode(getChildNode("id", getId()));
		ret.addChildNode(getChildNode("type", getType()));
		ret.addChildNode(getChildNode("timestamp", ""+getLastChangeTimestamp()));
		ret.addChildNode(getChildNode("iso8601timestamp", NumberUtils.makeISO8601TimestampString(getLastChangeTimestamp())));
		ret.addChildNode(getChildNode("footprint", getFootprint()));
		return ret;
	}
	
	private XMLNode getChildNode(String name, String text){
		XMLNode ret = new XMLNode(name);
		ret.setContent(text);
		return ret;
	}

	/**
	 * <p>Getter for the field <code>type</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getType() {
		return type;
	}

	/**
	 * <p>Setter for the field <code>type</code>.</p>
	 *
	 * @param type a {@link java.lang.String} object.
	 */
	public void setType(String type) {
		this.type = type;
	}
	
}
