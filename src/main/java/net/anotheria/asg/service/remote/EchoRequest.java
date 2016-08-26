package net.anotheria.asg.service.remote;

import java.io.Serializable;

/**
 * <p>EchoRequest class.</p>
 *
 * @author another
 * @version $Id: $Id
 */
public class EchoRequest implements Serializable{
	
	private static final long serialVersionUID = 8124014806417852570L;
	/**
	 * (Local) Time of request.
	 */
	private long requestTime;
	/**
	 * Amount of data to return in an array in bytes.
	 */
	private int echoDataSize;

	/**
	 * <p>Getter for the field <code>requestTime</code>.</p>
	 *
	 * @return a long.
	 */
	public long getRequestTime() {
		return requestTime;
	}
	/**
	 * <p>Setter for the field <code>requestTime</code>.</p>
	 *
	 * @param requestTime a long.
	 */
	public void setRequestTime(long requestTime) {
		this.requestTime = requestTime;
	}
	/**
	 * <p>Getter for the field <code>echoDataSize</code>.</p>
	 *
	 * @return a int.
	 */
	public int getEchoDataSize() {
		return echoDataSize;
	}
	/**
	 * <p>Setter for the field <code>echoDataSize</code>.</p>
	 *
	 * @param echoDataSize a int.
	 */
	public void setEchoDataSize(int echoDataSize) {
		this.echoDataSize = echoDataSize;
	}

}
