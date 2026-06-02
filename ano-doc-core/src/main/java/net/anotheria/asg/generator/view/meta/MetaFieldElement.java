package net.anotheria.asg.generator.view.meta;

/**
 * A view element which is tied to a document attribute and presented as edit-field.
 *
 * @author another
 * @version $Id: $Id
 */
public class MetaFieldElement extends MetaViewElement{

	/**
	 * <p>Constructor for MetaFieldElement.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public MetaFieldElement(String name){
		super(name);
	}

	/**
	 * <p>getVariableName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getVariableName(){
		return getName();
	}

	/**
	 * <p>toString.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toString(){
		return "Field "+getName();
	}

}
