package net.anotheria.asg.generator.meta;

import net.anotheria.util.StringUtils;


/**
 * Represents a single property of a document. A property may be basic, like int, boolean, long, or complex, like list or table.
 * This class defines single one typed property mainly.
 *
 * @author another
 * @version $Id: $Id
 */
public class MetaProperty implements Cloneable{
	
	public static enum Type{
		STRING("string"),
        PASSWORD("password"),
		TEXT("text"),
		BOOLEAN("boolean"),
		INT("int"),
		LONG("long"),
		DOUBLE("double"),
		FLOAT("float"),
		LIST("list"),
		IMAGE("image");
		

		String name;
		Type(String aName){
			name = aName;
		}
		
		public String getName(){
			return name;
		}
		
		public static Type findTypeByName(String name){
			for(Type t: values())
				if(t.getName().equals(name))
					return t;
			return null;
		}

		@Override
		public String toString() {
			return "Type{" +
					"name='" + name + '\'' +
					'}';
		}
	}
	
	/**
	 * The type of the property as string.
	 */
	private Type type;
	/**
	 * Name of the property.
	 */
	private String name;
	/**
	 * Resolved property type.
	 */
	private IMetaType metaType;
	/**
	 * True if the property is multilingual.
	 */
	private boolean multilingual;
	/**
	 * True if the property is readonly. For example id is a readonly property. Basically this is only used by the view, 
	 * the application itself still free to change a readonly property.
	 */
	private boolean readonly;
	/**
	 * Creates a new MetaProperty with given name and type description.
	 *
	 * @param aName a {@link java.lang.String} object.
	 * @param aType a {@link net.anotheria.asg.generator.meta.MetaProperty.Type} object.
	 */
	public MetaProperty(String aName, Type aType){
		this(aName, aType, false);
	}
	
	/**
	 * <p>Constructor for MetaProperty.</p>
	 *
	 * @param aName a {@link java.lang.String} object.
	 * @param aType a {@link net.anotheria.asg.generator.meta.MetaProperty.Type} object.
	 * @param aMultilingual a boolean.
	 */
	public MetaProperty(String aName, Type aType, boolean aMultilingual){
		name = aName;
		type = aType;
		metaType = TypeFactory.createType(aType);
		multilingual = aMultilingual;
		
		if (name==null)
			throw new IllegalArgumentException("name is null");
	}
	
	/**
	 * <p>Constructor for MetaProperty.</p>
	 *
	 * @param aName a {@link java.lang.String} object.
	 * @param aType a {@link net.anotheria.asg.generator.meta.IMetaType} object.
	 */
	public MetaProperty(String aName, IMetaType aType){
		name = aName;
		metaType = aType;
		multilingual = false;

		if (name==null)
			throw new IllegalArgumentException("name is null");
	}


	/**
	 * <p>Getter for the field <code>name</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the internal name of the property for language variant.
	 *
	 * @param language a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String getName(String language) {
		return language == null || !isMultilingual()? getName() : name+StringUtils.capitalize(language);
	}

	/**
	 * <p>Getter for the field <code>name</code>.</p>
	 *
	 * @param addOn a {@link java.lang.String} object.
	 * @param language a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String getName(String addOn, String language) {
		return language == null ? getName()+addOn : name+addOn+StringUtils.capitalize(language);
	}

	/**
	 * <p>Getter for the field <code>type</code>.</p>
	 *
	 * @return a {@link net.anotheria.asg.generator.meta.MetaProperty.Type} object.
	 */
	public Type getType() {
		return type;
	}

