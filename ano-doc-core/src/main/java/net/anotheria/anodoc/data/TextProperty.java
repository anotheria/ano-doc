package net.anotheria.anodoc.data;



/**
 * TextProperty is a derivative of a StringProperty which support full text search queries
 * (if the underlying storage supports them too).
 *
 * @author another
 * @version $Id: $Id
 */
public class TextProperty extends StringProperty {

	/**
	 * Creates a new TextProperty with given name and null value.
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public TextProperty(String name) {
		super(name);
	}

	/**
	 * Creates a new TextProperty with given name and value.
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param value a {@link java.lang.String} object.
	 */
	public TextProperty(String name, String value) {
		super(name, value);
	}

	/** {@inheritDoc} */
	@Override public PropertyType getPropertyType(){
		return PropertyType.TEXT;
	}

}
