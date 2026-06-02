package net.anotheria.asg.generator.meta;
/**
 * Definition of a federated module. Each federated module is mapped to a key.
 *
 * @author another
 * @version $Id: $Id
 */
public class FederatedModuleDef{
	/**
	 * Name of the Module.
	 */
	private String name;
	/**
	 * Key of the module.
	 */
	private String key;

	FederatedModuleDef(String aKey, String aName){
		name = aName;
		key = aKey;
	}

	/**
	 * <p>Getter for the field <code>key</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * <p>Getter for the field <code>name</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getName() {
		return name;
	}
} 
