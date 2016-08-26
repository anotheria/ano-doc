package net.anotheria.asg.generator.view.meta;
/**
 * Allows the developer to integrate a custom section into generated frontend.
 *
 * @author another
 * @version $Id: $Id
 */
public class MetaCustomSection extends MetaSection{
	/**
	 * Path to be called whenever the section is selected.
	 */
	private String path;
	
	/**
	 * <p>Constructor for MetaCustomSection.</p>
	 *
	 * @param title a {@link java.lang.String} object.
	 */
	public MetaCustomSection(String title){
		super(title);
	}
	
	/** {@inheritDoc} */
	@Override public String toString(){
		return getTitle()+": "+path;
	}

	/**
	 * <p>Getter for the field <code>path</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getPath() {
		return path;
	}

	/**
	 * <p>Setter for the field <code>path</code>.</p>
	 *
	 * @param path a {@link java.lang.String} object.
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}

}
