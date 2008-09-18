package net.anotheria.anodoc.query2;

import java.io.Serializable;

public class QueryProperty implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2030752289719048811L;
	private String name;
	private Object value;
	private boolean unprepaireable;
	
	public QueryProperty(String aName, Object aValue){
		this(aName, aValue, false);
	}
	
	public QueryProperty(String aName, Object aValue, boolean anUnprepaireable){
		name = aName;
		value = aValue;
		unprepaireable = anUnprepaireable;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
	public String toString(){
		return getName() + "=" +getValue();
	}
	
	public String getComparator(){
		return " = ";
	}
	
	public boolean doesMatch(Object o){
		return o== null ? value == null :
			o.equals(value);
	}
	
	public boolean unprepaireable(){
		return unprepaireable;
	}
	
	protected Object getOriginalValue(){
		return value;
	}
	
}
