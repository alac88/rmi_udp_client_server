/*
 * Created on 01-Mar-2016
 */
package rmi;

import common.MessageInfo;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RMIClient {

  public static void main(String[] args) {

    RMIServerI iRMIServer = null;

    // Check arguments for Server host and number of messages
    if (args.length < 2) {
      System.out.println("Needs 2 arguments: ServerHostName/IPAddress, TotalMessageCount");
      System.exit(-1);
    }

    String urlServer = "rmi://" + args[0] + "/RMIServer";
    int numMessages = Integer.parseInt(args[1]);

//		 TO-DO: Initialise Security Manager
    if (System.getSecurityManager() == null) {
      System.setSecurityManager(new SecurityManager());
    }

    // TO-DO: Bind to RMIServer
    try {
      iRMIServer = (RMIServerI) Naming.lookup(urlServer);
    } catch (NotBoundException e) {
      System.err.println("Registry could not be contacted: " + e);
      e.printStackTrace();
    } catch (RemoteException e) {
      System.err.println("Name is not currently bound: " + e);
      e.printStackTrace();
    } catch (Exception e) {
      System.err.println("Failed to lookup remote object of server: " + e);
      e.printStackTrace();
    }

    // TO-DO: Attempt to send messages the specified number of times
    for (int messageI = 1; messageI <= numMessages; messageI++) {
      MessageInfo msg = new MessageInfo(numMessages, messageI);
      try {
        iRMIServer.receiveMessage(msg);
      } catch (IOException e) {
        System.out.println("Send message failed: " + e);
        e.printStackTrace();
      }
    }

    System.out.println("Finished sending messages");
  }
}
