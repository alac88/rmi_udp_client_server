/*
 * Created on 01-Mar-2016
 */
package rmi;

import common.MessageInfo;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

	private int totalMessages = -1;
	private int[] receivedMessages;

	public RMIServer() throws RemoteException {
	}

	public static void main(String[] args) {

		RMIServer rmis = null;

		// TO-DO: Initialise Security Manager
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		try {
			// TO-DO: Instantiate the server class
			rmis = new RMIServer();

			// TO-DO: Bind to RMI registry
			if (args.length < 1) {
				System.out.println("Needs 1 argument1: ServerHostName/IPAddress");
				System.exit(-1);
			}
			String urlServer = new String("rmi://" + args[0] + "/RMIServer");
			rebindServer(urlServer, rmis);

			System.out.println("Server ready");
		} catch (Exception e) {
			// If the necessary communication resources are not available, such as if the requested
			// port is bound for some other purpose.
			System.out.println("Server initialisation failed: ");
			e.printStackTrace();
		}
	}

	protected static void rebindServer(String serverURL, RMIServer server) {

		// TO-DO:
		// Start / find the registry (hint use LocateRegistry.createRegistry(...)
		// If we *know* the registry is running we could skip this
		// (eg run rmiregistry in the start script)
		int IPLeft = serverURL.indexOf("//") + 2;
		int IPRight = serverURL.indexOf("/RMIServer");
		String IP = serverURL.substring(IPLeft, IPRight);
		String[] IPParse = IP.split(":");
		int serverPort = 1099;
		if (IPParse.length == 2) {
			serverPort = Integer.parseInt(IPParse[1]);
		}
		try {
			LocateRegistry.createRegistry(serverPort);
		} catch (Exception e) {
			System.out.println("Registry for " + serverPort + " has already existed");
		}

		// TO-DO:
		// Now rebind the server to the registry (rebind replaces any existing servers bound to the serverURL)
		// Note - Registry.rebind (as returned by createRegistry / getRegistry) does something similar but
		// expects different things from the URL field.
		try {
			Naming.rebind(serverURL, server);
		} catch (Exception e) {
			System.out.println("Rebind server fail: ");
			e.printStackTrace();
		}
	}

	private void printMissingMessage(int[] receivedMessages, int expectedMessages,
			int totalMessages) {
		boolean[] table = new boolean[expectedMessages];

		System.out.println("In total " + totalMessages + " have been received");
		for (int message : receivedMessages) {
			table[message - 1] = true;
		}
		for (int i = 0; i < expectedMessages; i++) {
			if (!table[i]) {
				System.out.println("Message " + (i + 1) + "is not received!");
			}
		}
	}

	public void receiveMessage(MessageInfo msg) throws RemoteException, IOException {

		// TO-DO: On receipt of first message, initialise the receive buffer
		if (totalMessages == -1) {
			if (msg != null) {
				receivedMessages = new int[msg.totalMessages];
			} else {
				throw new IOException("Null message");
			}
			totalMessages = 0;
		}

		// TO-DO: Log receipt of the message
		totalMessages++;
		receivedMessages[totalMessages - 1] = msg.messageNum;

		// TO-DO: If this is the last expected message, then identify
		//        any missing messages
		if (msg.totalMessages == msg.messageNum) {
			printMissingMessage(receivedMessages, msg.totalMessages, totalMessages);
			totalMessages = -1;
		}
	}
}
