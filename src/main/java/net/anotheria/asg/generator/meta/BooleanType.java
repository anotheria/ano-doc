package net.anotheria.asg.generator.meta;

/**
 * Representation of the boolean type.
 *
 * @author another
 * @version $Id: $Id
 */
public class BooleanType extends AbstractType{

	/* (non-Javadoc)
	 * @see net.anotheria.anodoc.generator.meta.IMetaType#toJava()
	 */
	/** {@inheritDoc} */
	@Override public String toJava() {
		return "boolean";
	}
	
	/** {@inheritDoc} */
	@Override public String toJavaObject(){
		return "Boolean";
	}


	/** {@inheritDoc} */
	@Override public String toBeanGetter(String name){
		return "is"+Character.toUpperCase(name.charAt(0))+name.substring(1);
	}
	

}
