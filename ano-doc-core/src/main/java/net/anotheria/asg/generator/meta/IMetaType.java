package net.anotheria.asg.generator.meta;

/**
 * Represent the type of a property.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public interface IMetaType {
	/**
	 * <p>toJava.</p>
	 *
	 * @return the type representation of this MetaType in java language.
	 */
	String toJava();
	/**
	 * <p>toJavaObject.</p>
	 *
	 * @return java object for this type (for example Integer for int).
	 */
	String toJavaObject();
	
	/**
	 * <p>toPropertySetter.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	String toPropertySetter();
	
	/**
	 * <p>toPropertyGetter.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	String toPropertyGetter();
	
	/**
	 * <p>toBeanGetter.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	String toBeanGetter(String name);
	
	/**
	 * <p>toBeanSetter.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	String toBeanSetter(String name);	
}
