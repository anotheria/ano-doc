package net.anotheria.asg.util.bean;

import net.anotheria.anodoc.data.NoSuchPropertyException;
import net.anotheria.asg.data.DataObject;
import net.anotheria.util.StringUtils;

/**
 * A bean which represents an incoming link to a document (as a result of a query).
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class LinkToMeBean {
	/**
	 * The type of the linking document.
	 */
	private String targetDocumentType;
	/**
	 * The id of the linking document.
	 */
	private String targetDocumentId;
	/**
	 * A description of the linking document.
	 */
	private String targetDocumentDescription;
	/**
	 * The linking property.
	 */
	private String targetDocumentProperty;
	/**
	 * A link for the edit tool for the linking document.
	 */
	private String targetDocumentLink;
	
	/**
	 * <p>Constructor for LinkToMeBean.</p>
	 */
	public LinkToMeBean(){
		
	}
	
	/**
	 * <p>Constructor for LinkToMeBean.</p>
	 *
	 * @param doc a {@link net.anotheria.asg.data.DataObject} object.
	 * @param propertyName a {@link java.lang.String} object.
	 */
	public LinkToMeBean(DataObject doc, String propertyName){
		targetDocumentType = doc.getDefinedName();
		targetDocumentId = doc.getId();
		targetDocumentLink = doc.getDefinedParentName().toLowerCase()+StringUtils.capitalize(doc.getDefinedName())+"Edit?ts="+System.currentTimeMillis()+"&pId="+doc.getId();
		targetDocumentProperty = propertyName;
		try{
			targetDocumentDescription = ""+doc.getPropertyValue("name");
		}catch(NoSuchPropertyException e){
			
		}catch(RuntimeException e){
			//temporarly, as long as VO objects are throwing exceptions of this type instead of something meaningful.
		}
	}
	
	/**
	 * <p>isDescriptionAvailable.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isDescriptionAvailable(){
		return targetDocumentDescription!=null && targetDocumentDescription.length()>0;
	}
	
	/**
	 * <p>Getter for the field <code>targetDocumentType</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTargetDocumentType() {
		return targetDocumentType;
	}
	/**
	 * <p>Setter for the field <code>targetDocumentType</code>.</p>
	 *
	 * @param targetDocumentType a {@link java.lang.String} object.
	 */
	public void setTargetDocumentType(String targetDocumentType) {
		this.targetDocumentType = targetDocumentType;
	}
	/**
	 * <p>Getter for the field <code>targetDocumentId</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTargetDocumentId() {
		return targetDocumentId;
	}
	/**
	 * <p>Setter for the field <code>targetDocumentId</code>.</p>
	 *
	 * @param targetDocumentId a {@link java.lang.String} object.
	 */
	public void setTargetDocumentId(String targetDocumentId) {
		this.targetDocumentId = targetDocumentId;
	}
	/**
	 * <p>Getter for the field <code>targetDocumentDescription</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTargetDocumentDescription() {
		return targetDocumentDescription;
	}
	/**
	 * <p>Setter for the field <code>targetDocumentDescription</code>.</p>
	 *
	 * @param targetDocumentDescription a {@link java.lang.String} object.
	 */
	public void setTargetDocumentDescription(String targetDocumentDescription) {
		this.targetDocumentDescription = targetDocumentDescription;
	}
	/**
	 * <p>Getter for the field <code>targetDocumentProperty</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTargetDocumentProperty() {
		return targetDocumentProperty;
	}
	/**
	 * <p>Setter for the field <code>targetDocumentProperty</code>.</p>
	 *
	 * @param targetDocumentProperty a {@link java.lang.String} object.
	 */
	public void setTargetDocumentProperty(String targetDocumentProperty) {
		this.targetDocumentProperty = targetDocumentProperty;
	}
	/**
	 * <p>Getter for the field <code>targetDocumentLink</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTargetDocumentLink() {
		return targetDocumentLink;
	}
	/**
	 * <p>Setter for the field <code>targetDocumentLink</code>.</p>
	 *
	 * @param targetDocumentLink a {@link java.lang.String} object.
	 */
	public void setTargetDocumentLink(String targetDocumentLink) {
		this.targetDocumentLink = targetDocumentLink;
	}
	
	
}
