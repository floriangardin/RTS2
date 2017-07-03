package model;

import java.io.IOException;
import java.util.Vector;
import multiplaying.Checksum;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import control.InputObject;
import plateau.Plateau;
import ressources.Map;


public class SimpleServer extends Listener {

	static Server server;
	static final int port = 27960;
	static final int delay = 2; // Number of delay rounds
	// State
	static Vector<InputObject> inputs = new Vector<InputObject>();
	static Vector<Checksum> checksums = new Vector<Checksum>();
	// Le serveur a juste pour role de faire passer des inputs ...

	public static void init(){
		server = new Server(5000000, 5000000);
		// Choose between byte and plateau
		server.getKryo().register(byte[].class);
		server.getKryo().register(Integer.class);
		server.getKryo().register(String.class);
		server.getKryo().register(Message.class);
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

	public synchronized static void addChecksum(Checksum c){
		checksums.add(c);
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
		SimpleServer.init();
	}
	
	public void connected(Connection c){
		// If connection send plateau to id
		System.out.println("Connection received.");
		server.sendToTCP(c.getID(), new Message(SimpleClient.getPlateau()));
		server.sendToAllExceptTCP(c.getID(), c.getID());
		System.out.println(c.getID());
	}
	
	public void received(Connection c, Object o){
		if(o instanceof Message){
			// Check if it is a checksum
			Message m = (Message) o;
			if(m.getType()==Message.CHECKSUM){
				addChecksum((Checksum) m.get());
				if(!isSynchro()){
					server.sendToAllTCP(new Message(SimpleClient.getPlateau()));
				}
			}else{
				// Broadcast inputs to all (including host)
				System.out.println("youpi aut chose");
				server.sendToAllTCP(o);
			}
		}else if(o instanceof String){
			server.sendToAllExceptTCP(c.getID(), o);
		}
	}
	
	public synchronized static void clearChecksum(){
		checksums.clear();
	}

	private static boolean isSynchro() {
		for(Checksum c : checksums){
			for(Checksum d: checksums){
				if(!c.equals(d)){
					return false;
				}
			}
		}
		if(checksums.size()>5){
			clearChecksum();
		}
		return true;
	}

	public void disconnected(Connection c){
		System.out.println("Connection dropped.");
		server.sendToAllExceptTCP(c.getID(), "Disconnected|"+c.getID());
	}
}
