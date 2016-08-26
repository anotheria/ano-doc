package net.anotheria.anodoc.query2;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.anotheria.anodoc.data.Property;
import net.anotheria.util.StringUtils;

/**
 * Query to List/Array Property. Passes only if Property contains all queried values.
 *
 * <b>IMPORTANT:<i>Not properly tested yet. Use on own risk!!!!</i></b>
 *
 * @author denis
 * @version $Id: $Id
 */
public class QueryContainsProperty <T>extends QueryProperty{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8649073486730051958L;
	
	/**
	 * <p>Constructor for QueryContainsProperty.</p>
	 *
	 * @param aName a {@link java.lang.String} object.
	 * @param aValues a T object.
	 */
	public QueryContainsProperty(String aName, T... aValues){
		this(aName, Arrays.asList(aValues));
	}

	/**
	 * <p>Constructor for QueryContainsProperty.</p>
	 *
	 * @param aName a {@link java.lang.String} object.
	 * @param aValues a {@link java.util.Collection} object.
	 */
	public QueryContainsProperty(String aName, Collection<T> aValues){
		super(aName, aValues);
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public boolean doesMatch(Object o) {
		if(o== null)
			return getOriginalValue() == null;
		List<Property> properties = ((List<Property>)o);
		Set<Object> toCompare = new HashSet<Object>();
		for(Property p: properties)
			toCompare.add(p.getValue());
		
		return toCompare.containsAll(getListValue());
	}

	/** {@inheritDoc} */
	@Override
	public String getComparator() {
		return " @> ";
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
