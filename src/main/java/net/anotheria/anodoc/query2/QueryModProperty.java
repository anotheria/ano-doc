package net.anotheria.anodoc.query2;


/**
 *
 * <b>IMPORTANT:<i>Tested only in postgressql!</i></b>
 *
 * @author denis
 * @version $Id: $Id
 */
public class QueryModProperty extends QueryProperty{
	
	private long mod;
	
	/**
	 * <p>Constructor for QueryModProperty.</p>
	 *
	 * @param aName a {@link java.lang.String} object.
	 * @param aMod a long.
	 * @param aValue a long.
	 * @param <T> a T object.
	 */
	public <T> QueryModProperty(String aName, long aMod, long aValue){
		super(aName, aValue);
		mod = aMod;
		
	}

	/** {@inheritDoc} */
	@Override
	public boolean doesMatch(Object o) {
		return o == null ?getOriginalValue() == null :
			((Long)o) % mod == (Long)getValue();
	}

	/** {@inheritDoc} */
	@Override
	public String getComparator() {
		return " %  " + mod + " = ";
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean unprepaireable() {
		return false;
	}

	/**
	 * <p>Getter for the field <code>mod</code>.</p>
	 *
	 * @return a long.
	 */
	public long getMod() {
		return mod;
	}

	/**
	 * <p>Setter for the field <code>mod</code>.</p>
	 *
	 * @param mod a long.
	 */
	public void setMod(long mod) {
		this.mod = mod;
	}
	
	
}
