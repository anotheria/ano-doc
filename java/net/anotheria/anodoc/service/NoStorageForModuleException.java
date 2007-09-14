package net.anotheria.anodoc.service;

/**
 * Thrown when the {@link net.anotheria.anodoc.service.IModuleService} needs 
 * to store or load a module instance, but doesn't have the appropriate storage configured. 
 */
public class NoStorageForModuleException extends Exception{
	public NoStorageForModuleException(String moduleId){
		super("No storage for module: "+moduleId);
	}

}
