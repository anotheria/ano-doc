package net.anotheria.asg.generator.meta;

/**
 * Internal representation of the FloatType.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class FloatType extends AbstractType{

	/* (non-Javadoc)
	 * @see net.anotheria.anodoc.generator.meta.IMetaType#toJava()
	 */
	/** {@inheritDoc} */
	@Override public String toJava() {
		return "float";
	}
	
	/** {@inheritDoc} */
	@Override public String toJavaObject(){
		return "Float";
	}

}
