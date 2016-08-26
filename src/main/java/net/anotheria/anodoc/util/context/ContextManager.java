package net.anotheria.anodoc.util.context;
/**
 * Context manager manages call contextes. It uses a CallContextFactory to create new contextes.
 * Call setFactory prior to anything else.
 *
 * @author another
 * @version $Id: $Id
 */
public class ContextManager {
	/**
	 * The factory for context creation.
	 */
	private static CallContextFactory factory;
	
	private static ThreadLocal<CallContext> callContext = new ThreadLocal<CallContext>(){
		@Override
		protected synchronized CallContext initialValue(){
			return factory.createContext();
		}
	};
	
	/**
	 * Returns the call context for this thread.
	 *
	 * @return a {@link net.anotheria.anodoc.util.context.CallContext} object.
	 */
	public static CallContext getCallContext(){
		return callContext.get();
	}
	
	/**
	 * <p>Setter for the field <code>callContext</code>.</p>
	 *
	 * @param value a {@link net.anotheria.anodoc.util.context.CallContext} object.
	 */
	public static void setCallContext(CallContext value){
		callContext.set(value);
	}
	
	/**
	 * Returns the set factory.
	 *
	 * @return a {@link net.anotheria.anodoc.util.context.CallContextFactory} object.
	 */
	public static CallContextFactory getFactory() {
		return factory;
	}
	/**
	 * Sets the factory.
	 *
	 * @param factory a {@link net.anotheria.anodoc.util.context.CallContextFactory} object.
	 */
	public static void setFactory(CallContextFactory factory) {
		ContextManager.factory = factory;
	}
}

