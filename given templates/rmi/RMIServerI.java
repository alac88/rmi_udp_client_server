/*
 * Created on 01-Mar-2016
 */
package rmi;

import common.MessageInfo;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerI extends Remote {

	public void receiveMessage(MessageInfo msg) throws RemoteException, IOException;
}
