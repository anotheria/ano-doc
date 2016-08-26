package net.anotheria.anodoc.query2;

/**
 * Case-insensitive version of {@link net.anotheria.anodoc.query2.QueryLikeProperty}.
 *
 * @author another
 * @version $Id: $Id
 */
public class QueryCaseInsensitiveLikeProperty extends QueryLikeProperty {

    /**
     * Constructor.
     *
     * @param aName  property name
     * @param aValue property value
     */
    public QueryCaseInsensitiveLikeProperty(String aName, Object aValue) {
        super(aName, aValue);
    }

    /** {@inheritDoc} */
    @Override
    public String getComparator() {
        return " ilike ";
    }

    /** {@inheritDoc} */
    @Override
    public boolean doesMatch(Object o) {
        return o == null ? getOriginalValue() == null :
                o.toString().toLowerCase().indexOf(getOriginalValue().toString().toLowerCase()) != -1;
    }

}
