package net.anotheria.asg.data;

import net.anotheria.anodoc.data.BooleanProperty;
import net.anotheria.anodoc.data.Document;
import net.anotheria.anodoc.data.LongProperty;
import net.anotheria.anodoc.data.NoSuchPropertyException;
import net.anotheria.anodoc.data.Property;
import net.anotheria.anodoc.data.StringProperty;

/**
 * Root object for all generated classes of type Document (instead of ano-doc Document used previously).
 *
 * @author another
 * @version $Id: $Id
 */
public abstract class AbstractASGDocument extends Document implements DataObject, LockableObject{
	
	/** Constant <code>INT_PROPERTY_MULTILINGUAL_DISABLED="ml-disabled"</code> */
	protected static final String INT_PROPERTY_MULTILINGUAL_DISABLED = "ml-disabled";

	/**
	 * Name of the internal property the timestamp of the last transfer to another instance is kept under.
	 *
	 * <p>It is an internal property and not a generated one for two reasons: it is not content, so it must not
	 * change the document's footprint, and it is not an edit, so writing it must not look like one. See
	 * {@link #setLastTransferTimestamp(long)}.
	 */
	protected static final String INT_PROPERTY_LAST_TRANSFER = "last-transfer";
	
	/**
	 * <p>Constructor for AbstractASGDocument.</p>
	 *
	 * @param anId a {@link java.lang.String} object.
	 */
	protected AbstractASGDocument(String anId){
		super(anId);
	}
	
	/**
	 * <p>Constructor for AbstractASGDocument.</p>
	 *
	 * @param toClone a {@link net.anotheria.asg.data.AbstractASGDocument} object.
	 */
	protected AbstractASGDocument(AbstractASGDocument toClone){
		super(toClone);
	}

	public abstract String getFootprint();

	/**
	 * When this document was last transferred to another instance.
	 *
	 * @return timestamp of the last transfer, 0 if the document was never transferred from here.
	 */
	public long getLastTransferTimestamp(){
		try{
			return ((LongProperty) getInternalProperty(INT_PROPERTY_LAST_TRANSFER)).getlong();
		}catch(NoSuchPropertyException e){
			return 0;
		}
	}

	/**
	 * Remembers that this document went out to another instance.
	 *
	 * <p>Deliberately not an edit: it leaves the last update timestamp, the author and the footprint alone, so
	 * the footer keeps telling the editor when the document was last <i>changed</i> next to when it was last
	 * <i>published</i>. The transfer engine writes it through
	 * {@code ASGService.markDocumentsTransferred(Map, long)}, which stores the document without firing an
	 * update event - firing one would send the document out again, which would write the timestamp again,
	 * forever.
	 *
	 * @param aTimestamp timestamp of the transfer.
	 */
	public void setLastTransferTimestamp(long aTimestamp){
		setInternalProperty(new LongProperty(INT_PROPERTY_LAST_TRANSFER, aTimestamp));
	}

	/** {@inheritDoc} */
	@Override public ObjectInfo getObjectInfo(){
		ObjectInfo ret = new ObjectInfo(this);
		ret.setId(getId());
		ret.setAuthor(getAuthor());
		ret.setLastChangeTimestamp(getLastUpdateTimestamp());
		ret.setFootprint(getFootprint());
		ret.setLastTransferTimestamp(getLastTransferTimestamp());
		return ret;
	}
	
	/**
	 * <p>getInternalProperty.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link net.anotheria.anodoc.data.Property} object.
	 */
	protected Property getInternalProperty(String name){
		return getProperty(getInternalPropertyName(name));
	}
	
	/**
	 * <p>setInternalProperty.</p>
	 *
	 * @param p a {@link net.anotheria.anodoc.data.Property} object.
	 */
	protected void setInternalProperty(Property p){
		try{
			Property toPut = p.cloneAs(getInternalPropertyName(p.getId()));
			putProperty(toPut);
		}catch(CloneNotSupportedException e){
			throw new IllegalArgumentException("Property not cloneable: "+p+", clazz: "+p.getClass());
		}
	}

	/**
	 * Returns the name for internally used asg-related property with given name.
	 * @param name
	 * @return
	 */
	private String getInternalPropertyName(String name){
		return "-asg-"+name+"-asg-";
	}

    /** {@inheritDoc} */
    @Override
    public boolean isLocked() {
        try {
            return ((BooleanProperty) getInternalProperty(INT_LOCK_PROPERTY_NAME)).getboolean();
        } catch (NoSuchPropertyException e) {
            return false;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setLocked(boolean aLock) {
        setInternalProperty(new BooleanProperty(INT_LOCK_PROPERTY_NAME, aLock));
    }

    /** {@inheritDoc} */
    @Override
    public String getLockerId() {
        try {
            return ((StringProperty) getInternalProperty(INT_LOCKER_ID_PROPERTY_NAME)).getString();
        } catch (NoSuchPropertyException e) {
            return null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setLockerId(String aLockerId) {
        setInternalProperty(new StringProperty(INT_LOCKER_ID_PROPERTY_NAME, aLockerId));
    }

    /** {@inheritDoc} */
    @Override
    public long getLockingTime() {
        try {
            return ((LongProperty) getInternalProperty(INT_LOCKING_TIME_PROPERTY_NAME)).getlong();
        } catch (NoSuchPropertyException e) {
            return 0;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void setLockingTime(long aLockTime) {
        setInternalProperty(new LongProperty(INT_LOCKING_TIME_PROPERTY_NAME, aLockTime));
    }
}
