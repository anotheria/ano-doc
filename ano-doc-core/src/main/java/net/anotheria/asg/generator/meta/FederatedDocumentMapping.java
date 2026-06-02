package net.anotheria.asg.generator.meta;
/**
 * Represents a document mapping in the federation.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class FederatedDocumentMapping {
	/**
	 * The source document name.
	 */
	private String sourceDocument;
	/**
	 * The target document.
	 */
	private String targetDocument;
	/**
	 * The key used as key for the target document.
	 */
	private String targetKey;
	/**
	 * <p>Getter for the field <code>sourceDocument</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getSourceDocument() {
		return sourceDocument;
	}
	/**
	 * <p>Setter for the field <code>sourceDocument</code>.</p>
	 *
	 * @param sourceDocument a {@link java.lang.String} object.
	 */
	public void setSourceDocument(String sourceDocument) {
		this.sourceDocument = sourceDocument;
	}
	/**
	 * <p>Getter for the field <code>targetDocument</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTargetDocument() {
		return targetDocument;
	}
	/**
	 * <p>Setter for the field <code>targetDocument</code>.</p>
	 *
	 * @param targetDocument a {@link java.lang.String} object.
	 */
	public void setTargetDocument(String targetDocument) {
		this.targetDocument = targetDocument;
	}
	/**
	 * <p>Getter for the field <code>targetKey</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTargetKey() {
		return targetKey;
	}
	/**
	 * <p>Setter for the field <code>targetKey</code>.</p>
	 *
	 * @param targetKey a {@link java.lang.String} object.
	 */
	public void setTargetKey(String targetKey) {
		this.targetKey = targetKey;
	}
	
	/** {@inheritDoc} */
	@Override public String toString(){
		return sourceDocument+" -> "+targetKey+"."+targetDocument;
	}
}
