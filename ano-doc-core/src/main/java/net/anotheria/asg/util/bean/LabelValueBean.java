package net.anotheria.asg.util.bean;

import net.anotheria.util.BasicComparable;
import net.anotheria.util.sorter.IComparable;

import java.io.Serializable;

/**
 * A simple id, value pair, which is used for holding the
 * available values and their ids in checkboxes and selects. Actually 
 * it's automatically present for every map in the profiles.xml.
 * @author lrosenberg
 * Created on 16.06.2004
 */
public class LabelValueBean implements IComparable<LabelValueBean>, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The label.
	 */
	private String label;
	/**
	 * The value.
	 */
	private String value;
	/**
	 * Creates a new label value bean.
	 * @param aValue
	 * @param aLabel
	 */
	public LabelValueBean(String aValue, String aLabel){
		this.value = aValue;
		this.label = aLabel;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String string) {
		value = string;
	}
	
	@Override public String toString(){
		return ""+value+"="+label;
	}

	/* (non-Javadoc)
	 * @see net.anotheria.util.sorter.IComparable#compareTo(net.anotheria.util.sorter.IComparable, int)
	 */
	public int compareTo(IComparable anotherComparable, int method) {
		switch(method){
			case LabelValueSortType.METHOD_LABEL:
				return BasicComparable.compareString(label, ((LabelValueBean)anotherComparable).label);
			case LabelValueSortType.METHOD_VALUE:
				return BasicComparable.compareString(value, ((LabelValueBean)anotherComparable).value);
			default:
				throw new IllegalArgumentException("Unsupported compare method "+method);
		}
	}
	

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
