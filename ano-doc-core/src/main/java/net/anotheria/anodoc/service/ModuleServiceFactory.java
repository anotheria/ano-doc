package net.anotheria.anodoc.service;

/**
 * Use this factory to obtain IModuleService instances.
 *
 * @author another
 * @version $Id: $Id
 */
public class ModuleServiceFactory {

	private ModuleServiceFactory() {
	}

	/**
	 * Returns a IModuleService instance.
	 *
	 * @return a {@link net.anotheria.anodoc.service.IModuleService} object.
	 */
	public static IModuleService createModuleService(){
		return ModuleServiceImpl.getInstance();
	}
}
