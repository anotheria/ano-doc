package net.anotheria.asg.generator.types.meta;

import net.anotheria.asg.generator.IGenerateable;

/**
 * A custom data type.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public abstract class DataType implements IGenerateable{
	/**
	 * Name of the data type.
	 */
	private String name;
	
	/**
	 * <p>Constructor for DataType.</p>
	 *
	 * @param aName a {@link java.lang.String} object.
	 */
	protected DataType(String aName){
		name = aName;
	}
	
	/**
	 * <p>Getter for the field <code>name</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getName() {
		return name;
	}

	/**
	 * <p>Setter for the field <code>name</code>.</p>
	 *
	 * @param string a {@link java.lang.String} object.
	 */
	public void setName(String string) {
		name = string;
	}
	
	

}
