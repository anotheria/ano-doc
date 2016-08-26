package net.anotheria.anodoc.query2;

import net.anotheria.anodoc.data.Document;
import net.anotheria.anodoc.data.Property;
import net.anotheria.util.BasicComparable;
import net.anotheria.util.sorter.IComparable;

/**
 * An entry in a query result list.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class QueryResultEntry implements IComparable{
	/**
	 * Link to the matched document.
	 */
	private Document matchedDocument;
	/**
	 * Link to the matched property.
	 */
	private Property matchedProperty;
	
	private int relevance;
	
	/**
	 * Info what matched and how.
	 */
	private MatchingInfo info;
	/**
	 * <p>Getter for the field <code>info</code>.</p>
	 *
	 * @return a {@link net.anotheria.anodoc.query2.MatchingInfo} object.
	 */
	public MatchingInfo getInfo() {
		return info;
	}
	/**
	 * <p>Setter for the field <code>info</code>.</p>
	 *
	 * @param info a {@link net.anotheria.anodoc.query2.MatchingInfo} object.
	 */
	public void setInfo(MatchingInfo info) {
		this.info = info;
	}
	/**
	 * <p>Getter for the field <code>matchedDocument</code>.</p>
	 *
	 * @return a {@link net.anotheria.anodoc.data.Document} object.
	 */
	public Document getMatchedDocument() {
		return matchedDocument;
	}
	/**
	 * <p>Setter for the field <code>matchedDocument</code>.</p>
	 *
	 * @param matchedDocument a {@link net.anotheria.anodoc.data.Document} object.
	 */
	public void setMatchedDocument(Document matchedDocument) {
		this.matchedDocument = matchedDocument;
	}
	/**
	 * <p>Getter for the field <code>matchedProperty</code>.</p>
	 *
	 * @return a {@link net.anotheria.anodoc.data.Property} object.
	 */
	public Property getMatchedProperty() {
		return matchedProperty;
	}
	/**
	 * <p>Setter for the field <code>matchedProperty</code>.</p>
	 *
	 * @param matchedProperty a {@link net.anotheria.anodoc.data.Property} object.
	 */
	public void setMatchedProperty(Property matchedProperty) {
		this.matchedProperty = matchedProperty;
	}
	
	/**
	 * <p>Getter for the field <code>relevance</code>.</p>
	 *
	 * @return a int.
	 */
	public int getRelevance() {
		return relevance;
	}
	/**
	 * <p>Setter for the field <code>relevance</code>.</p>
	 *
	 * @param relevance a int.
	 */
	public void setRelevance(int relevance) {
		this.relevance = relevance;
	}
	
	
	/** {@inheritDoc} */
	@Override public String toString(){
		return "Rel:" + relevance + ", doc: "+getMatchedDocument()+", prop: "+getMatchedProperty()+", matchinfo: "+getInfo();
	}
	/** {@inheritDoc} */
	@Override
	public int compareTo(IComparable anotherObject, int method) {
		QueryResultEntry anotherEntry = (QueryResultEntry)anotherObject;
		return BasicComparable.compareInt(relevance, anotherEntry.relevance);
	}
}
