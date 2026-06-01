package net.anotheria.asg.generator.view.meta;

import net.anotheria.asg.generator.IGenerateable;

/**
 * A section in the overview.
 *
 * @author another
 * @version $Id: $Id
 */
public class MetaSection implements IGenerateable{
	/**
	 * The title of the section.
	 */
	private String title;
	
	/**
	 * <p>Constructor for MetaSection.</p>
	 *
	 * @param aTitle a {@link java.lang.String} object.
	 */
	public MetaSection(String aTitle){
		this.title = aTitle;
	}
	/**
	 * <p>Getter for the field <code>title</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTitle() {
		return title;
	}
	
	/** {@inheritDoc} */
	@Override public String toString(){
		return "section "+title;
	}
	
	/** {@inheritDoc} */
	@Override public boolean equals(Object o){
		return (o instanceof MetaSection) && ((MetaSection)o).title.equals(title);
	}
	
	/**
	 * <p>hashCode.</p>
	 *
	 * @return a int.
	 */
	public int hashCode() {
		  assert false : "hashCode not designed";
		  return 42; // any arbitrary constant will do 
	}
}
