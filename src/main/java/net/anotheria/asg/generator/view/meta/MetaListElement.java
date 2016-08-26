package net.anotheria.asg.generator.view.meta;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO please remined another to comment this class
 *
 * @author another
 * @version $Id: $Id
 */
public class MetaListElement extends MetaViewElement{
	private List<MetaViewElement> elements;
	
	/**
	 * <p>Constructor for MetaListElement.</p>
	 */
	public MetaListElement(){
		super("");
		elements = new ArrayList<MetaViewElement>();
	}
	
	/**
	 * <p>Getter for the field <code>elements</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<MetaViewElement> getElements() {
		return elements;
	}

	/**
	 * <p>Setter for the field <code>elements</code>.</p>
	 *
	 * @param list a {@link java.util.List} object.
	 */
	public void setElements(List<MetaViewElement> list) {
		elements = list;
	}

	/**
	 * <p>addElement.</p>
	 *
	 * @param element a {@link net.anotheria.asg.generator.view.meta.MetaViewElement} object.
	 */
	public void addElement(MetaViewElement element){
		elements.add(element);
	}
	
	/**
	 * <p>toString.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toString(){
		return ""+elements;
	}
	
	/**
	 * <p>isComparable.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isComparable(){
		return false;
	}
	
}
