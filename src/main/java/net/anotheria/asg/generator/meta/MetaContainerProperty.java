package net.anotheria.asg.generator.meta;

/**
 * TODO please remined another to comment this class
 *
 * @author another
 * @version $Id: $Id
 */
public class MetaContainerProperty extends MetaProperty{
	
	
	/**
	 * <p>Constructor for MetaContainerProperty.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public MetaContainerProperty(String name){
		super(name, MetaProperty.Type.INT);
	}
	
	/**
	 * <p>Constructor for MetaContainerProperty.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param type a {@link net.anotheria.asg.generator.meta.MetaProperty.Type} object.
	 */
	public MetaContainerProperty(String name, MetaProperty.Type type){
		super(name, type);
	}

	/**
	 * <p>getContainerEntryName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getContainerEntryName(){
		return getClass().getName().substring(getClass().getName().lastIndexOf('.')+1);	
	}
}
