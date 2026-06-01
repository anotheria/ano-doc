package net.anotheria.asg.util.helper.cmsview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Container for the view helpers.
 *
 * @author lrosenberg
 * @version $Id: $Id
 */
public class CMSViewHelperRegistry {
	/**
	 * A map with the helpers.
	 */
	private static Map<String, List<CMSViewHelper>> helperMap;
	/**
	 * An empty list as null object.
	 */
	private static final ArrayList<CMSViewHelper> EMPTY_LIST = new ArrayList<CMSViewHelper>(0);

	private CMSViewHelperRegistry() {
	}

	static{
		helperMap = Collections.synchronizedMap(new HashMap<String, List<CMSViewHelper>>());
	}
	
	/**
	 * <p>addCMSViewHelper.</p>
	 *
	 * @param documentPath a {@link java.lang.String} object.
	 * @param helper a {@link net.anotheria.asg.util.helper.cmsview.CMSViewHelper} object.
	 */
	public static void addCMSViewHelper(String documentPath, CMSViewHelper helper){
		List<CMSViewHelper> helpers = helperMap.get(documentPath);
		if (helpers==null){
			helpers = new ArrayList<CMSViewHelper>();
			helperMap.put(documentPath, helpers);
		}
		helpers.add(helper);
		
	}
	
	/**
	 * <p>getCMSViewHelpers.</p>
	 *
	 * @param documentPath a {@link java.lang.String} object.
	 * @return a {@link java.util.List} object.
	 */
	public static List<CMSViewHelper> getCMSViewHelpers(String documentPath){
		List<CMSViewHelper> ret = helperMap.get(documentPath);
		return ret == null ? EMPTY_LIST : ret;
	}
}
