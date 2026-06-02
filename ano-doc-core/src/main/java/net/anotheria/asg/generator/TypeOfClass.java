package net.anotheria.asg.generator;
/**
 * Type of generateable class.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public enum TypeOfClass {
	/**
	 * A generated java class.
	 */
	CLASS,
    /**
	 * A generated java enum.
	 */
	ENUM,
	/**
	 * A generated java interface.
	 */
	INTERFACE;
	
	/**
	 * <p>getDefault.</p>
	 *
	 * @return a {@link net.anotheria.asg.generator.TypeOfClass} object.
	 */
	public static TypeOfClass getDefault(){
		return CLASS;
	}

	/**
	 * <p>toJava.</p>
	 *
	 * @return java declaration
	 */
	public String toJava() {
		return toString().toLowerCase();
	}
}
