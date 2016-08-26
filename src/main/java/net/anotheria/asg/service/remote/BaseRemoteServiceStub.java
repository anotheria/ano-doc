package net.anotheria.asg.service.remote;

import java.rmi.RemoteException;

import net.anotheria.asg.exception.ASGRuntimeException;

/**
 * <p>Abstract BaseRemoteServiceStub class.</p>
 *
 * @author another
 * @version $Id: $Id
 */
abstract public class BaseRemoteServiceStub <T extends RemoteService>{
	
	/**
	 * <p>notifyDelegateFailed.</p>
	 */
	abstract protected void notifyDelegateFailed();
	
	/**
	 * <p>getDelegate.</p>
	 *
	 * @return a T object.
	 * @throws net.anotheria.asg.exception.ASGRuntimeException if any.
	 */
	abstract protected T getDelegate() throws ASGRuntimeException;
	
	/**
	 * Sends echo request to remote service and receives response. Return duration off echo request/response in mills
	 *
	 * @return a long.
	 * @throws java.rmi.RemoteException if any.
	 */
	public long ping() throws RemoteException{
		return ping(0);
	}
	
	/**
	 * Sends echo request to remote service and receives response. Return duration off echo request/response in mills
	 *
	 * @param packetSize a int.
	 * @return a long.
	 * @throws java.rmi.RemoteException if any.
	 */
	public long ping(int packetSize) throws RemoteException{
		try{
			EchoRequest req = new EchoRequest();
			req.setEchoDataSize(packetSize);
			req.setRequestTime(System.currentTimeMillis());
			
			EchoResponse res = getDelegate().getEcho(req);
			
			long duration = System.currentTimeMillis() - res.getResponseTime();
			return duration;
		} catch (RemoteException e){
			notifyDelegateFailed();
			throw e;
		} catch (ASGRuntimeException e){
			throw new RemoteException("Server is unreachable: ",e);
		}
	}
	
	
	
}
