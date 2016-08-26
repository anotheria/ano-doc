package net.anotheria.asg.util.rmi;

import java.rmi.registry.Registry;

import org.configureme.annotations.ConfigureMe;

/**
 * Configuration for rmi services in ano-doc. This config file is configured by the configureme framework.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
@ConfigureMe (allfields=true)
public class RMIConfig {


	/**
	 * The host where the RMIRegistry is running.
	 */
	private String registryHost;

	/**
	 * The port where the RMIRegistry is running.
	 */
	private int  registryPort;
	
	/**
	 * Default registry host value if nothing is explicitely configured.
	 */
	public static final String DEF_REGISTRY_HOST = "localhost";
	/**
	 * Default registry port value if nothing is explicitely configured.
	 */
	public static final int DEF_REGISTRY_PORT = Registry.REGISTRY_PORT;
	
	/**
	 * Creates a new config.
	 */
	RMIConfig(){
		registryHost = DEF_REGISTRY_HOST;
		registryPort = DEF_REGISTRY_PORT;
	}
		
	/**
	 * <p>Getter for the field <code>registryHost</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getRegistryHost(){
		return registryHost;
	}
	
	/**
	 * <p>Getter for the field <code>registryPort</code>.</p>
	 *
	 * @return a int.
	 */
	public int getRegistryPort(){
		return registryPort;
	}
	
	/** {@inheritDoc} */
	@Override public String toString(){
		return "RMIConfig "+getRegistryHost()+":"+getRegistryPort();
	}

	/**
	 * <p>Setter for the field <code>registryHost</code>.</p>
	 *
	 * @param aRegistryHost a {@link java.lang.String} object.
	 */
	public void setRegistryHost(final String aRegistryHost) {
		registryHost = aRegistryHost;
	}

	/**
	 * <p>Setter for the field <code>registryPort</code>.</p>
	 *
	 * @param aRegistryPort a int.
	 */
	public void setRegistryPort(final int aRegistryPort) {
		registryPort = aRegistryPort;
	}
}
