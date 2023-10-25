package net.anotheria.anodoc.util.context;

import java.io.Serializable;
import java.util.List;

/**
 * Abstract class for CallContext which is assigned to each thread and allows multilinguality support and more.
 *
 * @author another
 * @version $Id: $Id
 */
public abstract class CallContext implements Serializable{
	
	private String currentAuthor;
	private String currentLanguage;
	private DBContext dbContext;
	private BrandConfig brandConfig;
	
	/**
	 * <p>reset.</p>
	 */
	public void reset(){
		currentLanguage = null;
		dbContext = null;
		brandConfig = null;
	}

	/**
	 * <p>Getter for the field <code>currentLanguage</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getCurrentLanguage() {
		return currentLanguage == null ? getDefaultLanguage() : currentLanguage;
	}

	/**
	 * <p>Setter for the field <code>currentLanguage</code>.</p>
	 *
	 * @param currentLanguage a {@link java.lang.String} object.
	 */
	public void setCurrentLanguage(String currentLanguage) {
		this.currentLanguage = currentLanguage;
	}
	
	/**
	 * <p>getDefaultLanguage.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public abstract String getDefaultLanguage();
	
	/**
	 * <p>getSupportedLanguages.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public abstract List<String> getSupportedLanguages();
	
	/**
	 * <p>Setter for the field <code>dbContext</code>.</p>
	 *
	 * @param aDbContent a {@link net.anotheria.anodoc.util.context.DBContext} object.
	 */
	public void setDbContext(DBContext aDbContent){
		dbContext = aDbContent;
	}
	
	/**
	 * <p>Getter for the field <code>dbContext</code>.</p>
	 *
	 * @return a {@link net.anotheria.anodoc.util.context.DBContext} object.
	 */
	public DBContext getDbContext(){
		//this is not really thread-safe, but its better to risk to create one dummy object as to 
		//synchronize here. Especially since the CallContext is ThreadLocal and therefore shouldn't 
		//be accessed from many threads at once.
		if (dbContext==null)
			dbContext = new DBContext();
		return dbContext;
	}

	/**
	 * <p>Getter for the field <code>currentAuthor</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getCurrentAuthor() {
		return currentAuthor;
	}

	/**
	 * <p>Setter for the field <code>currentAuthor</code>.</p>
	 *
	 * @param currentAuthor a {@link java.lang.String} object.
	 */
	public void setCurrentAuthor(String currentAuthor) {
		this.currentAuthor = currentAuthor;
	}

	/**
	 * <p>Getter for the field <code>brandConfig</code>.</p>
	 *
	 * @return a {@link BrandConfig} object.
	 */
	public BrandConfig getBrandConfig() {
		return brandConfig;
	}

	/**
	 * <p>Setter for the field <code>brandConfig</code>.</p>
	 *
	 * @param brandConfig a {@link BrandConfig} object.
	 */
	public void setBrandConfig(BrandConfig brandConfig) {
		this.brandConfig = brandConfig;
	}
}

