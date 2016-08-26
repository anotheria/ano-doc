package net.anotheria.anodoc.service;

/**
 * Thrown when the {@link net.anotheria.anodoc.service.IModuleService} needs
 * to store or load a module instance, but doesn't have the appropriate storage configured.
 *
 * @author another
 * @version $Id: $Id
 */
public class NoStorageForModuleException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * <p>Constructor for NoStorageForModuleException.</p>
	 *
	 * @param moduleId a {@link java.lang.String} object.
	 */
	public NoStorageForModuleException(String moduleId){
		super("No storage for module: "+moduleId);
	}

}
