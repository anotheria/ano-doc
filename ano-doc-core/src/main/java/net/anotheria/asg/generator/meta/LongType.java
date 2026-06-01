package net.anotheria.asg.generator.meta;

/**
 * Representation of the LongType.
 *
 * @author another
 * @version $Id: $Id
 */
public class LongType extends AbstractType{

	/* (non-Javadoc)
	 * @see net.anotheria.anodoc.generator.meta.IMetaType#toJava()
	 */
	/** {@inheritDoc} */
	@Override public String toJava() {
		return "long";
	}
	
	/** {@inheritDoc} */
	@Override public String toJavaObject(){
		return "Long";
	}
}
