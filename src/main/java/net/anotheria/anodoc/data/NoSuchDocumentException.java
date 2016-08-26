package net.anotheria.anodoc.data;

/**
 * This exception will be thrown when a not existing Document was requested,
 * or the DataHolder with correspoding name is not a Document.
 *
 * @author another
 * @version $Id: $Id
 */
public class NoSuchDocumentException extends RuntimeException{
	/**
	 * SerialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new instance.
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public NoSuchDocumentException(String name){
		super("No such document "+name+", or "+name+" is not a document");
	}

}
