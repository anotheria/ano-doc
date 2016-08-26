package net.anotheria.asg.generator.view.meta;

/**
 * An empty element in a view.
 *
 * @author another
 * @version $Id: $Id
 */
public class MetaEmptyElement extends MetaViewElement{
	/**
	 * Creates a new empty element.
	 */
	public MetaEmptyElement(){
		super(null);
	}
	
	/** {@inheritDoc} */
	@Override public String toString(){
		return "empty";
	}
	
	/** {@inheritDoc} */
	@Override public boolean isComparable(){
		return false;
	}
}
