package net.anotheria.asg.service;

import net.anotheria.asg.data.DataObject;

/**
 * <p>InMemoryObjectWrapper class.</p>
 *
 * @author another
 * @version $Id: $Id
 */
public class InMemoryObjectWrapper <T extends DataObject> {
	/**
	 * The wrapped object.
	 */
	private T t ;
	
	/**
	 * True if it's a newly created object.
	 */
	private boolean created;
	/**
	 * True if it's an updated object.
	 */
	private boolean updated;
	/**
	 * True if it's a deleted object.
	 */
	private boolean deleted;
	/**
	 * Timestamp of object creation.
	 */
	private long createdTimestamp;
	/**
	 * Timestamp of object update.
	 */
	private long updatedTimestamp;
	/**
	 * Timestmap of object deletion.
	 */
	private long deletedTimestamp;
	
	/**
	 * <p>Constructor for InMemoryObjectWrapper.</p>
	 *
	 * @param aT a T object.
	 */
	public InMemoryObjectWrapper(T aT){
		this(aT, false);
	}
	
	/**
	 * <p>Constructor for InMemoryObjectWrapper.</p>
	 *
	 * @param aT a T object.
	 * @param created a boolean.
	 */
	public InMemoryObjectWrapper(T aT, boolean created){
		t = aT;
		this.created = created;
		createdTimestamp = System.currentTimeMillis();
	}

	/**
	 * <p>getId.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getId(){
		return t.getId();
	}
	
	/**
	 * <p>get.</p>
	 *
	 * @return a T object.
	 */
	public T get(){
		return t;
	}
	
	/**
	 * <p>update.</p>
	 *
	 * @param aT a T object.
	 */
	public void update(T aT){
		t = aT;
		updated = true;
		updatedTimestamp = System.currentTimeMillis();
	}
	
	/**
	 * <p>delete.</p>
	 */
	public void delete(){
		t = null;
		deleted = true;
		deletedTimestamp = System.currentTimeMillis();
	}
	
	/**
	 * <p>toString.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String toString(){
		return ""+t;
	}

	/**
	 * <p>isCreated.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isCreated() {
		return created;
	}

	/**
	 * <p>isUpdated.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isUpdated() {
		return updated;
	}

	/**
	 * <p>isDeleted.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * <p>Getter for the field <code>createdTimestamp</code>.</p>
	 *
	 * @return a long.
	 */
	public long getCreatedTimestamp() {
		return createdTimestamp;
	}

	/**
	 * <p>Getter for the field <code>updatedTimestamp</code>.</p>
	 *
	 * @return a long.
	 */
	public long getUpdatedTimestamp() {
		return updatedTimestamp;
	}

	/**
	 * <p>Getter for the field <code>deletedTimestamp</code>.</p>
	 *
	 * @return a long.
	 */
	public long getDeletedTimestamp() {
		return deletedTimestamp;
	}
	
	
}
