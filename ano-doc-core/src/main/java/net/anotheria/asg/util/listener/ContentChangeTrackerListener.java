package net.anotheria.asg.util.listener;

import net.anotheria.asg.data.DataObject;
import net.anotheria.asg.util.CmsChangesTracker;
import net.anotheria.asg.util.DocumentChange;
import net.anotheria.asg.util.CmsChangesTracker.Action;
/**
 * <p>ContentChangeTrackerListener class.</p>
 *
 * @version $Id: $Id
 */
public class ContentChangeTrackerListener implements IServiceListener{
	
	/** {@inheritDoc} */
	@Override public void documentCreated(DataObject doc) {
		trackChanges(doc, Action.CREATE);
	}

    /** {@inheritDoc} */
    @Override
    public void documentImported(DataObject doc) {
    	trackChanges(doc, Action.IMPORT);
    }

    /** {@inheritDoc} */
    @Override public void documentDeleted(DataObject doc) {
    	trackChanges(doc, Action.DELETE);
	}

	/** {@inheritDoc} */
	@Override public void documentUpdated(DataObject oldVersion, DataObject newVersion) {
		trackChanges(newVersion, Action.UPDATE);
	}
	
	private void trackChanges(DataObject doc, Action action){
		DocumentChange dc = new DocumentChange();
		dc.setAction(action);
		dc.setDocumentName(doc.getDefinedName());
		dc.setParentName(doc.getDefinedParentName().toLowerCase());
		dc.setTimestamp(doc.getLastUpdateTimestamp());
		dc.setUserName(doc.getObjectInfo().getAuthor());
		dc.setId(doc.getId());
		
		CmsChangesTracker.saveChange(dc);
		
	}

	/** {@inheritDoc} */
	@Override
	public void persistenceChanged() {
		//nothing todo here
	}
	
}

