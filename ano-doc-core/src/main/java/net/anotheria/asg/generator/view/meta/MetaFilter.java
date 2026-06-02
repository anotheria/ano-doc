package net.anotheria.asg.generator.view.meta;
/**
 * Definition of a filter.
 *
 * @author another
 * @version $Id: $Id
 */
public class MetaFilter implements Cloneable{
	/**
	 * Human readable name of the filter.
	 */
	private String name;
	/**
	 * The name of the filter realization class.
	 */
	private String className;
	/**
	 * Name of the field the filter applies to.
	 */
	private String fieldName;
	
	/**
	 * <p>Constructor for MetaFilter.</p>
	 *
	 * @param aName a {@link java.lang.String} object.
	 * @param aClassName a {@link java.lang.String} object.
	 */
	public MetaFilter(String aName, String aClassName){
		name = aName;
		className = aClassName;
	}
	
	/** {@inheritDoc} */
	@Override public Object clone(){
		try{
			return super.clone();
		}catch(Exception e){
			return null;
		}
	}

	/**
	 * <p>Getter for the field <code>fieldName</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * <p>Setter for the field <code>fieldName</code>.</p>
	 *
	 * @param fieldName a {@link java.lang.String} object.
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	/**
	 * <p>Getter for the field <code>name</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * <p>Setter for the field <code>name</code>.</p>
	 *
	 * @param aName a {@link java.lang.String} object.
	 */
	public void setName(String aName){
		name = aName;
	}
	
	/**
	 * <p>Getter for the field <code>className</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getClassName(){
		return className;
	}

	/**
	 * Extracts the classname from the fully qualified class name.
	 *
	 * @return class name
	 */
	public String getClassNameOnly(){
		if (className.lastIndexOf('.')==-1)
			return className;
		return className.substring(className.lastIndexOf('.')+1);
	}

}
