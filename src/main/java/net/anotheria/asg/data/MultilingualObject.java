package net.anotheria.asg.data;

/**
 * Describes a multilingual object as
 *
 * @author lrosenberg.
 * @version $Id: $Id
 */
public interface MultilingualObject {
	/**
	 * Returns true if the multilingual features are disabled for this instance.
	 *
	 * @return a boolean.
	 */
	boolean isMultilingualDisabledInstance();
	
	/**
	 * Disables support for multilingual instances for this object.
	 *
	 * @param value a boolean.
	 */
	void setMultilingualDisabledInstance(boolean value);
}
