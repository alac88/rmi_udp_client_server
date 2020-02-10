/*
 * Created on 01-Mar-2016
 */
package udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import common.MessageInfo;

public class UDPClient {

	private DatagramSocket sendSoc = null;

	public static void main(String[] args) {
		InetAddress	serverAddr = null;
		int			recvPort;
		int 		countTo;

		// Get the parameters
		if (args.length < 3) {
			System.err.println("Arguments required: server name/IP, recv port, message count");
			System.exit(-1);
		}

		try {
			serverAddr = InetAddress.getByName(args[0]);
		} catch (UnknownHostException e) {
			System.out.println("Bad server address in UDPClient, " + args[0] + " caused an unknown host exception " + e);
			System.exit(-1);
		}
		recvPort = Integer.parseInt(args[1]);
		countTo = Integer.parseInt(args[2]);


		UDPClient client = new UDPClient();
		client.testLoop(serverAddr, recvPort, countTo);
	}

	public UDPClient(){
		try {
			this.sendSoc = new DatagramSocket();
		} catch(SocketException e){
			System.out.println("Error creating the socket: " + e);
		}
	}

	private void testLoop(InetAddress serverAddr, int recvPort, int countTo) {

		for (int i = 0 ; i < countTo ; i++){
			MessageInfo msg = new MessageInfo(countTo, i);
			try {
				TimeUnit.MILLISECONDS.sleep(10);
				send(msg.toString(), serverAddr, recvPort);
			} catch(Exception e){
				System.out.println("Error when sending message: " + e);
			}
		}
	}

	private void send(String payload, InetAddress destAddr, int destPort) {
		int				payloadSize;
		byte[]				pktData = new byte[256];
		DatagramPacket		pkt;

		pktData = payload.getBytes();
		payloadSize = pktData.length;
		pkt = new DatagramPacket(pktData, payloadSize, destAddr, destPort);
		try {
			this.sendSoc.send(pkt);
		} catch (IOException e){
			System.out.println("Error when sending message: " + e);
		}
	}
}
