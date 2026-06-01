package net.anotheria.anodoc.query2;

import java.util.ArrayList;
import java.util.List;
/**
 * The result of the query.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class QueryResult {
	/**
	 * Entries which are part of the QueryResult.
	 */
	private List<QueryResultEntry> entries;
	/**
	 * Creates a new query result.
	 */
	public QueryResult(){
		entries = new ArrayList<QueryResultEntry>();
	}
	
	/**
	 * Adds a result entry.
	 *
	 * @param entry a {@link net.anotheria.anodoc.query2.QueryResultEntry} object.
	 */
	public void add(QueryResultEntry entry){
		entries.add(entry);
	}
	/**
	 * Adds some result entries.
	 *
	 * @param someEntries a {@link java.util.List} object.
	 */
	public void add(List<QueryResultEntry> someEntries){
		entries.addAll(someEntries);
	}
	/**
	 * Returns the entries.
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<QueryResultEntry> getEntries(){
		return entries;
	}
	
	/** {@inheritDoc} */
	@Override public String toString(){
		return "QueryResult with "+entries.size()+" entries: \n"+entries;
	}
}