	/**
	 * <p>Setter for the field <code>name</code>.</p>
	 *
	 * @param string a {@link java.lang.String} object.
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * <p>toNameConstant.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toNameConstant(){
		return "PROP_"+getNameConstantBase();
	}
	
	/**
	 * <p>toNameConstant.</p>
	 *
	 * @param language a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String toNameConstant(String language){
		return "PROP_"+getNameConstantBase()+"_"+language.toUpperCase();
	}
	
	private String getNameConstantBase(){
		String ret = "";
		for (int i=0; i<name.length(); i++){
			char c = name.charAt(i);
			if (Character.isLowerCase(c))
				ret += Character.toUpperCase(c);
			if (Character.isUpperCase(c)){
				ret += "_"+c;
			}
			if (!Character.isLetter(c))
				ret += c;
		}
		return ret;
	}

	/**
	 * <p>getAccesserName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getAccesserName(){
		return Character.toUpperCase(name.charAt(0))+name.substring(1);
	}
	
	/**
	 * <p>getAccesserName.</p>
	 *
	 * @param language a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String getAccesserName(String language){
		return language == null ? getAccesserName() : Character.toUpperCase(name.charAt(0))+name.substring(1)+Character.toUpperCase(language.charAt(0))+language.substring(1);
	}

	/**
	 * <p>toJavaType.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toJavaType(){
		return metaType.toJava();
	}
	
	/**
	 * <p>toJavaErasedType.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toJavaErasedType(){
		return metaType.toJava();
	}

	/**
	 * <p>toJavaObjectType.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toJavaObjectType(){
		return metaType.toJavaObject();
	}
	
	/**
	 * <p>toPropertyGetter.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toPropertyGetter(){
		return metaType.toPropertyGetter();
	}
	
	/**
	 * <p>toPropertySetter.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toPropertySetter(){
		return metaType.toPropertySetter();
	}
	
	/**
	 * <p>toBeanGetter.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toBeanGetter(){
		return metaType.toBeanGetter(name);
	}
	
	/**
	 * <p>toBeanGetter.</p>
	 *
	 * @param language a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String toBeanGetter(String language){
		return language == null ? toBeanGetter() : metaType.toBeanGetter(name)+StringUtils.capitalize(language);
	}

	/**
	 * <p>toBeanSetter.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toBeanSetter(){
		return metaType.toBeanSetter(name);
	}

	/**
	 * <p>toBeanSetter.</p>
	 *
	 * @param language a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String toBeanSetter(String language){
		return language == null ? toBeanSetter() : metaType.toBeanSetter(name)+StringUtils.capitalize(language);
	}

	/** {@inheritDoc} */
	@Override public String toString(){
		return type+" "+name;
	}
	
	/**
	 * <p>isLinked.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isLinked(){
		return false;
	}

	/**
	 * <p>toSetter.</p>
	 *
	 * @param language a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String toSetter(String language){
		return "set"+getAccesserName(language);
	}
	
	/**
	 * <p>toSetter.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toSetter(){
		return "set"+getAccesserName();
	}
	
	/**
	 * <p>toGetter.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toGetter(){
		return "get"+getAccesserName();
	}
	
	/**
	 * <p>toGetter.</p>
	 *
	 * @param language a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	public String toGetter(String language){
		return "get"+getAccesserName(language);
	}

	/**
	 * Returns the metatype of this property.
	 *
	 * @return a {@link net.anotheria.asg.generator.meta.IMetaType} object.
	 */
	public IMetaType getMetaType(){
		return metaType;
	}
	
	
	/**
	 * Returns true if the property is multilingual.
	 *
	 * @return a boolean.
	 */
	public boolean isMultilingual() {
		return multilingual;
	}
	/**
	 * Sets the multilingual support of the property.
	 *
	 * @param multilingual a boolean.
	 */
	public void setMultilingual(boolean multilingual) {
		this.multilingual = multilingual;
	}
	/**
	 * Returns true if the property is read only.
	 *
	 * @return a boolean.
	 */
	public boolean isReadonly() {
		return readonly;
	}
	/**
	 * <p>Setter for the field <code>readonly</code>.</p>
	 *
	 * @param readonly a boolean.
	 */
	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}
	
	/** {@inheritDoc} */
	@Override public Object clone(){
		try{
			return super.clone();
		}catch(CloneNotSupportedException e){
			//ignore
		}
		throw new AssertionError("Can't happen");
	}

}
