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
import control.Player;
import menu.Lobby;
import menuutils.Menu_Player;
import multiplaying.ChatHandler;
import multiplaying.ChatMessage;
import multiplaying.Checksum;
import plateau.Plateau;

public class GameClient extends Listener {
	//OPTIONS
	private final static Client client = new Client(5000000, 5000000);
	private static String ip = null; //FOR SINGLEPLAYER
	public final static int port = 27960;
	public static int slowDown = 0;
	// STATE
	private static  Plateau plateau; // Mutable State side effect ...
	private final static Vector<InputObject> inputs = new Vector<InputObject>();
	public static final int delay = 6; // Number of delay rounds
	static final ReentrantLock mutex = new ReentrantLock() ;
	
	public static void init(String ip){
		client.getKryo().register(byte[].class);
		client.getKryo().register(Integer.class);
		client.getKryo().register(Message.class);
		client.addListener(new Listener(){
			public void received(Connection c, Object o){
				Player.init(c.getID());	
				if(o instanceof Message){
					Message m = (Message) o;
					int type = m.getType();
					if(type==Message.PLATEAU){
						Plateau plateau = (Plateau) m.get();
						GameClient.setPlateau(plateau);
					}else if(type==Message.INPUTOBJECT){
						InputObject im = (InputObject)m.get();
						if(im.round>getRound()+GameClient.delay){
							//System.out.println("input recu trop tot : "+(im.round-delay-getRound()));
							client.sendUDP((im.round-delay-getRound()));
						}
						GameClient.addInput(im);
					}else if(type==Message.MENUPLAYER){
						Menu_Player mpMessage = (Menu_Player)m.get();
						boolean found = false;
						synchronized (Lobby.players) {
							for(Menu_Player mp : Lobby.players){
								if(mp.id==mpMessage.id){
									if(mp.id!=Player.getID()){
										mp.update(mpMessage);
									}
									found = true;
								}
							}
							if(!found){
								Lobby.addPlayer(mpMessage);
							}
						}
						if(mpMessage.isHost && mpMessage.id!=Player.getID()){
							Lobby.idCurrentMap = mpMessage.idMap;
						}
					}else if(type==Message.CHATMESSAGE){
						ChatHandler.addMessage((ChatMessage)m.get());
					}
				}else if(o instanceof Integer){
					slowDown = (Integer) o;
				}
			}
		});
		client.start();
		try {
			System.out.println("IP of server : " +ip);
			client.connect(5000, ip, port, port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getExistingServerIP() {
		// Method to move on the lobby .
		try{
			InetAddress host = client.discoverHost(port, 5000); 
			return host.getHostAddress();
		}catch(Exception e ){
			return null;
		}
	}
	public static Vector<String> getExistingServerIPS() {
		// Method to move on the lobby .
		try{
			Vector<String> result = new Vector<String>();
			for(InetAddress host : client.discoverHosts(port, 5000)){
				result.add(host.getHostAddress());
			}
			return result;
		}catch(Exception e ){
			return new Vector<String>();
		}
	}
	
	public static void send(InputObject im){
		Message m = new Message(im);
		client.sendUDP(m);
	}
	public static void send(Vector<InputObject> ims){
		for(InputObject im : ims ){			
			Message m = new Message(im);
			client.sendUDP(m);
		}
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
	public static void send(Menu_Player menu_player){
		Message m = new Message(menu_player);
		client.sendUDP(m);
	}
	public static void send(Plateau plateau){
		Message m = new Message(plateau);
		client.sendUDP(Serializer.serialize(m));
	}
	public static void send(ChatMessage message){
		Message m = new Message(message);
		client.sendUDP(m);
	}

	public static Plateau getPlateau(){	
		return GameClient.plateau;
	}
	public static void setPlateau(Plateau plateau){
		mutex.lock();
		try {
			System.out.println("New plateau");
			GameClient.plateau = plateau;
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
