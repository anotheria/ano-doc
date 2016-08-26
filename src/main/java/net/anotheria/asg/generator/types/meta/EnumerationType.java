package net.anotheria.asg.generator.types.meta;

import java.util.ArrayList;
import java.util.List;

/**
 * Internal representation of an enumeration type.
 *
 * @author another
 * @version $Id: $Id
 */
public class EnumerationType extends DataType{
	/**
	 * List of possible values.
	 */
	private List<String> values;
	
	/**
	 * <p>Constructor for EnumerationType.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public EnumerationType(String name){
		super(name);
		values = new ArrayList<String>();
	}
	
	/**
	 * <p>addValue.</p>
	 *
	 * @param aValue a {@link java.lang.String} object.
	 */
	public void addValue(String aValue){
		values.add(aValue);
	}
	
	/** {@inheritDoc} */
	@Override public String toString(){
		return "Enumeration "+getName()+" :"+values;
	}
	/**
	 * <p>Getter for the field <code>values</code>.</p>
	 *
	 * @return possible values
	 */
	public List<String> getValues() {
		return values;
	}

	/**
	 * Sets possible values.
	 *
	 * @param list values
	 */
	public void setValues(List<String> list) {
		values = list;
	}

}
