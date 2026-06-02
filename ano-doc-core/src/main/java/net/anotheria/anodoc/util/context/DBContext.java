package net.anotheria.anodoc.util.context;

import java.io.Serializable;

/**
 * A part of CallContext used for DB controlling. Its useful if you have multiple tables for same purposes and want to perform selection outside of the dao.
 *
 * @author another
 * @version $Id: $Id
 */
public class DBContext implements Serializable{
	/**
	 * Prefix of the table name.
	 */
	private String tableNamePrefix;
	/**
	 * Postfix of the table name.
	 */
	private String tableNamePostfix;
	
	/** Constant <code>DELIMITER="_"</code> */
	public static final String DELIMITER = "_";
	/**
	 * Creates a new DBContext.
	 */
	public DBContext(){
		tableNamePostfix = tableNamePrefix = "";
	}
	
	/**
	 * <p>Getter for the field <code>tableNamePostfix</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTableNamePostfix() {
		return tableNamePostfix;
	}
	/**
	 * <p>Setter for the field <code>tableNamePostfix</code>.</p>
	 *
	 * @param tableNamePostfix a {@link java.lang.String} object.
	 */
	public void setTableNamePostfix(String tableNamePostfix) {
		this.tableNamePostfix = tableNamePostfix;
	}
	/**
	 * <p>Getter for the field <code>tableNamePrefix</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTableNamePrefix() {
		return tableNamePrefix;
	}
	/**
	 * <p>Setter for the field <code>tableNamePrefix</code>.</p>
	 *
	 * @param tableNamePrefix a {@link java.lang.String} object.
	 */
	public void setTableNamePrefix(String tableNamePrefix) {
		this.tableNamePrefix = tableNamePrefix;
	}
	
	/**
	 * <p>getTableNameInContext.</p>
	 *
	 * @param tableName a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String getTableNameInContext(String tableName){
		StringBuilder ret = new StringBuilder();
		if (tableNamePrefix!=null && tableNamePrefix.length()>0)
			ret.append(tableNamePrefix).append(DELIMITER);
		ret.append(tableName);
		if (tableNamePostfix!=null && tableNamePostfix.length()>0)
			ret.append(tableNamePostfix).append(DELIMITER);
		
		return ret.toString();
	}
}
