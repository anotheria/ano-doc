package net.anotheria.asg.data;

import net.anotheria.util.NumberUtils;
import net.anotheria.util.xml.XMLAttribute;
import net.anotheria.util.xml.XMLNode;

public class ObjectInfo {
	
	
	/**
	 * The id of the document
	 */
	private String id;
	
	/**
	 * The author of the document
	 */
	private String author;
	
	/**
	 * The last change timestamp of the document
	 */
	private long lastChangeTimestamp;

	/**
	 * The footprint of the document (built over all attribute-values in all languages).
	 */
	private String footprint;

	private String type;
	
	public ObjectInfo(){
		
	}
	
	public ObjectInfo(DataObject object){
		setId(object.getId());
		setLastChangeTimestamp(object.getLastUpdateTimestamp());
		setFootprint(object.getFootprint());
		setType(object.getDefinedName());
	}

	
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public long getLastChangeTimestamp() {
		return lastChangeTimestamp;
	}

	public void setLastChangeTimestamp(long lastChangeTimestamp) {
		this.lastChangeTimestamp = lastChangeTimestamp;
	}

	public String getFootprint() {
		return footprint;
	}

	public void setFootprint(String footprint) {
		this.footprint = footprint;
	}
	
	public String toString(){
		return "Id: "+getId()+", Ts: "+getLastChangeTimestamp()+", Footprint: "+getFootprint()+", Author: "+getAuthor();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
