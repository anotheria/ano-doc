package net.anotheria.asg.service.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * <p>RemoteService interface.</p>
 *
 * @author another
 * @version $Id: $Id
 */
public interface RemoteService extends Remote{
	
	/**
	 * Converts echo request to echo response and return it. Is useful for for checking remote object availability
	 *
	 * @return converted EchoRequest to EchoResponse
	 * @param echoRequest a {@link net.anotheria.asg.service.remote.EchoRequest} object.
	 * @throws java.rmi.RemoteException if any.
	 */
	EchoResponse getEcho(EchoRequest echoRequest) throws RemoteException;
	
}
