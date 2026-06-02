package net.anotheria.anodoc.data;

import net.anotheria.util.StringUtils;

/**
 * Declaration of supported types for properties.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public enum PropertyType {
	/**
	 * Integer.
	 */
	INT('I',IntProperty.class),
	/**
	 * Long.
	 */
	LONG('L',LongProperty.class),
	/**
	 * Double.
	 */
	DOUBLE('D',DoubleProperty.class),
	/**
	 * Float.
	 */
	FLOAT('F',FloatProperty.class),
	/**
	 * List of properties.
	 */
	LIST('[',ListProperty.class),
	/**
	 * String.
	 */
	STRING('S',StringProperty.class),
	/**
	 * Text. Same as String but with different editors.
	 */
	TEXT('T',TextProperty.class),
	/**
	 * Boolean.
	 */
	BOOLEAN('B',BooleanProperty.class);

	/**
	 * Indicator for textual representation of the property.
	 */
	private char indicator;

	/**
	 * Indicator for class representation of the property.
	 */
	private final Class<? extends Property> clazz;

	/**
	 * Creates a new property type.
	 * @param anIndicator
	 */
	PropertyType(char anIndicator,final Class<? extends Property> cls) {
		indicator = anIndicator;
		clazz = cls;
	}

	public Class<? extends Property> getClazz() {
		return clazz;
	}
	
	/**
	 * Returns the indicator for this property type.
	 *
	 * @return a char.
	 */
	public char getIndicator(){
		return indicator;
	}

	public static PropertyType byName(final String name){
		if(StringUtils.isEmpty(name))
			throw new IllegalArgumentException("name is not valid");
		return PropertyType.valueOf(name);
	}
}