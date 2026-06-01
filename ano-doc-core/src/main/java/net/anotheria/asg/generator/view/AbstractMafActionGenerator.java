package net.anotheria.asg.generator.view;

import net.anotheria.asg.generator.AbstractGenerator;
import net.anotheria.asg.generator.GeneratorDataRegistry;
import net.anotheria.asg.generator.meta.MetaModule;
import net.anotheria.util.StringUtils;

/**
 * <p>AbstractMafActionGenerator class.</p>
 *
 * @author another
 * @version $Id: $Id
 */
public class AbstractMafActionGenerator extends AbstractGenerator{

	/**
	 * Returns the base action name for the application.
	 *
	 * @return a {@link java.lang.String} object.
	 */
	protected static String getBaseMafActionName(){
		return "Base"+StringUtils.capitalize(GeneratorDataRegistry.getInstance().getContext().getApplicationName())+"MafAction";
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
		return getSharedActionPackageName() + "." + getBaseMafActionName();
	}
	
}
