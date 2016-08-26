package net.anotheria.asg.util.helper.cmsview;

import net.anotheria.asg.data.DataObject;

/**
 * <p>CMSViewHelper interface.</p>
 *
 * @author another
 * @version $Id: $Id
 */
public interface CMSViewHelper {
	/**
	 * <p>getFieldExplanation.</p>
	 *
	 * @param documentPath Module.Document from the data definition
	 * @param object the edited object (if any)
	 * @param property the property of the object which should be explained
	 * @return a {@link java.lang.String} object.
	 */
	public String getFieldExplanation(String documentPath, DataObject object, String property);
		
	
}
