package net.anotheria.anodoc.query2;

import java.util.Collection;

import net.anotheria.util.StringUtils;

/**
 * Difference with QueryProperty is collection of possible values instead of single value.
 *
 * <b>IMPORTANT:<i>Tested only in postgressql!</i></b>
 *
 * @author denis
 * @version $Id: $Id
 */
public class QueryInProperty <T>extends QueryProperty{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8649073486730051958L;

	/**
	 * <p>Constructor for QueryInProperty.</p>
	 *
	 * @param aName a {@link java.lang.String} object.
	 * @param aValues a {@link java.util.Collection} object.
	 */
	public QueryInProperty(String aName, Collection<T> aValues){
		super(aName, aValues);
	}

	/** {@inheritDoc} */
	@Override
	public boolean doesMatch(Object o) {
		return o== null ?getOriginalValue() == null :
			getListValue().contains(o);
	}

	/** {@inheritDoc} */
	@Override
	public String getComparator() {
		return " IN ";
	}

	/** {@inheritDoc} */
	@Override
	public Object getValue() {
		Collection<T> values = getListValue(); 
		return StringUtils.surroundWith(StringUtils.concatenateTokens(values, ',', '\'', '\''), '(', ')');
	}
	
	@SuppressWarnings("unchecked")
	private Collection<T> getListValue(){
		return (Collection<T>) getOriginalValue();
	}

	/** {@inheritDoc} */
	@Override
	public boolean unprepaireable() {
		return true;
	}
	
	
}
