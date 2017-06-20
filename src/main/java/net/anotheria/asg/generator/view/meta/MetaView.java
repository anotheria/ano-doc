package net.anotheria.asg.generator.view.meta;

import java.util.ArrayList;
import java.util.List;

import net.anotheria.asg.generator.IGenerateable;

/**
 * Meta description of the view.
 *
 * @author another
 * @version $Id: $Id
 */
public class MetaView implements IGenerateable{
	/**
	 * Name of the view.
	 */
	private String name;
	/**
	 * Flag for cms 2.0 version.
	 */
	private boolean cms20;
	
	//not used?
	//private boolean passwordProtected;
	/**
	 * Sections of the view.
	 */
	private List<MetaSection> sections;
	/**
	 * Title of the view.
	 */
	private String title;

	/**
	 * Creates a new MetaView.
	 *
	 * @param aName a {@link java.lang.String} object.
	 */
	public MetaView(String aName){
		name = aName;
		sections = new ArrayList<MetaSection>();
	}
	
// --- SOFAR NOT USED, HENCE OUTCOMMENTED 	
	/**
	 * @return
	 */
//	public boolean isPasswordProtected() {
	//	return passwordProtected;
	//}

	/**
	 * @param b
	 */
//	public void setPasswordProtected(boolean b) {
	//	passwordProtected = b;
	//}
//--- // END OUTCOMMENTED	
	
	/**
	 * Adds a section to this view.
	 *
	 * @param section a {@link net.anotheria.asg.generator.view.meta.MetaSection} object.
	 */
	public void addSection(MetaSection section){
		sections.add(section);	
	}

	/**
	 * <p>Getter for the field <code>sections</code>.</p>
	 *
	 * @return sections which are part of this view
	 */
	public List<MetaSection> getSections() {
		return sections;
	}

	/**
	 * <p>Setter for the field <code>sections</code>.</p>
	 *
	 * @param list sections of the view
	 */
	public void setSections(List<MetaSection> list) {
		sections = list;
	}

	/** {@inheritDoc} */
	@Override public String toString(){
		return "view "+name+", Sections: "+sections+" T: "+title;
	}
	/**
	 * <p>Getter for the field <code>name</code>.</p>
	 *
	 * @return name of the view
	 */
	public String getName() {
		return name;
	}

	/**
	 * <p>Getter for the field <code>title</code>.</p>
	 *
	 * @return title of the view
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * <p>Setter for the field <code>title</code>.</p>
	 *
	 * @param string title of the view
	 */
	public void setTitle(String string) {
		title = string;
	}

	/**
	 * <p>isCms20.</p>
	 *
	 * @return flag for cms 2.0 version
	 */
	public boolean isCms20() {
		return cms20;
	}

	/**
	 * <p>Setter for the field <code>cms20</code>.</p>
	 *
	 * @param cms20 flag for cms 2.0 version
	 */
	public void setCms20(boolean cms20) {
		this.cms20 = cms20;
	}

}
