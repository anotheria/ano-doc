package net.anotheria.asg.generator.view.meta;

/**
 * This element allows to specify a custom function element (link or button).
 *
 * @author another
 * @version $Id: $Id
 */
public class MetaCustomFunctionElement extends MetaViewElement{
	/**
	 * Link target of the element.
	 */
	private String link;
	/**
	 * Caption of the link.
	 */
	private String caption;
	
	/**
	 * <p>Getter for the field <code>caption</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getCaption() {
		return caption;
	}

	/** {@inheritDoc} */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * <p>Getter for the field <code>link</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getLink() {
		return link;
	}

	/**
	 * <p>Setter for the field <code>link</code>.</p>
	 *
	 * @param link a {@link java.lang.String} object.
	 */
	public void setLink(String link) {
		this.link = link;
	}
	/**
	 * Creates a new MetaCustomFunctionElement.
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public MetaCustomFunctionElement(String name){
		super(name);
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}

}

