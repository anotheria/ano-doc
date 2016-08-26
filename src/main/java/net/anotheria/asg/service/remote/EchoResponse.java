package net.anotheria.asg.service.remote;

import java.io.Serializable;

/**
 * Response to an echo request.
 *
 * @author another
 * @version $Id: $Id
 */
public class EchoResponse implements Serializable{
	/**
	 * SerialVersionUID.
	 */
	private static final long serialVersionUID = 6461115896678559676L;
	//TODO ? wie berechnet?
	/**
	 * Duration of the response.
	 */
	private long responseTime;
//	private String serviceClass;
	/**
	 * Sent data.
	 */
	private byte[] data;
	
	/**
	 * <p>Getter for the field <code>responseTime</code>.</p>
	 *
	 * @return a long.
	 */
	public long getResponseTime() {
		return responseTime;
	}
	/**
	 * <p>Setter for the field <code>responseTime</code>.</p>
	 *
	 * @param responseTime a long.
	 */
	public void setResponseTime(long responseTime) {
		this.responseTime = responseTime;
	}
	/**
	 * <p>Getter for the field <code>data</code>.</p>
	 *
	 * @return an array of byte.
	 */
	public byte[] getData() {
		return data;
	}
	/**
	 * <p>Setter for the field <code>data</code>.</p>
	 *
	 * @param data an array of byte.
	 */
	public void setData(byte[] data) {
		this.data = data;
	}
		
}
