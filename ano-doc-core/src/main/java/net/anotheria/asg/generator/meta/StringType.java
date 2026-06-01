package net.anotheria.asg.generator.meta;

/**
 * Representation of the string type.
 *
 * @author another
 * @version $Id: $Id
 */
public class StringType extends AbstractType{

	/* (non-Javadoc)
	 * @see net.anotheria.anodoc.generator.meta.IMetaType#toJava()
	 */
	/** {@inheritDoc} */
	@Override public String toJava() {
		return "String";
	}

	/** {@inheritDoc} */
	@Override public String toJavaObject(){
		return toJava();
	}

}
