package net.anotheria.asg.util.decorators;

import net.anotheria.anodoc.data.Document;
import net.anotheria.anodoc.data.NoSuchPropertyException;
import net.anotheria.anodoc.data.StringProperty;

/**
 * This decorator decorates a link value with the linked document name.
 *
 * @author another
 * @version $Id: $Id
 */
public abstract class AbstractLinkDecorator implements IAttributeDecorator{

	/* (non-Javadoc)
	 * @see net.anotheria.asg.util.decorators.IAttributeDecorator#decorate(net.anotheria.anodoc.data.Document, java.lang.String, java.lang.String)
	 */
	/** {@inheritDoc} */
	public String decorate(Document doc, String attributeName, String rule) {
		try{
			String id = ((StringProperty)doc.getProperty(attributeName)).getString();
			return getName(id)+" ["+id+"]";
		}catch(NoSuchPropertyException e1){
			return "";
		}catch(Exception e){
			return "Unknown ["+e.getMessage()+"]";
		}
	}
	
	/**
	 * <p>getName.</p>
	 *
	 * @param id a {@link java.lang.String} object.
	 * @return a {@link java.lang.String} object.
	 */
	protected abstract String getName(String id);

}
