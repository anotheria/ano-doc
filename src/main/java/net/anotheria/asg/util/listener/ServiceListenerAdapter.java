package net.anotheria.asg.util.listener;

import net.anotheria.asg.data.DataObject;
/**
 * An adapter class for ServiceListeners.
 *
 * @author another
 * @version $Id: $Id
 */
public abstract class ServiceListenerAdapter implements IServiceListener{

	/** {@inheritDoc} */
	@Override public void documentCreated(DataObject doc) {
	}

	/** {@inheritDoc} */
	@Override public void documentDeleted(DataObject doc) {
	}

	/** {@inheritDoc} */
	@Override public void documentUpdated(DataObject oldVersion, DataObject newVersion) {
	}

    /** {@inheritDoc} */
    @Override public void documentImported(DataObject doc) {
	}

	/** {@inheritDoc} */
	@Override public void persistenceChanged() {
	}

}
