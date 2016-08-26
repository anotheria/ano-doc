package net.anotheria.asg.generator.view.action;

import net.anotheria.asg.generator.AbstractGenerator;
import net.anotheria.asg.generator.GeneratorDataRegistry;
import net.anotheria.asg.generator.meta.MetaModule;
import net.anotheria.util.StringUtils;

/**
 * <p>AbstractActionGenerator class.</p>
 *
 * @author another
 * @version $Id: $Id
 */
public class AbstractActionGenerator extends AbstractGenerator{

	/**
	 * Returns the base action name for the application.
	 *
	 * @return a {@link java.lang.String} object.
	 */
	protected static String getBaseActionName(){
		return "Base"+StringUtils.capitalize(GeneratorDataRegistry.getInstance().getContext().getApplicationName())+"Action";
	}
	
	
	/**
	 * <p>getSharedActionPackageName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	protected static String getSharedActionPackageName(){
		return GeneratorDataRegistry.getInstance().getContext().getPackageName(MetaModule.SHARED)+".action";
	}
	
	/**
	 * <p>getBaseActionClassName.</p>
	 *
	 * @return a {@link java.lang.String} object.
	 */
	protected static String getBaseActionClassName(){
		return getSharedActionPackageName() + "." + getBaseActionName();
	}
	
}
