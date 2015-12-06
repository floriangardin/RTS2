package multiplaying;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Vector;

import org.newdawn.slick.GameContainer;

import com.sun.jmx.snmp.InetAddressAcl;

import model.Game;

public class MultiSender extends Thread{

	public InetAddress address;
	int port;
	Vector<String> depot;
	Game game;
	DatagramSocket client;
	// DEBUGGING
	int sent = 0;

	public MultiSender(InetAddress addressToSend, int port, Vector<String> depot, Game game){
		this.depot = depot;
		this.address = addressToSend;
		this.port = port;
		this.game = game;
		try {
			client = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		this.setName("MultiSender "+port);
	}


	public MultiSender(int port, Vector<String> depot, Game game){
		this.depot = depot;
		String address;
		try {
			address = InetAddress.getLocalHost().getHostAddress();
			String[] tab = address.split("\\.");
			address = tab[0]+"."+tab[1]+"."+tab[2]+".255";
			this.port = port;
			this.game = game;
			this.address = InetAddress.getByName(address);
			client = new DatagramSocket();
		} catch (SocketException | UnknownHostException e) {
			System.out.println("Erreur dans la creation d'un multisender");
		}
		this.setName("MultiSender "+port);
	}

	public void run(){
		try {
			byte[] message;
			DatagramPacket packet;
			if(Game.debugSender)
				System.out.println("Creation d'un sender - " + port);
			while(!client.isClosed()){
				if(Game.debugThread){
					System.out.println(this.getName());
				}
				if(this.depot.size()>0){
					this.game.idPaquetSend++;
					//					if(depot.get(0).charAt(0)=='2' && game.host){
					//						address = InetAddress.getByName(depot.get(0).substring(1));
					//					}
					message = (game.plateau.currentPlayer.id+depot.get(0)).getBytes();
					packet = new DatagramPacket(message, message.length, this.address, this.port);
					packet.setData(message);
					client.send(packet);
					sent++;
					//System.out.println("sent :" + sent);
					if(Game.debugSender)
						System.out.println("port : " + port + " address: "+this.address.getHostAddress()+" message sent: " + this.depot.get(0));
					this.depot.remove(0);
				}
			}
		} catch (SocketException e1) {
		} catch (IOException e) {
		} 
	}

	public void shutdown(){
		client.close();
	}
	public void changeAddress(String a){
		try {
			this.address = InetAddress.getByName(a);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	public void changeAddress(InetAddress a){
		this.address = a;
	}
}
