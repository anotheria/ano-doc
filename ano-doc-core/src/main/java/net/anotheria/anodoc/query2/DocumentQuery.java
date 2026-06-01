package net.anotheria.anodoc.query2;

import java.util.List;

import net.anotheria.asg.data.DataObject;

/**
 * <p>DocumentQuery interface.</p>
 *
 * @author another
 * @version $Id: $Id
 */
public interface DocumentQuery {
	/**
	 * Returns a list of possible results for an object.
	 *
	 * @param doc a {@link net.anotheria.asg.data.DataObject} object.
	 * @return a {@link java.util.List} object.
	 */
	List<QueryResultEntry> match(DataObject doc);
}
