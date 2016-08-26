package net.anotheria.asg.exception;

/**
 * Base class for all exceptions thrown by generated code at runtime.
 *
 * @author another
 * @version $Id: $Id
 */
public class ASGRuntimeException extends Exception{
	/**
	 * Creates a new exception with a message.
	 *
	 * @param message a {@link java.lang.String} object.
	 */
	public ASGRuntimeException(String message){
		super(message);
	}
	/**
	 * Creates a new exception with  a cause.
	 *
	 * @param cause a {@link java.lang.Throwable} object.
	 */
	public ASGRuntimeException(Throwable cause){
		super(cause);
	}
	/**
	 * Creates a new exception with a message and a cause.
	 *
	 * @param cause a {@link java.lang.Throwable} object.
	 * @param message a {@link java.lang.String} object.
	 */
	public ASGRuntimeException(String message, Throwable cause){
		super(message, cause);
	}
}
