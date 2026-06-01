package net.anotheria.anodoc.util.context;

/**
 * A factory for call context creation. You may supply your own CallContextFactory implementation to provide your own CallContexts.
 *
 * @author another
 * @version $Id: $Id
 */
public interface CallContextFactory {
	/**
	 * Creates a new context.
	 *
	 * @return a {@link net.anotheria.anodoc.util.context.CallContext} object.
	 */
	CallContext createContext();
}
