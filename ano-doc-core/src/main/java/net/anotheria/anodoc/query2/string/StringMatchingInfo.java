package net.anotheria.anodoc.query2.string;

import net.anotheria.anodoc.query2.MatchingInfo;
import net.anotheria.util.CharacterEntityCoder;

/**
 * <p>StringMatchingInfo class.</p>
 *
 * @author another
 * @version $Id: $Id
 */
public class StringMatchingInfo implements MatchingInfo{
	/**
	 * Part of the string prior to the matched part.
	 */
	private String pre;
	/**
	 * Part of the string past matched part.
	 */
	private String post;
	/**
	 * Matched part.
	 */
	private String match;
	/**
	 * Creates a new StringMatchingInfo.
	 */
	public StringMatchingInfo(){
		
	}
	
	/**
	 * Creates a new StringMatchingInfo.
	 *
	 * @param aPre a {@link java.lang.String} object.
	 * @param aMatch a {@link java.lang.String} object.
	 * @param aPost a {@link java.lang.String} object.
	 */
	public StringMatchingInfo(String aPre, String aMatch, String aPost){
		pre = aPre;
		match = aMatch;
		post = aPost;
	}
	
	/**
	 * <p>Getter for the field <code>match</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getMatch() {
		return match;
	}



	/**
	 * <p>Setter for the field <code>match</code>.</p>
	 *
	 * @param match a {@link java.lang.String} object.
	 */
	public void setMatch(String match) {
		this.match = match;
	}



	/**
	 * <p>Getter for the field <code>post</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getPost() {
		return post;
	}



	/**
	 * <p>Setter for the field <code>post</code>.</p>
	 *
	 * @param post a {@link java.lang.String} object.
	 */
	public void setPost(String post) {
		this.post = post;
	}



	/**
	 * <p>Getter for the field <code>pre</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getPre() {
		return pre;
	}



	/**
	 * <p>Setter for the field <code>pre</code>.</p>
	 *
	 * @param pre a {@link java.lang.String} object.
	 */
	public void setPre(String pre) {
		this.pre = pre;
	}



	/** {@inheritDoc} */
	@Override public String toHtml(){
		String ret = "";
		ret += "<i>"+CharacterEntityCoder.htmlEncodeString(pre)+"</i>";
		ret += "<b>"+CharacterEntityCoder.htmlEncodeString(match)+"</b>";
		ret += "<i>"+CharacterEntityCoder.htmlEncodeString(post)+"</i>";
		return ret;
	}
	
	/** {@inheritDoc} */
	@Override public String toString(){
		return toHtml();
	}
}
