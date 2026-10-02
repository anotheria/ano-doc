package net.anotheria.asg.service;

import net.anotheria.anoprise.metafactory.Service;
import net.anotheria.asg.exception.ASGRuntimeException;
import net.anotheria.asg.util.listener.IServiceListener;

import java.util.List;
import java.util.Map;

/**
 * Interface for the basic ASGService.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public interface ASGService extends Service{
	/**
	 * Adds a service listener to this service.
	 *
	 * @param listener the listener to add.
	 */
	void addServiceListener(IServiceListener listener);

	/**
	 * Removes the service listener from the service.
	 *
	 * @param listener the listener to remove.
	 */
	void removeServiceListener(IServiceListener listener);

	/**
	 * Returns true if there are service listeners connected to this service.
	 *
	 * @return true if there are service listeners attached.
	 */
	boolean hasServiceListeners();

	/** Purges a language from all objects. */
	void purgeLanguageFromAllObjects(String language) throws ASGRuntimeException;

	/**
	 * Remembers on documents of this module that they were transferred to another instance.
	 *
	 * <p>Called by the transfer engine after a transfer reached every target of a group, so the cms can show
	 * the editor when a document was last published next to when it was last changed. Implementations store
	 * the timestamp <b>without</b> touching the last update timestamp, the author or the footprint, and
	 * without firing an update event: the event would start the next transfer, which would write the timestamp
	 * again.
	 *
	 * <p>Takes all documents of one transfer at once because storing them means storing the module, and a deep
	 * transfer of a few hundred documents would otherwise write the whole module a few hundred times.
	 *
	 * <p>Only documents in the cms carry a transfer timestamp. Other storage types ignore the call, which is
	 * why this is a default method and not something every service has to implement.
	 *
	 * @param idsByDocumentName ids of the transferred documents, by document type as written in the datadef.
	 * @param timestamp         timestamp of the transfer.
	 */
	default void markDocumentsTransferred(Map<String, List<String>> idsByDocumentName, long timestamp) {
		//documents of this service don't carry a transfer timestamp.
	}
}
