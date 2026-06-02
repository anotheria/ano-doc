package net.anotheria.asg.generator.view.meta;

import java.util.ArrayList;
import java.util.List;

/**
 * The definition of a dialog.
 *
 * @author another
 * @version $Id: $Id
 */
public class MetaDialog {
	/**
	 * Name of the dialog.
	 */
	private String name;
	/**
	 * Title of the dialog.
	 */
	private String title;
	/**
	 * Elements of the dialog.
	 */
	private List<MetaViewElement> elements;
	/**
	 * Link to customization javascript.
	 */
	private String javascript;
	
	/**
	 * Creates a new dialog with the given name.
	 *
	 * @param aName the name of the dialog.
	 */
	public MetaDialog(String aName){
		this.name = aName;
		elements = new ArrayList<MetaViewElement>();
	}

	/**
	 * Adds new dialog element.
	 *
	 * @param element element to add
	 */
	public void addElement(MetaViewElement element){
		elements.add(element);
	}
	
	

	/**
	 * <p>Getter for the field <code>elements</code>.</p>
	 *
	 * @return list of dialog elements
	 */
	public List<MetaViewElement> getElements() {
		return elements;
	}

	/**
	 * <p>Getter for the field <code>name</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getName() {
		return name;
	}

	/**
	 * <p>Getter for the field <code>title</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTitle() {
		return title;
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
	 * <p>Setter for the field <code>name</code>.</p>
	 *
	 * @param string a {@link java.lang.String} object.
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * <p>Setter for the field <code>title</code>.</p>
	 *
	 * @param string a {@link java.lang.String} object.
	 */
	public void setTitle(String string) {
		title = string;
	}

	/**
	 * <p>Getter for the field <code>javascript</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getJavascript() {
		return javascript;
	}


	/**
	 * <p>Setter for the field <code>javascript</code>.</p>
	 *
	 * @param javascript a {@link java.lang.String} object.
	 */
	public void setJavascript(String javascript) {
		this.javascript = javascript;
	}

	/** {@inheritDoc} */
	@Override public String toString(){
		return name+" "+elements;
	}

}
