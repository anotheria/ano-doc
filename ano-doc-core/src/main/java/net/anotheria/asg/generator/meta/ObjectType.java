package net.anotheria.asg.generator.meta;

/**
 * <p>ObjectType class.</p>
 *
 * @author another
 * @version $Id: $Id
 */
public class ObjectType implements IMetaType{
	
	private String clazz;
	
	/**
	 * <p>Constructor for ObjectType.</p>
	 *
	 * @param aClazz a {@link java.lang.String} object.
	 */
	public ObjectType(String aClazz){
		clazz = aClazz;
	}

	/** {@inheritDoc} */
	public String toBeanGetter(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	/** {@inheritDoc} */
	public String toBeanSetter(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * <p>toJava.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toJava() {
		return clazz;
	}

	/**
	 * <p>toJavaObject.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toJavaObject() {
		return clazz;
	}

	/**
	 * <p>toPropertyGetter.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toPropertyGetter() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * <p>toPropertySetter.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toPropertySetter() {
		return null;
	}

}
