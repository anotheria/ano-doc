package net.anotheria.asg.generator.meta;

/**
 * Representation of the DoubleType.
 *
 * @author another
 * @version $Id: $Id
 */
public class DoubleType extends AbstractType{

	/* (non-Javadoc)
	 * @see net.anotheria.anodoc.generator.meta.IMetaType#toJava()
	 */
	/** {@inheritDoc} */
	@Override public String toJava() {
		return "double";
	}
	
	/** {@inheritDoc} */
	@Override public String toJavaObject(){
		return "Double";
	}

}
