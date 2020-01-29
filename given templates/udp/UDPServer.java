/*
 * Created on 01-Mar-2016
 */
package udp;

import common.MessageInfo;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServer {

	private DatagramSocket recvSoc;
	private int totalMessages = -1;
	private int[] receivedMessages;
	private boolean close;

	public UDPServer(int rp) {
		// TO-DO: Initialise UDP socket for receiving data
		try {
			recvSoc = new DatagramSocket(rp);
		} catch (Exception e) {
			// TO-DO
		}

		// Done Initialisation
		System.out.println("UDPServer ready");
	}

	public static void main(String args[]) {
		int recvPort;

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

	private void run() {
		int pacSize;
		byte[] pacData;
		DatagramPacket pac;

		// TO-DO: Receive the messages and process them by calling processMessage(...).
		//        Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever
		close = false;
		pacData = new byte[256];
		while (!close) {
			pacSize = pacData.length;
			pac = new DatagramPacket(pacData, pacSize);
			try {
				recvSoc.receive(pac);
			} catch (Exception e) {
				// TO-DO

			}

			String received = new String(pac.getData(), 0, pac.getLength());
			System.out.println("Receive: " + received);
			processMessage(received);
		}
		recvSoc.close();
	}

	private void printMissingMessage(int[] receivedMessages, int msgNum, int totalMessages) {
		boolean[] table = new boolean[msgNum];

		System.out.println("In total " + totalMessages + " have been received");
		for (int msgSeq : receivedMessages) {
			table[msgSeq - 1] = true;
		}
		for (int i = 0; i < msgNum; i++) {
			if (!table[i]) {
				System.out.println("Message " + i + 1 + "is not received!");
			}
		}
	}

	public void processMessage(String data) {

//		System.out.println("Now processing data: " + data);

		MessageInfo msg = null;

		// TO-DO: Use the data to construct a new MessageInfo object
		try {
			msg = new MessageInfo(data.substring(0, data.length() - 1));
		} catch (Exception e) {
			// TO-DO: deal with exception
			System.out.println("Create a new receive message failed! (" + e + ")");
		}

		// TO-DO: On receipt of first message, initialise the receive buffer
		if (totalMessages == -1) {
			receivedMessages = new int[msg.totalMessages];
			totalMessages = 1;
		} else {
			totalMessages++;
		}

		// TO-DO: Log receipt of the message
		receivedMessages[totalMessages - 1] = msg.messageNum;

		// TO-DO: If this is the last expected message, then identify
		//        any missing messages
		if (msg.messageNum == msg.totalMessages) {
			printMissingMessage(receivedMessages, msg.totalMessages, totalMessages);
			close = true;
		}
	}

}
