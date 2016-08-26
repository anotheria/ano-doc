package net.anotheria.asg.util;

import net.anotheria.asg.util.CmsChangesTracker.Action;
/**
 * <p>DocumentChange class.</p>
 *
 * @author another
 * @version $Id: $Id
 */
public class DocumentChange{
	private String userName;
	private String parentName;
	private String documentName;
	private String id;
	private Action action;
	private long timestamp;
	
	/**
	 * <p>Getter for the field <code>userName</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * <p>Setter for the field <code>userName</code>.</p>
	 *
	 * @param userName a {@link java.lang.String} object.
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * <p>Getter for the field <code>documentName</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getDocumentName() {
		return documentName;
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
	 * <p>Getter for the field <code>action</code>.</p>
	 *
	 * @return a {@link net.anotheria.asg.util.CmsChangesTracker.Action} object.
	 */
	public Action getAction() {
		return action;
	}
	/**
	 * <p>Setter for the field <code>action</code>.</p>
	 *
	 * @param action a {@link net.anotheria.asg.util.CmsChangesTracker.Action} object.
	 */
	public void setAction(Action action) {
		this.action = action;
	}
	/**
	 * <p>Getter for the field <code>timestamp</code>.</p>
	 *
	 * @return a long.
	 */
	public long getTimestamp() {
		return timestamp;
	}
	/**
	 * <p>Setter for the field <code>timestamp</code>.</p>
	 *
	 * @param timestamp a long.
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
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
	 * <p>Getter for the field <code>parentName</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getParentName() {
		return parentName;
	}
	/**
	 * <p>Setter for the field <code>parentName</code>.</p>
	 *
	 * @param parentName a {@link java.lang.String} object.
	 */
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
}
