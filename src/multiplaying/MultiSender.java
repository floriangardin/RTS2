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
	Vector<MultiMessage> depot;
	Game game;
	DatagramSocket client;
	// DEBUGGING
	int sent = 0;

	public MultiSender(Vector<MultiMessage> depot, Game game){
		this.depot = depot;
		try {
			this.game = game;
			client = new DatagramSocket();
		} catch (SocketException  e) {
			System.out.println("Erreur dans la creation d'un multisender");
		}
		this.setName("MultiSender");
	}

	public void run(){
		try {
			byte[] message;
			DatagramPacket packet;
			if(Game.debugSender)
				System.out.println("Creation d'un sender - " + port);
			MultiMessage multimessage;
			while(!client.isClosed()){
				if(Game.debugThread){
					System.out.println(this.getName());
				}
				if(this.depot.size()>0){
					this.game.idPaquetSend++;
					//					if(depot.get(0).charAt(0)=='2' && game.host){
					//						address = InetAddress.getByName(depot.get(0).substring(1));
					//					}
					multimessage = depot.get(0);
					this.address = multimessage.address;
					this.port = multimessage.port;
					message = (game.currentPlayer.id+multimessage.message).getBytes();
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
