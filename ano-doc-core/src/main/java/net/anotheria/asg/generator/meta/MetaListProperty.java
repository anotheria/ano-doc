package net.anotheria.asg.generator.meta;


/**
 * A container property is a container of a property of another type. This is usually used for lists or tables. They may only contain one type of the data - the containedPoperty.
 *
 * @author another
 * @version $Id: $Id
 */
public class MetaListProperty extends MetaContainerProperty{
	
	/**
	 * The property inside this container.
	 */
	private MetaProperty containedProperty;
	
	/**
	 * <p>Constructor for MetaListProperty.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public MetaListProperty(String name){
		super(name, MetaProperty.Type.LIST);
	}

	/**
	 * <p>Constructor for MetaListProperty.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param aContainedProperty a {@link net.anotheria.asg.generator.meta.MetaProperty} object.
	 */
	public MetaListProperty(String name, MetaProperty aContainedProperty){
		super(name, MetaProperty.Type.LIST);
		containedProperty = aContainedProperty;
	}

	/**
	 * <p>Getter for the field <code>containedProperty</code>.</p>
	 *
	 * @return a {@link net.anotheria.asg.generator.meta.MetaProperty} object.
	 */
	public MetaProperty getContainedProperty() {
		return containedProperty;
	}

	/**
	 * <p>Setter for the field <code>containedProperty</code>.</p>
	 *
	 * @param property a {@link net.anotheria.asg.generator.meta.MetaProperty} object.
	 */
	public void setContainedProperty(MetaProperty property) {
		containedProperty = property;
	}

	/* (non-Javadoc)
	 * @see net.anotheria.anodoc.generator.meta.MetaContainerProperty#getContainerEntryName()
	 */
	/**
	 * <p>getContainerEntryName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getContainerEntryName() {
		return "Element";
	}
	
	/** {@inheritDoc} */
	@Override public String toJavaType(){
		return super.toJavaType()+"<"+containedProperty.toJavaObjectType()+">";
	}
	
	/** {@inheritDoc} */
	@Override public String toJavaErasedType(){
		return super.toJavaType();
	}

	
	
//	@Override
//	public String toJavaObjectType(){
//		return super.toJavaObjectType()+getGenericTypeDeclaration();
//	}
//	
//	protected String getGenericTypeDeclaration(){			
//		return containedProperty != null? StringUtils.surroundWith(containedProperty.toJavaObjectType(), '<', '>'): "";
//	}

}
