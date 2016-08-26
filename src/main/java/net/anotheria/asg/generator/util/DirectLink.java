package net.anotheria.asg.generator.util;

import net.anotheria.asg.generator.meta.MetaDocument;
import net.anotheria.asg.generator.meta.MetaLink;
import net.anotheria.asg.generator.meta.MetaModule;

/**
 * A link to a target document.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class DirectLink {
	/**
	 * Source module.
	 */
	private MetaModule module;
	/**
	 * Source document.
	 */
	private MetaDocument document;
	/**
	 * Link in the source document.
	 */
	private MetaLink property;
	
	/**
	 * <p>Constructor for DirectLink.</p>
	 */
	public DirectLink(){
		
	}
	
	/**
	 * <p>Constructor for DirectLink.</p>
	 *
	 * @param aModule a {@link net.anotheria.asg.generator.meta.MetaModule} object.
	 * @param aDocument a {@link net.anotheria.asg.generator.meta.MetaDocument} object.
	 * @param aProperty a {@link net.anotheria.asg.generator.meta.MetaLink} object.
	 */
	public DirectLink(MetaModule aModule, MetaDocument aDocument, MetaLink aProperty){
		module = aModule;
		document = aDocument;
		property = aProperty;
	}
	
	/**
	 * <p>Getter for the field <code>module</code>.</p>
	 *
	 * @return a {@link net.anotheria.asg.generator.meta.MetaModule} object.
	 */
	public MetaModule getModule() {
		return module;
	}
	/**
	 * <p>Setter for the field <code>module</code>.</p>
	 *
	 * @param module a {@link net.anotheria.asg.generator.meta.MetaModule} object.
	 */
	public void setModule(MetaModule module) {
		this.module = module;
	}
	/**
	 * <p>Getter for the field <code>document</code>.</p>
	 *
	 * @return a {@link net.anotheria.asg.generator.meta.MetaDocument} object.
	 */
	public MetaDocument getDocument() {
		return document;
	}
	/**
	 * <p>Setter for the field <code>document</code>.</p>
	 *
	 * @param document a {@link net.anotheria.asg.generator.meta.MetaDocument} object.
	 */
	public void setDocument(MetaDocument document) {
		this.document = document;
	}
	/**
	 * <p>Getter for the field <code>property</code>.</p>
	 *
	 * @return a {@link net.anotheria.asg.generator.meta.MetaLink} object.
	 */
	public MetaLink getProperty() {
		return property;
	}
	/**
	 * <p>Setter for the field <code>property</code>.</p>
	 *
	 * @param property a {@link net.anotheria.asg.generator.meta.MetaLink} object.
	 */
	public void setProperty(MetaLink property) {
		this.property = property;
	}
}
