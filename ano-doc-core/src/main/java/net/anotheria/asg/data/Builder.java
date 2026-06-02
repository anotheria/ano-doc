package net.anotheria.asg.data;

/**
 * Generic interface for DataObject Builders.
 *
 * @author lrosenberg
 * @param <T> the type to build.
 * @version $Id: $Id
 */
public interface Builder<T extends DataObject> {
	/**
	 * Builds a new instance of T.
	 *
	 * @return T
	 */
	T build();
}
