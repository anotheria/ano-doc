package net.anotheria.anodoc.service;

import net.anotheria.anodoc.data.Module;
import net.anotheria.asg.util.listener.IModuleListener;

/**
 * Storage for Module instances.
 *
 * @author another
 * @version $Id: $Id
 */
public interface IModuleStorage {

	/**
	 * Saves the given instance permanently.
	 *
	 * @param module a {@link net.anotheria.anodoc.data.Module} object.
	 * @throws net.anotheria.anodoc.service.StorageFailureException if any.
	 */
	void saveModule(Module module) throws StorageFailureException;
	
	/**
	 * Loads the specified instance.
	 *
	 * @return loaded module
	 * @param ownerId a {@link java.lang.String} object.
	 * @param copyId a {@link java.lang.String} object.
	 * @throws net.anotheria.anodoc.service.NoStoredModuleEntityException if any.
	 * @throws net.anotheria.anodoc.service.StorageFailureException if any.
	 */
	Module loadModule(String ownerId, String copyId) throws NoStoredModuleEntityException, StorageFailureException;
	
	/**
	 * Delete the specified instance.
	 *
	 * @param ownerId a {@link java.lang.String} object.
	 * @param copyId a {@link java.lang.String} object.
	 * @throws net.anotheria.anodoc.service.StorageFailureException if any.
	 */
	void deleteModule(String ownerId, String copyId) throws StorageFailureException;

	/**
	 * Adds a module listener.
	 *
	 * @param listener the listener to add.
	 */
	void addModuleListener(IModuleListener listener);

	/**
	 * Removes the module listener from the module storage.
	 *
	 * @param listener the listener to remove.
	 */
	void removeModuleListener(IModuleListener listener);

}
