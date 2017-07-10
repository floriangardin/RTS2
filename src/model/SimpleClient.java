package model;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import control.InputObject;
import multiplaying.Checksum;
import plateau.Plateau;

public class SimpleClient extends Listener {
	//OPTIONS
	private final static Client client = new Client(500000, 500000);
	private static String ip = null; //FOR SINGLEPLAYER
	private static int port = 27960;
	// STATE
	private static  Plateau plateau; // Mutable State side effect ...
	private final static Vector<InputObject> inputs = new Vector<InputObject>();
	static final int delay = 8; // Number of delay rounds
	static final ReentrantLock mutex = new ReentrantLock() ;
	public static void init(Plateau plateau){
		SimpleClient.plateau = plateau;
		client.getKryo().register(byte[].class);
		client.getKryo().register(Integer.class);
		client.getKryo().register(String.class);
		client.getKryo().register(Message.class);
		client.addListener(new Listener(){
			public void received(Connection c, Object o){
				if(o instanceof Message){
					Message m = (Message) o;
					int type = m.getType();
					if(type==Message.PLATEAU){
						Plateau plateau = (Plateau) m.get();
						SimpleClient.setPlateau(plateau);
					}else if(type==Message.INPUTOBJECT){
						InputObject im = (InputObject)m.get();
						System.out.println("Ping : "+ (int)(1e-6*(System.nanoTime()-im.time))+" ms");
						SimpleClient.addInput(im);
					}
				}
			}
		});
		client.start();
		try {
			ip  = getIP();
			System.out.println("IP of server : " +ip);
			client.connect(5000, ip, port, port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static String getIP() {
		try{
			InetAddress host = client.discoverHost(port, 5000); 
			return host.getHostAddress();
		}catch(Exception e ){
			return null;
		}

	}
	public static void send(InputObject im){
		Message m = new Message(im);
		client.sendUDP(m);
	}

	public static int roundForInput(){
		return plateau.round+delay;
	}
	public static int getRound(){
		return plateau.round;
	}

	public static Vector<InputObject> getInputForRound(){
		Vector<InputObject> res = new Vector<InputObject>();
		Vector<InputObject> toRemove = new Vector<InputObject>();
		synchronized(inputs){
			for(InputObject im: inputs){
				if(im.round==plateau.round){
					res.add(im);
				}else if(im.round<plateau.round){
					toRemove.add(im);
				}
			}
			inputs.removeAll(res);
			inputs.removeAll(toRemove);
		}
		//System.out.println("Res : "+res.size());
		return res;
	}
	public static void send(Checksum checksum){
		Message m = new Message(checksum);
		client.sendUDP(m);
	}
	public static void send(Plateau plateau){
		Message m = new Message(plateau);
		client.sendUDP(Serializer.serialize(m));
	}

	public static Plateau getPlateau(){	
		return SimpleClient.plateau;
	}
	public static void setPlateau(Plateau plateau){
		mutex.lock();
		try {
			System.out.println("New plateau");
			SimpleClient.plateau = plateau;
		} finally {
		    mutex.unlock();
		}
	}
	public static void addInput(InputObject im){
		synchronized(inputs){	
			inputs.add(im);
		}
	}
	public static void removeInput(Vector<InputObject> ims){
		synchronized(inputs){			
			inputs.removeAll(ims);
		}
	}

}
