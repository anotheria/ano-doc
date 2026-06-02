package net.anotheria.asg.util.bean;

import net.anotheria.util.sorter.SortType;

/**
 * A sort type for IDValueBean sorting.
 * @author lrosenberg
 * Created on 23.06.2004
 */
public class LabelValueSortType extends SortType{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int METHOD_VALUE = 1;
	public static final int METHOD_LABEL = 2;
	
	public LabelValueSortType(){
		super(METHOD_VALUE);
	}
}
