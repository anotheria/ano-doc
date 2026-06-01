package net.anotheria.asg.generator;

import java.util.HashMap;
import java.util.Map;

/**
 * Options for the generator.
 *
 * @author another
 * @version $Id: $Id
 */
public class GenerationOptions {
	/**
	 * Name of the rmi enabling option.
	 */
	public static final String RMI = "rmi";
	/**
	 * Name of the inmemory option.
	 */
	public static final String INMEMORY = "inmemory";
	/**
	 * Name of the fixture option.
	 */
	public static final String FIXTURE = "fixture";
	/**
	 * Name of the JDBCConfig option.
	 */
	public static final String JDBCCONFIG = "jdbcConfig";
	/**
	 * Internal map with generation options.
	 */
	private Map<String, GenerationOption> options;
	
	/**
	 * <p>Constructor for GenerationOptions.</p>
	 */
	public GenerationOptions(){
		options = new HashMap<String, GenerationOption>();
	}
	/**
	 * Returns the option stored und the given key.
	 *
	 * @param key a {@link java.lang.String} object.
	 * @return a {@link net.anotheria.asg.generator.GenerationOption} object.
	 */
	public GenerationOption get(String key){
		return options.get(key);
	}
	
	/**
	 * Returns true if the option exists and is equal "true".
	 *
	 * @param key a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	public boolean isEnabled(String key){
		GenerationOption v = options.get(key);
		return v!=null && v.getValue().equalsIgnoreCase("true");
	}
	
	/**
	 * Sets an option with given key and value.
	 *
	 * @param key a {@link java.lang.String} object.
	 * @param value a {@link java.lang.String} object.
	 */
	public void set(String key, String value){
		options.put(key, new GenerationOption(key,value));
	}
	/**
	 * Sets the option.
	 *
	 * @param option a {@link net.anotheria.asg.generator.GenerationOption} object.
	 */
	public void set(GenerationOption option){
		options.put(option.getName(), option);
	}
}
