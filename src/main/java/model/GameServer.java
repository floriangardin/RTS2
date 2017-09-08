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


public strictfp class GameServer extends Listener {

	static Server server;
	public static boolean hasLaunched = false;
	static final Vector<Checksum> checksums = new Vector<Checksum>();
	// Le serveur a juste pour role de faire passer des inputs ...
	public static void init(){
		if(!hasLaunched){
			server = new Server(5000000, 5000000);
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
	}

	public static void addChecksum(Checksum c){
		synchronized(checksums){			
			checksums.add(c);
		}
	}

	public static void main(String[] args) throws IOException{
		GameServer.init();
	}
	
	public static void close(){
		if(server!=null){
			server.close();
		}
		hasLaunched = false;
	}
	
	public void connected(Connection c){
		
		// If connection send plateau to id
		System.out.println("Connection received.");
		if(GameClient.getPlateau() != null){			
			server.sendToAllTCP(new Message(GameClient.getPlateau()));
		}
		//server.sendToAllExceptTCP(c.getID(), c.getID());
		server.sendToTCP(c.getID(), "");
	}
	
	public void received(Connection c, Object o){
		if(o instanceof Message){
			// Check if it is a checksum
			Message m = (Message) o;
			if(m.getType()==Message.CHECKSUM){
				addChecksum((Checksum) m.get());
				if(!isSynchro()){
					server.sendToAllTCP(new Message(GameClient.getPlateau()));
				}
			}else if(m.getType()==Message.INPUTOBJECT){
				// Broadcast inputs to all (including host)
				server.sendToAllTCP(o);
			}else if(m.getType()==Message.MENUPLAYER){
				server.sendToAllTCP(o);
			}else if(m.getType()==Message.CHATMESSAGE){
				server.sendToAllTCP(o);
			}
		}else if(o instanceof Integer){
			server.sendToAllExceptTCP(c.getID(), o);
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
