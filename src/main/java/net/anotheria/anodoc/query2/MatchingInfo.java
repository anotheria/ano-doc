package net.anotheria.anodoc.query2;

/**
 * Used to present matches in the search page.
 *
 * @author another
 * @version $Id: $Id
 */
public interface MatchingInfo {
	/**
	 * Returns the html presentation of the match.
	 *
	 * @return a {@link java.lang.String} object.
	 */
	String toHtml();
}
