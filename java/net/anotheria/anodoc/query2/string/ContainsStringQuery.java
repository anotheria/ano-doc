package net.anotheria.anodoc.query2.string;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import net.anotheria.anodoc.data.Document;
import net.anotheria.anodoc.data.Property;
import net.anotheria.anodoc.data.StringProperty;
import net.anotheria.anodoc.query2.DocumentQuery;
import net.anotheria.anodoc.query2.QueryResultEntry;
import net.anotheria.asg.data.DataObject;

import org.apache.log4j.Logger;

/**
 * Implementation of DocumentQuery
 */
public class ContainsStringQuery implements DocumentQuery {

	/**
	 * Offset.
	 */
	public static final int OFFSET = 40;

	/**
	 * Logger.
	 */
	private static final Logger log = Logger.getLogger(ContainsStringQuery.class.getName());

	/**
	 * Any character constant
	 */
	public static final String ANY_CHAR_EXPRESSION = "?";
	/**
	 * Any character regular expression for replace()
	 */
	public static final String ANY_CHAR_REG_EXPRESSION = "\\?";

	/**
	 * Any string constant
	 */
	public static final String ANY_STRING_EXPRESSION = "*";
	/**
	 * Any string regular expression for replace()
	 */
	public static final String ANY_STRING_REG_EXPRESSION = "\\*";

	/**
	 * Search criteria
	 */
	private String criteria;

	/**
	 * Criteria in RegEx syntax.
	 */
	private Pattern criteriaRegExPattern;
	/**
	 * Criteria match part in RegEx syntax. Uses do find pre-match-post strings.
	 */
	private Pattern criteriaMatchRegExPattern;

	/**
	 * ContainsStringQuery constructor.
	 * 
	 * @param aCriteria
	 *            is expression, that supports {@link ANY_CHAR_EXPRESSION} and {@link ANY_STRING_EXPRESSION} characters
	 */
	public ContainsStringQuery(String aCriteria) {
		criteria = aCriteria;

		// Set criteriaRegEx
		String criteriaRegEx = aCriteria.replaceAll(ANY_CHAR_REG_EXPRESSION, ".").replaceAll(ANY_STRING_REG_EXPRESSION, ".*");

		// remove start end final ANY_STRING_EXPRESSIONs from criteria
		// to determine criteriaMatchRegEx
		String criteriaMatchRegEx = aCriteria.replaceAll(ANY_CHAR_REG_EXPRESSION, ".");
		if (criteriaMatchRegEx.startsWith(ANY_STRING_EXPRESSION)) {
			criteriaMatchRegEx = criteriaMatchRegEx.substring(ANY_STRING_EXPRESSION.length());
		}
		if (criteriaMatchRegEx.endsWith(ANY_STRING_EXPRESSION)) {
			criteriaMatchRegEx = criteriaMatchRegEx.substring(0, criteriaMatchRegEx.length() - ANY_STRING_EXPRESSION.length());
		}
		criteriaMatchRegEx = criteriaMatchRegEx.replaceAll(ANY_STRING_REG_EXPRESSION, ".*");

		// Compile patterns
		try {
			criteriaRegExPattern = Pattern.compile(criteriaRegEx, Pattern.CASE_INSENSITIVE);
			criteriaMatchRegExPattern = Pattern.compile(criteriaMatchRegEx, Pattern.CASE_INSENSITIVE);
		} catch (PatternSyntaxException e) {
			log.warn("PatternSyntaxException: " + e.getMessage());
			// Try to set regExPattern simply equals to aCriteria
			try {
				criteriaRegExPattern = Pattern.compile(aCriteria, Pattern.CASE_INSENSITIVE);
				criteriaMatchRegExPattern = criteriaRegExPattern;
			} catch (PatternSyntaxException e1) {
				log.warn("PatternSyntaxException: " + e.getMessage());
				// Should not happened
				criteriaRegExPattern = null;
				criteriaMatchRegExPattern = null;
			}
		}
	}

	/**
	 * Match method with simple expressions processing support.
	 */
	public List<QueryResultEntry> match(DataObject obj) {
		List<QueryResultEntry> ret = new ArrayList<QueryResultEntry>();

		if (!(obj instanceof Document) || (criteriaRegExPattern == null) || (criteriaMatchRegExPattern == null)) {
			return ret;
		}

		QueryResultEntry tempEntry;
		Document doc = (Document) obj;
		List<Property> properties = doc.getProperties();

		// search in id
		tempEntry = getMatchQueryResultEntry(new StringProperty("id", doc.getId()), doc);
		if (tempEntry != null) {
			ret.add(tempEntry);
		}

		// search in properties
		for (int i = 0; i < properties.size(); i++) {
			Property p = properties.get(i);
			if (p instanceof StringProperty) {
				tempEntry = getMatchQueryResultEntry((StringProperty) p, doc);
				if (tempEntry != null) {
					ret.add(tempEntry);
				}
			}
		}

		return ret;
	}

	/**
	 * Returns QueryResultEntry for given matchedProperty if it matches with {@link criteria}.
	 * 
	 * @param matchedProperty
	 *            property
	 * @param matchedDocument
	 *            document
	 */
	private QueryResultEntry getMatchQueryResultEntry(StringProperty matchedProperty, Document matchedDocument) {
		QueryResultEntry retEntry = null;

		if (matchedProperty != null) {
			String value = matchedProperty.getValue().toString();
			String pre;
			String match;
			String post;
			Matcher matchMatcher;

			// Check is value matches to pattern
			if (value != null && criteriaRegExPattern.matcher(value).matches()) {

				retEntry = new QueryResultEntry();
				retEntry.setMatchedDocument(matchedDocument);
				retEntry.setMatchedProperty(matchedProperty);

				// find pre-match-post parts
				matchMatcher = criteriaMatchRegExPattern.matcher(value);
				if (matchMatcher.find()) {
					pre = value.substring(0, matchMatcher.start());
					match = matchMatcher.group();
					post = value.substring(matchMatcher.end());
				} else {
					pre = "";
					match = value;
					post = "";
				}

				StringMatchingInfo info = new StringMatchingInfo(pre, match, post);
				retEntry.setInfo(info);
			}
		}

		return retEntry;
	}

	@Override
	public String toString() {
		return criteria;
	}

}
