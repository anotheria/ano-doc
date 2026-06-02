package net.anotheria.anodoc.service;

import net.anotheria.anodoc.data.DataHolder;
import net.anotheria.anodoc.data.Document;
import net.anotheria.anodoc.data.DocumentList;
import net.anotheria.anodoc.data.Module;

/**
 * This factory is responsible for creating new modules, if a service should be
 * able to create new modules.
 * It also should have the ability to create new Documents in the
 * Module Modell.
 *
 * @author another
 * @version $Id: $Id
 */
public interface IModuleFactory {
	/**
	 * Creates a new Module Instance for given owner and copy ids.
	 *
	 * @param ownerId a {@link java.lang.String} object.
	 * @param copyId a {@link java.lang.String} object.
	 * @return a {@link net.anotheria.anodoc.data.Module} object.
	 */
	Module createModule(String ownerId, String copyId);
	
	/**
	 * Creates a new Document with given name.
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link net.anotheria.anodoc.data.Document} object.
	 */
	Document createDocument(String name);
	
	/**
	 * Creates a new Document with given name and a type identifier.
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param typeIdentifier a {@link java.lang.String} object.
	 * @return a {@link net.anotheria.anodoc.data.Document} object.
	 */
	Document createDocument(String name, String typeIdentifier);
	
	/**
	 * Creates a new Document with given name in a given context.
	 * A context can be another document which holds the newly created document,
	 * or a list.
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param context a {@link net.anotheria.anodoc.data.DataHolder} object.
	 * @return a {@link net.anotheria.anodoc.data.Document} object.
	 */
	Document createDocument(String name, DataHolder context);
	
	/**
	 * Creates a new DocumentList with given name and without a context. Normally
	 * you don't need this method, since list is seldom overwritten.
	 *
	 * @param name a {@link java.lang.String} object.
	 * @return a {@link net.anotheria.anodoc.data.DocumentList} object.
	 */
	<D extends Document>DocumentList<D> createDocumentList(String name);
	/**
	 * Creates a new DocumentList with given name and context.
	 *
	 * @param name a {@link java.lang.String} object.
	 * @param context a {@link net.anotheria.anodoc.data.DataHolder} object.
	 * @return a {@link net.anotheria.anodoc.data.DocumentList} object.
	 */
	<D extends Document>DocumentList<D> createDocumentList(String name, DataHolder context);
	
}
