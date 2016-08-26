package net.anotheria.anodoc.query2;

/**
 * <p>QueryStartsWithProperty class.</p>
 *
 * @author another
 * @version $Id: $Id
 */
public class QueryStartsWithProperty extends QueryProperty{
	/**
	 * <p>Constructor for QueryStartsWithProperty.</p>
	 *
	 * @param aName a {@link java.lang.String} object.
	 * @param aValue a {@link java.lang.Object} object.
	 */
	public QueryStartsWithProperty(String aName, Object aValue){
		super(aName, aValue);
	}
	
	/**
	 * <p>getComparator.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getComparator(){
		return " like ";
	}

	/** {@inheritDoc} */
	@Override
	public Object getValue() {
		return super.getValue()+"%";
	}

	
	/** {@inheritDoc} */
	public boolean doesMatch(Object o){
		return o== null ?getOriginalValue() == null :
			o.toString().startsWith(getOriginalValue().toString());
	}

}
