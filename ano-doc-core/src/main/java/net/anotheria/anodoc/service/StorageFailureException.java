package net.anotheria.anodoc.service;

import net.anotheria.anodoc.util.CommonModuleStorageException;

/**
 * General exception a service can throw if it can't proceed as desired.
 *
 * @author another
 * @version $Id: $Id
 */
public class StorageFailureException extends CommonModuleStorageException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * <p>Constructor for StorageFailureException.</p>
	 *
	 * @param message a {@link java.lang.String} object.
	 */
	public StorageFailureException(String message){
		super(message);
	}

}
