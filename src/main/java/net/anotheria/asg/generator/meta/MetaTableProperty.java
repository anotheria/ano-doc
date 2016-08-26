package net.anotheria.asg.generator.meta;

import java.util.ArrayList;
import java.util.List;

/**
 * A property type for representation of tables.
 *
 * @author another
 * @version $Id: $Id
 */
public class MetaTableProperty extends MetaContainerProperty{
	/**
	 * Columns of the table.
	 */
	private List<MetaProperty> columns;
	
	/**
	 * <p>Constructor for MetaTableProperty.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public MetaTableProperty(String name){
		super(name); 
		columns = new ArrayList<MetaProperty>();
	}
	
	/**
	 * <p>addColumn.</p>
	 *
	 * @param columnName a {@link java.lang.String} object.
	 */
	public void addColumn(String columnName){
		MetaProperty p = new MetaProperty(getName()+"_"+columnName,MetaProperty.Type.LIST);
		columns.add(p);	
	}
	
	/**
	 * <p>Getter for the field <code>columns</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<MetaProperty> getColumns() {
		return columns;
	}

	/**
	 * <p>Setter for the field <code>columns</code>.</p>
	 *
	 * @param list a {@link java.util.List} object.
	 */
	public void setColumns(List<MetaProperty> list) {
		columns = list;
	}
	
	/**
	 * <p>extractSubName.</p>
	 *
	 * @param p a {@link net.anotheria.asg.generator.meta.MetaProperty} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String extractSubName(MetaProperty p){
		return p.getName().substring(getName().length()+1);	
	}

	/* (non-Javadoc)
	 * @see net.anotheria.anodoc.generator.meta.MetaContainerProperty#getContainerEntryName()
	 */
	/**
	 * <p>getContainerEntryName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getContainerEntryName() {
		return "Row";
	}

}
