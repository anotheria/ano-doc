package net.anotheria.asg.generator.meta;

import java.util.List;

/**
 * Representation of an internal link to another document.
 *
 * @author another
 * @version $Id: $Id
 */
public class MetaLink extends MetaProperty{
	
	/**
	 * Type of the link. Currently the only supported link type is single.
	 */
	private String linkType;
	/**
	 * Link target as relative or absolute document name.
	 */
	private String linkTarget;
	
	/**
	 * Properties of target document are used to decorate link in additional to ID
	 */
	private List<String> linkDecoration;
	
	/**
	 * <p>Constructor for MetaLink.</p>
	 *
	 * @param name a {@link java.lang.String} object.
	 */
	public MetaLink(String name){
		super(name, MetaProperty.Type.STRING);
	}
	/**
	 * <p>Getter for the field <code>linkTarget</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getLinkTarget() {
		return linkTarget;
	}

	/**
	 * <p>Getter for the field <code>linkType</code>.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getLinkType() {
		return linkType;
	}

	/**
	 * <p>Setter for the field <code>linkTarget</code>.</p>
	 *
	 * @param string a {@link java.lang.String} object.
	 */
	public void setLinkTarget(String string) {
		linkTarget = string;
	}

	/**
	 * <p>Setter for the field <code>linkType</code>.</p>
	 *
	 * @param string a {@link java.lang.String} object.
	 */
	public void setLinkType(String string) {
		linkType = string;
	}

	/* (non-Javadoc)
	 * @see net.anotheria.anodoc.generator.meta.MetaProperty#toNameConstant()
	 */
	/** {@inheritDoc} */
	@Override public String toNameConstant() {
		return "LINK_"+super.toNameConstant();
	}
	
	/** {@inheritDoc} */
	@Override public boolean isLinked(){
		return true;
	}
	
	/**
	 * <p>getTargetModuleName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTargetModuleName(){
		int index = getLinkTarget().indexOf('.');
		return index == -1 ? null : getLinkTarget().substring(0, index);
	}

	/**
	 * <p>getTargetDocumentName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	public String getTargetDocumentName(){
		return getLinkTarget().substring(getLinkTarget().indexOf('.')+1);
	}
	
	/**
	 * <p>doesTargetMatch.</p>
	 *
	 * @param document a {@link net.anotheria.asg.generator.meta.MetaDocument} object.
	 * @return a boolean.
	 */
	public boolean doesTargetMatch(MetaDocument document){
		return doesTargetMath(document.getParentModule(), document);
	}
	
	/**
	 * <p>doesTargetMath.</p>
	 *
	 * @param module a {@link net.anotheria.asg.generator.meta.MetaModule} object.
	 * @param document a {@link net.anotheria.asg.generator.meta.MetaDocument} object.
	 * @return a boolean.
	 */
	public boolean doesTargetMath(MetaModule module, MetaDocument document){
		return linkTarget != null && linkTarget.equals(module.getName()+"."+document.getName());
	}
	
	/**
	 * <p>isRelative.</p>
	 *
	 * @return a boolean.
	 */
	public boolean isRelative(){
		return getLinkTarget().indexOf('.') == -1;
	}
	/**
	 * <p>Getter for the field <code>linkDecoration</code>.</p>
	 *
	 * @return a {@link java.util.List} object.
	 */
	public List<String> getLinkDecoration() {
		return linkDecoration;
	}
	/**
	 * <p>Setter for the field <code>linkDecoration</code>.</p>
	 *
	 * @param linkDecoration a {@link java.util.List} object.
	 */
	public void setLinkDecoration(List<String> linkDecoration) {
		this.linkDecoration = linkDecoration;
	}

}
