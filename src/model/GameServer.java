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


public class GameServer extends Listener {

	static Server server;
	
	static boolean hasLaunched = false;
	// State
	static final Vector<InputObject> inputs = new Vector<InputObject>();
	static final Vector<Checksum> checksums = new Vector<Checksum>();
	// Le serveur a juste pour role de faire passer des inputs ...

	public static void init(){
		server = new Server(500000, 500000);
		// Choose between byte and plateau
		server.getKryo().register(byte[].class);
		server.getKryo().register(Integer.class);
		server.getKryo().register(Message.class);
		try {
			server.bind(GameClient.port, GameClient.port);
			hasLaunched = true;
			server.start();
			server.addListener(new GameServer());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void addChecksum(Checksum c){
		synchronized(checksums){			
			checksums.add(c);
		}
	}
	public void addInput(InputObject im){
		synchronized(inputs){			
			inputs.add(im);
		}
	}
	public static Vector<InputObject> getInputs(){
		synchronized(inputs){
			Vector<InputObject> result = new Vector<InputObject>();
			result.addAll(inputs);
			inputs.clear();
			return result;
			
		}
	}
	
	public static void main(String[] args) throws IOException{
		GameServer.init();
	}
	
	public void connected(Connection c){
		// If connection send plateau to id
		System.out.println("Connection received.");
		server.sendToAllTCP( new Message(GameClient.getPlateau()));
		//server.sendToAllExceptTCP(c.getID(), c.getID());
		server.sendToTCP(c.getID(), new Message(c.getID()));
	}
	
	public void received(Connection c, Object o){
		if(o instanceof Message){
			// Check if it is a checksum
			Message m = (Message) o;
			if(m.getType()==Message.CHECKSUM){
				addChecksum((Checksum) m.get());
				if(!isSynchro()){
					System.out.println("desynchro");
					server.sendToAllTCP(new Message(GameClient.getPlateau()));
				}
			}else if(m.getType()==Message.INPUTOBJECT){
				// Broadcast inputs to all (including host)
				server.sendToAllUDP(o);
			}
		}else if(o instanceof Integer){
			server.sendToAllExceptUDP(c.getID(), o);
		}
	}
	
	public static void clearChecksum(){
		synchronized(checksums){			
			checksums.clear();
		}
		
		
	}
	public static Vector<Checksum> getChecksums(){
		synchronized(checksums){
			return checksums;
		}
	}
	
	

	private static boolean isSynchro() {
		int minRound = -1;
		int maxRound = -1;
		for(Checksum c : getChecksums()){
			for(Checksum d: getChecksums()){
				if(c.round<minRound || minRound==-1){
					minRound = c.round;
				}
				if(c.round>maxRound){
					maxRound = c.round;
				}
				if(!c.equals(d)){
					System.out.println("Inequalities in round "+c.round+" "+d.round);
					clearChecksum();
					return false;
				}
			}
		}
		if(getChecksums().size()>2*GameClient.delay){
			clearChecksum();
		}
		//System.out.println("Size checksms "+getChecksums().size());
		//int deltaRound = (maxRound-minRound);
		//System.out.println("max round : "+maxRound+ " min : "+minRound+ " d "+deltaRound);
		return true;
	}

	public void disconnected(Connection c){
		System.out.println("Connection dropped.");
		//server.sendToAllExceptTCP(c.getID(), "Disconnected|"+c.getID());
	}
}
