package net.anotheria.anodoc.util;

import net.anotheria.anodoc.data.Module;
import net.anotheria.anodoc.service.IModuleFactory;

/**
 * Interface for module storage.
 *
 * @author another
 * @version $Id: $Id
 */
public interface ICommonModuleStorage {

	/**
	 * Saves the given instance permanently.
	 *
	 * @param module module to save
	 * @throws net.anotheria.anodoc.util.CommonModuleStorageException if any.
	 */
	void saveModule(Module module) throws CommonModuleStorageException;

	/**
	 * Loads the specified instance.
	 *
	 * @return loaded module
	 * @param moduleId a {@link java.lang.String} object.
	 * @param ownerId a {@link java.lang.String} object.
	 * @param copyId a {@link java.lang.String} object.
	 * @param factory a {@link net.anotheria.anodoc.service.IModuleFactory} object.
	 * @throws net.anotheria.anodoc.util.CommonModuleStorageException if any.
	 */
	Module loadModule(String moduleId, String ownerId, String copyId, IModuleFactory factory) throws CommonModuleStorageException;

	/**
	 * Delete the specified instance.
	 *
	 * @param moduleId id of module
	 * @param ownerId id of owner
	 * @param copyId id of copy
	 * @throws net.anotheria.anodoc.util.CommonModuleStorageException if any.
	 */
	void deleteModule(String moduleId, String ownerId, String copyId) throws CommonModuleStorageException;

}
