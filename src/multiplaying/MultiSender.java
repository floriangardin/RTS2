package multiplaying;

import java.io.IOException;
import java.net.*;
import java.util.Vector;

import org.newdawn.slick.GameContainer;

import model.Game;

public class MultiSender extends Thread{

	InetAddress address;
	int port;
	GameContainer gc;
	Game g;
	Vector<String> depot;

	// DEBUGGING
	private boolean debug = true;
	int sent = 0;
	
	public MultiSender(GameContainer gc, Game g, InetAddress address, int port, Vector<String> depot){
		this.gc = gc;
		this.g = g;
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
					message = (depot.get(0).toString()).getBytes();
					packet = new DatagramPacket(message, message.length, this.address, this.port);
					packet.setData(message);
					client.send(packet);
					sent++;
					//System.out.println("sent :" + sent);
					if(debug)
						System.out.println("port : " + port + " message sent: " + this.depot.get(0));
					this.depot.remove(0);
				}
				try{
					Thread.sleep(1);
				} catch (InterruptedException e){
					if(debug)
						System.out.println("death of sender " + this);
					break;
				}
			}
		} catch (SocketException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
