package net.anotheria.asg.service.remote;

import java.io.Serializable;

public class EchoRequest implements Serializable{
	
	private static final long serialVersionUID = 8124014806417852570L;

	private long requestTime;
	private int echoDataSize;

	public long getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(long requestTime) {
		this.requestTime = requestTime;
	}
	public int getEchoDataSize() {
		return echoDataSize;
	}
	public void setEchoDataSize(int echoDataSize) {
		this.echoDataSize = echoDataSize;
	}

}
