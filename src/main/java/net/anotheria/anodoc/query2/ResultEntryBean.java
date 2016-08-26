package net.anotheria.anodoc.query2;

/**
 * A bean which contains one result entry.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class ResultEntryBean {
	/**
	 * The link for document edit dialog.
	 */
	private String editLink;
	/**
	 * The id of the document/object which was found.
	 */
	private String documentId;
	/**
	 * The name of the document/object which was found.
	 */
	private String documentName;
	/**
	 * The name of the property which matched.
	 */
	private String propertyName;
	/**
	 * The matching info (additional information).
	 */
	private String info;
	
	/**
	 * <p>Getter for the field <code>documentId</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getDocumentId() {
		return documentId;
	}
	/**
	 * <p>Setter for the field <code>documentId</code>.</p>
	 *
	 * @param documentId a {@link java.lang.String} object.
	 */
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	/**
	 * <p>Getter for the field <code>editLink</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getEditLink() {
		return editLink;
	}
	/**
	 * <p>Setter for the field <code>editLink</code>.</p>
	 *
	 * @param editLink a {@link java.lang.String} object.
	 */
	public void setEditLink(String editLink) {
		this.editLink = editLink;
	}
	/**
	 * <p>Getter for the field <code>info</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getInfo() {
		return info;
	}
	/**
	 * <p>Setter for the field <code>info</code>.</p>
	 *
	 * @param info a {@link java.lang.String} object.
	 */
	public void setInfo(String info) {
		this.info = info;
	}
	/**
	 * <p>Getter for the field <code>propertyName</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getPropertyName() {
		return propertyName;
	}
	/**
	 * <p>Setter for the field <code>propertyName</code>.</p>
	 *
	 * @param propertyName a {@link java.lang.String} object.
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	/**
	 * <p>Setter for the field <code>documentName</code>.</p>
	 *
	 * @param documentName a {@link java.lang.String} object.
	 */
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	/**
	 * <p>Getter for the field <code>documentName</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getDocumentName() {
		return documentName;
	}
	
	
}
