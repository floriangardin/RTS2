package model;

import java.io.IOException;
import java.util.Vector;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import control.InputObject;
import plateau.Plateau;
import ressources.Map;


public class SimpleServer extends Listener {

	static Server server;
	static final int port = 27960;
	static Vector<Integer> players = new Vector<Integer>();
	
	static Vector<InputObject> inputs = new Vector<InputObject>();
	// Hold Game State just in case 
	static Plateau plateau;
	static boolean p2p = true;
	
	// Le serveur a juste pour role de faire passer des inputs ...
	public static void launch(Plateau plateau){
		SimpleServer.plateau = plateau;
		server = new Server(5000000, 5000000);
		// Choose between byte and plateau
		server.getKryo().register(byte[].class);
		server.getKryo().register(Plateau.class);
		server.getKryo().register(String.class);
		try {
			server.bind(port, port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		server.start();
		server.addListener(new SimpleServer());
		System.out.println("The server is ready");
	}
	
	public synchronized void addInput(InputObject im){
		inputs.add(im);
	}
	public synchronized static Vector<InputObject> getInputs(){
		Vector<InputObject> result = new Vector<InputObject>();
		result.addAll(inputs);
		inputs.clear();
		return result;
	}
	
	public static void main(String[] args) throws IOException{
		if(!p2p){
			SimpleServer.launch(Map.createPlateau(Map.maps().get(0), "maps"));
		}else{
			SimpleServer.launch(null);
		}
		while(!p2p){ // Play plateau serverside
			play(16);
		}
	}
	
	public void connected(Connection c){
		server.sendToAllExceptTCP(c.getID(), "Connected"+c.getID());
		players.add(c.getID());
		System.out.println(c.getID());
		System.out.println("Connection received.");
	}
	

	public void received(Connection c, Object o){
		if(o instanceof byte[]){
			// Broadcast inputs to all
			server.sendToAllTCP(o);
			if(!p2p){				
				addInput((InputObject) Serializer.deserialize((byte[])o));
			}
		}else if(o instanceof String){
			server.sendToAllExceptTCP(c.getID(), o);
		}
	}
	
	public static void play(int sleepMillis){
		Vector<InputObject> toPlay = getInputs();
		System.out.println("Inputs size  : "+toPlay.size());
		plateau.update(toPlay);
		// Send plateau if necessary
		server.sendToAllTCP(Serializer.serialize(plateau));
		// Send Other inputs
		
		// Sleep until next turn
		try {
			Thread.sleep(sleepMillis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static byte[] serialize(InputObject im){
		return Serializer.serialize(im);
	}
	public static InputObject serialize(byte[] im){
		return Serializer.deserialize(im);
	}
	
	public void disconnected(Connection c){
		System.out.println("Connection dropped.");
		players.removeElement(c.getID());
		server.sendToAllExceptTCP(c.getID(), "Disconnected|"+c.getID());
	}
}
