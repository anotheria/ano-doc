package net.anotheria.asg.generator.forms.meta;

/**
 * TODO please remined another to comment this class
 *
 * @author another
 * @version $Id: $Id
 */
public class MetaFormSingleField extends MetaFormField{
	private String title;
	private String type;
	private int size;

	/**
	 * <p>Constructor for MetaFormSingleField.</p>
	 *
	 * @param aName a {@link java.lang.String} object.
	 */
	public MetaFormSingleField(String  aName){
		super(aName);
	}

	/**
	 * <p>Getter for the field <code>title</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * <p>Setter for the field <code>title</code>.</p>
	 *
	 * @param string a {@link java.lang.String} object.
	 */
	public void setTitle(String string) {
		title = string;
	}
	/**
	 * <p>Getter for the field <code>type</code>.</p>
	 *
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}
	/**
	 * <p>Setter for the field <code>type</code>.</p>
	 *
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * <p>getJavaType.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getJavaType(){
		if (type.equals("boolean"))
			return "boolean";
		return "String";
	}
    
	/**
	 * <p>isSpacer.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isSpacer(){
		return type.equals("spacer");
	}

	/**
	 * <p>isSingle.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isSingle(){
		return true;
	}
    
	/**
	 * <p>isComplex.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isComplex(){
		return false;
	}
	
	/**
	 * <p>toString.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toString(){
		return getType()+" "+getName()+" ["+getSize()+"]";
	}

	/**
	 * <p>Getter for the field <code>size</code>.</p>
	 *
	 * @return a int.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * <p>Setter for the field <code>size</code>.</p>
	 *
	 * @param size a int.
	 */
	public void setSize(int size) {
		this.size = size;
	}

}
