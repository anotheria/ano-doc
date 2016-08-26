package net.anotheria.asg.util.bean;

/**
 * A link in the presentation of paging.
 *
 * @author another
 * @version $Id: $Id
 */
public class PagingLink {
	/**
	 * Link to a page.
	 */
	private String link;
	/**
	 * Caption of a page.
	 */
	private String caption;
	
	/**
	 * <p>Constructor for PagingLink.</p>
	 *
	 * @param aLink a {@link java.lang.String} object.
	 * @param aCaption a {@link java.lang.String} object.
	 */
	public PagingLink(String aLink, String aCaption){
		link = aLink;
		caption = aCaption;
	}
	
	/**
	 * <p>Constructor for PagingLink.</p>
	 *
	 * @param aCaption a {@link java.lang.String} object.
	 */
	public PagingLink(String aCaption){
		this(null, aCaption);
	}

	/**
	 * <p>Getter for the field <code>caption</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * <p>Setter for the field <code>caption</code>.</p>
	 *
	 * @param caption a {@link java.lang.String} object.
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * <p>Getter for the field <code>link</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getLink() {
		return link;
	}

	/**
	 * <p>Setter for the field <code>link</code>.</p>
	 *
	 * @param link a {@link java.lang.String} object.
	 */
	public void setLink(String link) {
		this.link = link;
	}
	
	/**
	 * <p>isLinked.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isLinked(){
		return link != null && link.length()>0;
	}
	

	
}
