/*
 * Created on 01-Mar-2016
 */
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

import common.MessageInfo;

public class UDPServer {

	private DatagramSocket recvSoc;
	private int totalMessages = -1;
	private int totalMessagesReceived = -1;
	private int[] receivedMessages;
	// private boolean close;

	private void run() {
		byte[]			pacData = new byte[256];
		int				pacSize = pacData.length;
		DatagramPacket 	pac;

		// TO-DO: Receive the messages and process them by calling processMessage(...).
		//        Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever
		pac = new DatagramPacket(pacData, pacSize);
		try {
			this.recvSoc.setSoTimeout(30000);
			while (true){
				this.recvSoc.receive(pac);
				String s = new String(pac.getData());
				this.processMessage(s.replace("\n", "").replace("\r", "").trim());
			}			
		} catch(SocketTimeoutException e){
			System.out.println("Timeout reached");
			// System.out.println("Number of lost messages: " + (msg.totalMessages - this.totalMessages));
			for (int i = 0 ; i < this.totalMessages ; i++){
				if (this.receivedMessages[i] == 0){
					System.out.println("[-] Message " + i + " is missing");
				}
			}
		} catch (IOException e){
			System.out.println("Error when receiving message: " + e);
		}
	}

	public void processMessage(String data) {

		MessageInfo msg = null;

		try {
			// TO-DO: Use the data to construct a new MessageInfo object
			msg = new MessageInfo(data);
	
			// TO-DO: On receipt of first message, initialise the receive buffer
			if (msg.messageNum == 0){
				this.totalMessages = msg.totalMessages;
				this.receivedMessages = new int[this.totalMessages];
				Arrays.fill(this.receivedMessages, 0);
			}
	
			// TO-DO: Log receipt of the message
			this.totalMessagesReceived++;
			this.receivedMessages[msg.messageNum] = 1;
			System.out.println("[+] Message " + msg.messageNum + " received");
	
			// TO-DO: If this is the last expected message, then identify
			//        any missing messages
	
			if (msg.messageNum == (this.totalMessages - 1)){
				if ((this.totalMessagesReceived + 1) != this.totalMessages){
					System.out.println("Number of lost messages: " + (this.totalMessages - this.totalMessagesReceived));
					for (int i = 0 ; i < this.totalMessages ; i++){
						if (this.receivedMessages[i] == 0){
							System.out.println("[-] Message " + i + " is missing");
						}
					}
				} else {
					System.out.println("No message was lost!");
				}
				System.exit(0);
			}

		} catch(SocketTimeoutException e){
			System.out.println("Timeout reached");
			// System.out.println("Number of lost messages: " + (msg.totalMessages - this.totalMessages));
			for (int i = 0 ; i < this.totalMessages ; i++){
				if (this.receivedMessages[i] == 0){
					System.out.println("[-] Message " + i + " is missing");
				}
			}
		} catch(Exception e){
			System.out.println("Error when receiving message: " + e);
		}


	}


	public UDPServer(int rp) {
		// TO-DO: Initialise UDP socket for receiving data
		try {
			this.recvSoc = new DatagramSocket(rp);
		} catch(SocketException e){
			System.out.println("Error creating the socket: " + e);
		}

		// Done Initialisation
		System.out.println("UDPServer ready");
	}

	public static void main(String args[]) {
		int	recvPort;
		
		// Get the parameters from command line
		if (args.length < 1) {
			System.err.println("Arguments required: recv port");
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[0]);

		// TO-DO: Construct Server object and start it by calling run().
		UDPServer server = new UDPServer(recvPort);
		server.run();
	}

}
