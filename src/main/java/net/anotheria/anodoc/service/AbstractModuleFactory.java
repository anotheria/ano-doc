package net.anotheria.anodoc.service;

import net.anotheria.anodoc.data.DataHolder;
import net.anotheria.anodoc.data.Document;
import net.anotheria.anodoc.data.DocumentList;
import net.anotheria.anodoc.data.IDHolder;
import net.anotheria.anodoc.data.Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Base imlementation of IModuleFactory interface which only
 * demands createModule method.
 *
 * @author another
 * @version $Id: $Id
 */
public abstract class AbstractModuleFactory implements IModuleFactory, Serializable{
	
	private static final long serialVersionUID=1580293076199614251L;
	
	/**
	 * {@link Logger} instance.
	 */
	private Logger log;
	
	/**
	 * <p>Constructor for AbstractModuleFactory.</p>
	 */
	protected AbstractModuleFactory(){
		log = LoggerFactory.getLogger(this.getClass());
	}
	
	/**
	 * <p>Getter for the field <code>log</code>.</p>
	 *
	 * @return a {@link org.slf4j.Logger} object.
	 */
	protected Logger getLog(){
		return log;
	}
	/**
	 * Creates a new Document from a name and a context (like list). If this function is not overwritten, it call createDocument(name).
	 *
	 * @return new Document
	 * @param name a {@link java.lang.String} object.
	 * @param context a {@link net.anotheria.anodoc.data.DataHolder} object.
	 */
	public Document createDocument(String name, DataHolder context) {
		getLog().debug("This Factory doesn't overwrite create document with context (Doc:"+name+", context:"+context+")"); 
		getLog().debug("will call createDocument(name) instead.");
		return createDocument(name);
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * Creates a new Document from a name and a typeidentifier. If this function is not overwritten, it call createDocument(name).
	 * @return new Document
	 */
	public Document createDocument(String name, String typeIdentifier){
		getLog().debug("This Factory doesn't overwrite create document with identifier (Doc:"+name+", identifier:"+typeIdentifier+")"); 
		getLog().debug("will call createDocument(name) instead.");
		return createDocument(name);
	}


	/**
	 * {@inheritDoc}
	 *
	 * Creates and returns a new Document. This method should be overwritten by the extending class,
	 * since usage of Document directly in your modell is not fitting in the concept of ano-doc.
	 */
	@Override public Document createDocument(String id) {
		if (id.startsWith(IDHolder.DOC_ID_HOLDER_PRE))
			return new IDHolder(id);
		
		getLog().debug("This Factory doesn't overwrite create document (docname:"+id+")");
		return new Document(id);
	}

	/**
	 * {@inheritDoc}
	 *
	 * Creates and returns a DocumentList.
	 */
	public <D extends Document> DocumentList<D> createDocumentList(String name, DataHolder context) {
		getLog().debug("This Factory doesn't overwrite create document list(listname:"+name+")");
		return new DocumentList<D>(name);
	}

	/**
	 * {@inheritDoc}
	 *
	 * Creates and returns a DocumentList.
	 */
	public <D extends Document> DocumentList<D> createDocumentList(String name) {
		getLog().debug("This Factory doesn't overwrite create document list(listname:"+name+")");
		return new DocumentList<D>(name);
	}


	/** {@inheritDoc} */
	@Override public final Module createModule(String ownerId, String copyId) {
		Module module = recreateModule(ownerId, copyId);
		module.setOwnerId(ownerId);
		module.setCopyId(copyId);
		return module;
	}
	
	/**
	 * Recreates a Module. This method is called by the AbstractModuleFactory in
	 * the createModule method. It sets the proper owner and copy ids in the newly
	 * created Module instance, so the extending class doesn't need to do it itself.
	 *
	 * @return recreated module
	 * @param ownerId a {@link java.lang.String} object.
	 * @param copyId a {@link java.lang.String} object.
	 */
	public abstract Module recreateModule(String ownerId, String copyId);
	

}
