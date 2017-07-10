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
		server.getKryo().register(String.class);
		server.getKryo().register(Message.class);
		try {
			server.bind(port, port);
			hasLaunched = true;
			server.start();
			server.addListener(new SimpleServer());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

	public static void addChecksum(Checksum c){
		SimpleClient.mutex.lock();
		try{
			checksums.add(c);
		}finally{
			SimpleClient.mutex.unlock();
		}
	}
	public void addInput(InputObject im){
		SimpleClient.mutex.lock();
		try{			
			inputs.add(im);
		}finally{
			SimpleClient.mutex.unlock();
		}
	}
	public static Vector<InputObject> getInputs(){
		SimpleClient.mutex.lock();
		Vector<InputObject> result = new Vector<InputObject>();
		try{			
			result.addAll(inputs);
			inputs.clear();
		}finally{
			SimpleClient.mutex.unlock();
		}
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
					System.out.println("desynchro");
					server.sendToAllTCP(new Message(SimpleClient.getPlateau()));
				}
			}else{
				// Broadcast inputs to all (including host)
				server.sendToAllTCP(o);
			}
		}
	}
	
	public static void clearChecksum(){
		SimpleClient.mutex.lock();
		try{			
			checksums.clear();
		}finally{
			SimpleClient.mutex.unlock();
		}
		
	}
	public static Vector<Checksum> getChecksums(){
		SimpleClient.mutex.lock();
		try{			
			return checksums;
		}finally{
			SimpleClient.mutex.unlock();
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
		if(getChecksums().size()>2*SimpleClient.delay){
			clearChecksum();
		}
		//System.out.println("Size checksms "+getChecksums().size());
		//int deltaRound = (maxRound-minRound);
		//System.out.println("max round : "+maxRound+ " min : "+minRound+ " d "+deltaRound);
		return true;
	}

	public void disconnected(Connection c){
		System.out.println("Connection dropped.");
		server.sendToAllExceptTCP(c.getID(), "Disconnected|"+c.getID());
	}
}
