package net.anotheria.asg.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteService extends Remote{
	
	/**
	 * Converts echo request to echo response and return it. Is useful for for checking remote object availability
	 */
	public byte[] getEcho(byte[] echoRequest) throws RemoteException;
	
}
