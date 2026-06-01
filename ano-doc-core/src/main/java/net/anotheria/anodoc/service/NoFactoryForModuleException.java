package net.anotheria.anodoc.service;

/**
 * Thrown when the {@link net.anotheria.anodoc.service.IModuleService} needs
 * to create a new module instance, but doesn't have the appropriate factory configured.
 *
 * @author another
 * @version $Id: $Id
 */
public class NoFactoryForModuleException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * <p>Constructor for NoFactoryForModuleException.</p>
	 *
	 * @param message a {@link java.lang.String} object.
	 */
	public NoFactoryForModuleException(String message){
		super(message);
	}

}
