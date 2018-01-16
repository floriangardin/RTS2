package model;

import java.io.IOException;
import java.util.Vector;

import main.Main;
import multiplaying.Checksum;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import control.InputObject;
import plateau.Plateau;
import ressources.Map;


public strictfp class GameServer extends Listener {
	static final GameServer gameServer= new GameServer(); 
	static Server server;
	public static boolean hasLaunched = false;
	static final Vector<Checksum> checksums = new Vector<Checksum>();
	
	public static String DESYNCHRO = "desynchro";
	public static String HOST = "host";
	public static Vector<Integer> players = new Vector<Integer>();
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
				server.addListener(gameServer);
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
			server.removeListener(gameServer);
			server.close();
			
		}
		hasLaunched = false;
	}
	
	public void connected(Connection c){
		
		// If connection send plateau to id
		System.out.println("Connection received. "+c.getID());
		if(GameClient.getPlateau() != null){			
			server.sendToAllTCP(new Message(GameClient.getPlateau()));
		}
//		//server.sendToAllExceptTCP(c.getID(), c.getID());
//		server.sendToTCP(c.getID(), "");
		if(players.size()==0){
			server.sendToTCP(c.getID(), new Message(GameServer.HOST));
		}
		players.addElement(c.getID());
		for(Integer i : players){
			System.out.print("  "+i);
		}
		System.out.println();
		
	}
	
	public void received(Connection c, Object o){
		if(o instanceof Message){
			// Check if it is a checksum
			Message m = (Message) o;
			if(m.getType()==Message.CHECKSUM){
				addChecksum((Checksum) m.get());
				if(!isSynchro()){
					//server.sendToAllTCP(new Message(GameClient.getPlateau()));
					server.sendToTCP(players.get(0), new Message(GameServer.DESYNCHRO));
				}
			}else if(m.getType()==Message.INPUTOBJECT){
				// Broadcast inputs to all (including host)
				
				server.sendToAllUDP(o);
				server.sendToAllTCP(o);
			}else if(m.getType()==Message.MENUPLAYER){
				server.sendToAllUDP(o);
			}else if(m.getType()==Message.CHATMESSAGE){
				server.sendToAllUDP(o);
			}else if(m.getType()==Message.PLATEAU){
				server.sendToAllTCP(m);
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
		if(getChecksums().size()>2*Main.delay){
			clearChecksum();
		}
		//System.out.println("Size checksms "+getChecksums().size());
		//int deltaRound = (maxRound-minRound);
		//System.out.println("max round : "+maxRound+ " min : "+minRound+ " d "+deltaRound);
		return true;
	}
	

	public void disconnected(Connection c){
		System.out.println("Connection dropped. "+c.getID());
		//server.sendToAllExceptTCP(c.getID(), "Disconnected|"+c.getID());
		if(players.get(0)==c.getID() && players.size()>1){
			server.sendToTCP(players.get(1), GameServer.HOST);
		}
		players.removeElement(c.getID());
		for(Integer i : players){
			System.out.print("  "+i);
		}
		System.out.println();
		
	}

	
	
}
