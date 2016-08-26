package net.anotheria.asg.generator.forms.meta;

/**
 * Base class for FormFields which are parts of a Form.
 *
 * @author lrosenberg
 * @created on Mar 14, 2005
 * @version $Id: $Id
 */
public abstract class MetaFormField {
	/**
	 * Name of the field.
	 */
    private String name;
    
    /**
     * <p>Constructor for MetaFormField.</p>
     *
     * @param aName a {@link java.lang.String} object.
     */
    public MetaFormField(String aName){
        name = aName;
    }
    
    /**
     * <p>Getter for the field <code>name</code>.</p>
     *
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * <p>Setter for the field <code>name</code>.</p>
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /** {@inheritDoc} */
    @Override public String toString(){
        return name;
    }
    
    /**
     * <p>isSingle.</p>
     *
     * @return a boolean.
     */
    public abstract boolean isSingle();
    
    /**
     * <p>isComplex.</p>
     *
     * @return a boolean.
     */
    public abstract boolean isComplex();
    
}
