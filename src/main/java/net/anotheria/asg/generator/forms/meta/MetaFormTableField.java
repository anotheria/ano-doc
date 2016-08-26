package net.anotheria.asg.generator.forms.meta;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO please remined another to comment this class
 *
 * @author another
 * @version $Id: $Id
 */
public class MetaFormTableField extends MetaFormField{
	
	private int rows;
	private List<MetaFormTableColumn> columns;
	
	/**
	 * <p>Constructor for MetaFormTableField.</p>
	 *
	 * @param aName a {@link java.lang.String} object.
	 */
	public MetaFormTableField(String aName){
		super(aName);
		columns = new ArrayList<MetaFormTableColumn>();
	}

	/**
	 * <p>isSingle.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isSingle(){
		return false;
	}
    
	/**
	 * <p>isComplex.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isComplex(){
		return true;
	}

	/**
	 * <p>Getter for the field <code>rows</code>.</p>
	 *
	 * @return a int.
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * <p>Setter for the field <code>rows</code>.</p>
	 *
	 * @param i a int.
	 */
	public void setRows(int i) {
		rows = i;
	}
	
	/**
	 * <p>toString.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toString(){
		return "table "+getName()+" with "+rows+" row(s) and columns: "+columns;
	}
	
	/**
	 * <p>addColumn.</p>
	 *
	 * @param column a {@link net.anotheria.asg.generator.forms.meta.MetaFormTableColumn} object.
	 */
	public void addColumn(MetaFormTableColumn column){
		columns.add(column);
	}

	/**
	 * <p>Getter for the field <code>columns</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<MetaFormTableColumn> getColumns() {
		return columns;
	}

	/**
	 * <p>Setter for the field <code>columns</code>.</p>
	 *
	 * @param list a {@link java.util.List} object.
	 */
	public void setColumns(List<MetaFormTableColumn> list) {
		columns = list;
	}
	
	/**
	 * <p>getVariableName.</p>
	 *
	 * @param row a int.
	 * @param column a int.
	 * @return a {@link java.lang.String} object.
	 */
	public String getVariableName(int row, int column){
		return getName()+"R"+(row+1)+"C"+(column+1);		
	}
	

}
 
