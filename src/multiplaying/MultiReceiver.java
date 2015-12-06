package multiplaying;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;

import model.Game;
import model.Objet;

public abstract class MultiReceiver extends Thread{
	Game g;
	int port;
	public int received = 0;

	public DatagramSocket server;
	byte[] message;
	DatagramPacket packet;


	// DEBUGGING


	public MultiReceiver(Game g, int port){
		this.g = g;
		this.port = port;
		this.setName("MultiReceiver "+port);
	}
	
	public abstract void action(String msg);

	@Override
	public void run(){
		try{
			this.server = new DatagramSocket(port);
			if(Game.debugReceiver)
				System.out.println("Creation d'un receiver - " + port);
			while(!server.isClosed()){
				if(Game.debugThread){
					System.out.println(this.getName());
				}
				message = new byte[4000];
				packet = new DatagramPacket(message, message.length);
				try{
					server.receive(packet);
				} catch(java.net.SocketException e){
					break;
				}
				String msg = new String(packet.getData());
				this.g.nbPaquetReceived++;
				if(msg.length()>0 && !msg.substring(0,1).equals(""+g.plateau.currentPlayer.id)){
					if(Game.debugReceiver) System.out.println("port : " + port + " message received: " + msg.substring(1));
					this.action(msg.substring(1));
				}
			}
		} catch (IOException e) {
		} 
	}
	
	public void shutdown(){
		this.server.close();
	}
}

