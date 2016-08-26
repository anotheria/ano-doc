package net.anotheria.asg.generator.forms.meta;

/**
 * TODO please remined another to comment this class
 *
 * @author another
 * @version $Id: $Id
 */
public class MetaFormTableHeader {
	private String width;
	private String key;
	
	/**
	 * <p>Constructor for MetaFormTableHeader.</p>
	 */
	public MetaFormTableHeader(){
		
	}
	
	/**
	 * <p>Constructor for MetaFormTableHeader.</p>
	 *
	 * @param aKey a {@link java.lang.String} object.
	 * @param aWidth a {@link java.lang.String} object.
	 */
	public MetaFormTableHeader(String aKey, String aWidth){
		key = aKey;
		width = aWidth;
	}

	/**
	 * <p>Getter for the field <code>key</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * <p>Getter for the field <code>width</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getWidth() {
		return width;
	}

	/**
	 * <p>Setter for the field <code>key</code>.</p>
	 *
	 * @param string a {@link java.lang.String} object.
	 */
	public void setKey(String string) {
		key = string;
	}

	/**
	 * <p>Setter for the field <code>width</code>.</p>
	 *
	 * @param string a {@link java.lang.String} object.
	 */
	public void setWidth(String string) {
		width = string;
	}
	
	/**
	 * <p>toString.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toString(){
		return key+" "+width;
	}

}
