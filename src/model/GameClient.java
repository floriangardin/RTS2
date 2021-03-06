package model;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import control.InputObject;
import control.Player;
import main.Main;
import menu.Lobby;
import menu.MenuMulti.OpenGame;
import menuutils.Menu_Map;
import menuutils.Menu_Player;
import multiplaying.ChatHandler;
import multiplaying.ChatMessage;
import multiplaying.Checksum;
import plateau.Plateau;
import ressources.Map;

public strictfp class GameClient extends Listener {
	//OPTIONS
	private final static Client client = new Client(5000000, 5000000);
	public static final GameClient gameClient = new GameClient();
	private static String ip = null; //FOR SINGLEPLAYER
	public final static int port = 27960;
	public static int slowDown = 0;
	// STATE
	private static  Plateau plateau; // Mutable State side effect ...
	public static boolean isHost;
	private final static Vector<InputObject> inputs = new Vector<InputObject>();
	static final ReentrantLock mutex = new ReentrantLock() ;
	public static String ipBattlemythe = "37.59.97.190"; 
public static void init(String ip) throws IOException{
		client.getKryo().register(byte[].class);
		client.getKryo().register(Integer.class);
		client.getKryo().register(Message.class);
		client.addListener(gameClient);
		client.start();
		client.connect(5000, ip, port, port);
	}
	
	
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
				if(im.round>getRound()+Main.delay){
					//System.out.println("input recu trop tot : "+(im.round-delay-getRound()));
					client.sendTCP((im.round-Main.delay-getRound()));
					client.sendUDP((im.round-Main.delay-getRound()));
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
				if(mpMessage.isHost ){
					Lobby.idCurrentMap = mpMessage.idMap;
				}
			} else if(type==Message.CHATMESSAGE){
				ChatHandler.addMessage((ChatMessage)m.get());
			}else if(type==Message.SYSTEMMESSAGE){
				if(((String) m.get()).equals(GameServer.DESYNCHRO)){					
					client.sendTCP(new Message(this.getPlateau()));
				}
				else if(((String) m.get()).equals(GameServer.HOST)){					
					GameClient.isHost = true;
					System.out.println("Je suis maintenant host");
				}
			}
		}else if(o instanceof Integer){
			slowDown = (Integer) o;
		}
	}
	
	public static Vector<OpenGame> getExistingServerIPS() {
		// Method to move on the lobby .
		try{
			Vector<OpenGame> result = new Vector<OpenGame>();
			Collection<InetAddress> hosts = client.discoverHosts(port, 200);
			for(InetAddress host : hosts){
				if(!host.getHostAddress().equals("127.0.0.1")){
					result.add(new OpenGame(host.getHostAddress(), ""));
				}
			}
			if(result.size()==0){
				// Check for battlemythe servers
				result.add(new OpenGame(GameClient.ipBattlemythe, ""));
			}
			return result;
		}catch(Exception e ){
			return new Vector<OpenGame>();
		}
	}
	
	public static void send(InputObject im){
		Message m = new Message(im);
//		client.sendUDP(m);
		client.sendTCP(m);
	}
	public static void send(Vector<InputObject> ims){
		for(InputObject im : ims ){			
			Message m = new Message(im);
//			client.sendUDP(m);
			client.sendTCP(m);
		}
	}

	public static int roundForInput(){
		return plateau.getRound()+Main.delay;
	}
	public static int getRound(){
		return plateau.getRound();
	}

	public static Vector<InputObject> getInputForRound(){
		Vector<InputObject> res = new Vector<InputObject>();
		Vector<InputObject> toRemove = new Vector<InputObject>();
		synchronized(inputs){
			for(InputObject im: inputs){
				if(im.round==plateau.getRound()){
					boolean toAdd = true;
					for(InputObject im2 : res){ // Check that this input is not duplicated
						if(im.team==im2.team && im.round==im2.round){
							toAdd=false;
							break;
						}
					}
					if(toAdd){						
						res.add(im);
					}
				}else if(im.round<plateau.getRound()){
					toRemove.add(im);
				}
			}
			if(res.size()>1){				
				inputs.removeAll(res);
				inputs.removeAll(toRemove);
			}
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
		client.sendTCP(Serializer.serialize(m));
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
		System.out.println("Mutex is locked for setting a new plateau");
		try {
			if(GameClient.getPlateau()!=null && plateau!=null){				
				System.out.println("New plateau round :"+getRound()+" round of new plateau : "+plateau.getRound() );
			}
			GameClient.plateau = plateau;
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			System.out.println("Mutex is unlocked after setting a new plateau");
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

	public static void close() {
		
		client.close();
		
	}

}
