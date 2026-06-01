package net.anotheria.asg.generator.meta;

/**
 * Representation of the Integer type.
 *
 * @author another
 * @version $Id: $Id
 */
public class IntType extends AbstractType{

	/* (non-Javadoc)
 	 * @see net.anotheria.anodoc.generator.meta.IMetaType#toJava()
 	 */
	/** {@inheritDoc} */
	@Override public String toJava() {
		return "int";
	}
	/** {@inheritDoc} */
	@Override public String toJavaObject(){
		return "Integer";
	}

}
