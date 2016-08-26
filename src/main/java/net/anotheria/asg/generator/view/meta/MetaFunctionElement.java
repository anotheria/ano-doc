package net.anotheria.asg.generator.view.meta;

/**
 * A view element which represents an action.
 *
 * @author another
 * @version $Id: $Id
 */
public class MetaFunctionElement extends MetaViewElement{
	/**
	 * Functions caption.
	 */
	private String caption;

	/**
	 * Creates a new MetaFunctionElement.
	 *
	 * @param aName a {@link java.lang.String} object.
	 */
	public MetaFunctionElement(String aName){
		super(aName);
	}
	
	/**
	 * <p>getPropertyName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getPropertyName(){
		return getName()+"Link";
	}
	

	/**
	 * <p>Getter for the field <code>caption</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getCaption() {
		return caption == null || caption.length() == 0 ? getName() : caption;
	}

	/** {@inheritDoc} */
	public void setCaption(String string) {
		caption = string;
	}
	
	/** {@inheritDoc} */
	@Override public String toString(){
		return "Fun: "+getName();
	}

	/** {@inheritDoc} */
	@Override public boolean isComparable(){
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}

}
