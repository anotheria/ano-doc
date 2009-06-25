package net.anotheria.asg.generator.meta;

/**
 * Base class for type implementations.
 * @author another
 */
public abstract class AbstractType implements IMetaType{

	/* (non-Javadoc)
	 * @see net.anotheria.anodoc.generator.meta.IMetaType#toPropertyGetter()
	 */
	public String toPropertyGetter() {
		return "get"+Character.toUpperCase(toJava().charAt(0))+toJava().substring(1);
	}

	/* (non-Javadoc)
	 * @see net.anotheria.anodoc.generator.meta.IMetaType#toPropertySetter()
	 */
	public String toPropertySetter() {
		return "set"+Character.toUpperCase(toJava().charAt(0))+toJava().substring(1);
	}

	public String toBeanGetter(String name){
		return "get"+Character.toUpperCase(name.charAt(0))+name.substring(1);
	}
	
	public String toBeanSetter(String name){
		return "set"+Character.toUpperCase(name.charAt(0))+name.substring(1);			
	}

}
