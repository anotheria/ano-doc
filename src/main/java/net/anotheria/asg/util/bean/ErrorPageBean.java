package net.anotheria.asg.util.bean;

/**
 * This beans contains information about an error for the global error page.
 * @author lrosenberg
 * Created on 21.05.2004
 */
public class ErrorPageBean {
	private String message;
	private String stackTrace;
	
	public ErrorPageBean(){
		
	}
	
	public ErrorPageBean(String aMessage, String aStackTrace){
		message = aMessage;
		stackTrace = aStackTrace;
	}
	
	/**
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return
	 */
	public String getStackTrace() {
		return stackTrace;
	}

	/**
	 * @param string
	 */
	public void setMessage(String string) {
		message = string;
	}

	/**
	 * @param string
	 */
	public void setStackTrace(String string) {
		stackTrace = string;
	}

}
