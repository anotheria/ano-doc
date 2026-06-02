package net.anotheria.asg.util.filter;

import java.util.List;

import net.anotheria.asg.data.DataObject;

/**
 * A filter which reduces the documents showed in the cms.
 *
 * @author another
 * @version $Id: $Id
 */
public interface DocumentFilter {
	/**
	 * Returns true if the document may pass the filtering defined by applying the filter parameter to the attribute
	 * with filters internal rule.
	 *
	 * @param document a {@link net.anotheria.asg.data.DataObject} object.
	 * @param attributeName a {@link java.lang.String} object.
	 * @param filterParameter a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	boolean mayPass(DataObject document, String attributeName, String filterParameter);
	
	/**
	 * <p>getTriggerer.</p>
	 *
	 * @param storedFilterParameter a {@link java.lang.String} object.
	 * @return the list of triggerers
	 */
	List<FilterTrigger> getTriggerer(String storedFilterParameter);
	
	
}
