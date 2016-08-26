package net.anotheria.asg.util;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * <p>CmsChangesTracker class.</p>
 *
 * @author another
 * @version $Id: $Id
 */
public class CmsChangesTracker {

	private CmsChangesTracker() {
	}

	/** Constant <code>TRACING_SIZE=20</code> */
	public static final int TRACING_SIZE = 20;
	
	public static enum Action{
		CREATE,
		UPDATE,
		IMPORT,
		DELETE;
		public String getShortName(){
			return name().charAt(0) + "";
		}
	}
	
	private static List<DocumentChange> history = new CopyOnWriteArrayList<DocumentChange>();
	
	/**
	 * <p>saveChange.</p>
	 *
	 * @param documentChanges a {@link net.anotheria.asg.util.DocumentChange} object.
	 */
	public static void saveChange(DocumentChange documentChanges){
		history.add(0,documentChanges);
		if(history.size() > TRACING_SIZE)
			history.remove(history.size() - 1);
	}
	
	/**
	 * <p>getChanges.</p>
	 *
	 * @return a {@link java.util.Collection} object.
	 */
	public static Collection<DocumentChange> getChanges(){
		return history;
	}
	
}
