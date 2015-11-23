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

	public InetAddress address;
	int port;
	Vector<String> depot;
	Game game;
	DatagramSocket client;
	// DEBUGGING
	int sent = 0;
	
	public MultiSender(InetAddress address, int port, Vector<String> depot, Game game){
		this.depot = depot;
		this.address = address;
		this.port = port;
		this.game = game;
		try {
			client = new DatagramSocket();
			
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run(){
		try {
			@SuppressWarnings("resource")
			
			byte[] message;
			DatagramPacket packet;
			if(Game.debugSender)
				System.out.println("Cr�ation d'un sender - " + port);
			while(true){
				if(this.depot.size()>0){
					this.game.idPaquetSend++;
//					if(depot.get(0).charAt(0)=='2' && game.host){
//						address = InetAddress.getByName(depot.get(0).substring(1));
//					}
					message = (depot.get(0).toString()).getBytes();
					packet = new DatagramPacket(message, message.length, this.address, this.port);
					packet.setData(message);
					client.send(packet);
					sent++;
					//System.out.println("sent :" + sent);
					if(Game.debugSender)
						System.out.println("port : " + port + " address: "+this.address.getHostAddress()+" message sent: " + this.depot.get(0));
					this.depot.remove(0);
				}
				Thread.sleep(1);
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
	
	public void sendMessage(String s){
		this.depot.addElement(s);
	}
}
