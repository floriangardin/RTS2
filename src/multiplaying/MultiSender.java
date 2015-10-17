package multiplaying;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Vector;

import org.newdawn.slick.GameContainer;

import model.Game;

public class MultiSender extends Thread{

	InetAddress address;
	int port;
	Vector<String> depot;

	// DEBUGGING
	private boolean debug = true;
	int sent = 0;
	
	public MultiSender(InetAddress address, int port, Vector<String> depot){
		this.depot = depot;
		this.address = address;
		this.port = port;
	}

	public void run(){
		try {
			@SuppressWarnings("resource")
			DatagramSocket client = new DatagramSocket();
			byte[] message;
			DatagramPacket packet;
			if(debug)
				System.out.println("Création d'un sender - " + port);
			while(true){
				if(this.depot.size()>0){
					if(depot.get(0).charAt(0)=='2'){
						address = InetAddress.getByName(depot.get(0).substring(1));
					}
					message = (depot.get(0).toString()).getBytes();
					
					packet = new DatagramPacket(message, message.length, this.address, this.port);
					packet.setData(message);
					client.send(packet);
					sent++;
					//System.out.println("sent :" + sent);
					if(debug)
						System.out.println("port : " + port + " address: "+this.address.getHostAddress()+" message sent: " + this.depot.get(0));
					this.depot.remove(0);
				}
				Thread.sleep((long) 0.001);
			}
		} catch (SocketException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
