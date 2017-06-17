package multiplaying;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Vector;

import model.Game;
import model.KryonetClient;
import model.KryonetServer;
import model.Serializer;
import plateau.Plateau;
import ressources.Taunts;
import system.Debug;
import tests.FatalGillesError;

public class Communications {
	
	
	/////////////////////////////
	/// NETWORK & MULTIPLAYING///
	/////////////////////////////
	
	private static DatagramSocket normalClient;
	private static KryonetServer kryonetServer;
	private static KryonetClient kryonetClient;
	private static DatagramSocket normalServer;
	

	//Clock
	public static Clock clock;
	public boolean inMultiplayer;
	public boolean host = false;
	public long startTime;
	// addresses
	public static InetAddress addressLocal;
	private static InetAddress addressBroadcast;
	public static InetAddress addressHost;
	// ports
	public static int portUDP = 2302;
	public static int portUDPKryonet = 2303;
	public static int portTCP = 54555;
	public static int portUDPKryonetResynchro = 2304;
	public static int portTCPResynchro = 54556;
	
	// depots for senders
	public static boolean usingKryonet = true;
	public Vector<MultiMessage> kryonetBuffer = new Vector<MultiMessage>();
	int nbReception, tempsReception;
	// depots for receivers
	public static Vector<String> receivedConnexion = new Vector<String>();
	public static Vector<String> receivedValidation = new Vector<String>();
	public static Vector<String> receivedPing = new Vector<String>();
	public static Vector<Checksum> receivedChecksum = new Vector<Checksum>();
	public static Vector<String> receivedChat = new Vector<String>();
	
	public ChatHandler chatHandler;
	private Plateau toParse;
	public static MultiMessage toSendThisTurn;
	public static int idInput = 0;
	private static int idPaquetSend;


	public static void init(){
		try {
			addressLocal = InetAddress.getLocalHost();
			String address = addressLocal.getHostAddress();
			String[] tab = address.split("\\.");
			address = tab[0]+"."+tab[1]+"."+tab[2]+".255";
			addressBroadcast = InetAddress.getByName(address);
			normalClient = new DatagramSocket();
			normalClient.setSendBufferSize(500000);
			//		System.out.println("Game line 1429 : " +normalClient.getSendBufferSize());
			kryonetServer = new KryonetServer(portTCP, portUDPKryonet, portTCPResynchro, portUDPKryonetResynchro);
			kryonetClient = new KryonetClient();
			normalServer = new DatagramSocket(portUDP);
			normalServer.setSoTimeout(1);
			normalServer.setBroadcast(true);				
		} catch (IOException e) {
			e.printStackTrace();
		}
		clock = new Clock();
		//		g.clock.start();
		ChatHandler.init();
	}
	
	public static void sendFromMenu(String message) throws FatalGillesError{
		//si on est sur le point de commencer à jouer, on n'envoit plus de requête de ping
		if(Game.isInMenu()){
			// on gère les connexions de menumapchoice

			if(Game.gameSystem.host){
				MultiMessage m = new MultiMessage(addressBroadcast);
				m.connexion.add(message);
				send(m);
				for(InetAddress ia : Game.menuSystem.menuMapChoice.lobby.addressesInvites){
					MultiMessage m1 = new MultiMessage(ia);
					m.connexion.add(message);
					send(m1);
				}
			} else {
				MultiMessage m = new MultiMessage(addressHost);
				m.connexion.add(message);
				send(m);
			}
		}
	}

	//private static long timeToSend;

	public static void sendFromGame(MultiMessage m) throws FatalGillesError{
//		for(int i=1; i<this.nPlayers; i++){
//			if(i!=currentPlayer.id){
//				m.address = this.players.get(i).address;
//				this.send(m);
//			}
//		}
	}

	private static void send(MultiMessage m) throws FatalGillesError{
		//		if(!isInMenu){
		//			if(timeToSend!=0L)
		//				System.out.println("dernier message envoyé il y a: "+(System.nanoTime()-timeToSend)/1000);
		//			//			if((System.nanoTime()-timeToSend)/1000<3000)
		//			System.out.println("   = >  "+m.message);
		//			timeToSend= System.nanoTime();
		//		}
		idPaquetSend++;
		if(usingKryonet && !Game.isInMenu()){	
			if(Game.gameSystem.host){
				if(m.resynchro!=null){
					kryonetServer.sendResynchro(m);
				}else {
					kryonetServer.send(m);
				}
			} else {
				kryonetClient.send(m);
			}
		} else {
			InetAddress address = m.address;
			byte[] message = Serializer.serialize(m);
			DatagramPacket packet = new DatagramPacket(message, message.length, address, portUDP);
			packet.setData(message);
			try {
				normalClient.setSendBufferSize(500000);
//				System.out.println("sending message Game line 1506 (pour l'instant) size:"+normalClient.getSendBufferSize());
				normalClient.send(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
		if(Debug.debugSender)
			System.out.println("port : " + portUDP + " address: "+m.address.getHostAddress()+" message sent: " + m.toString());
	}
	
	public static void pingRequest() {
//		toSendThisTurn.ping.addElement(clock.getCurrentTime()+"|"+Game.gameSystem.currentPlayer.id+"|");
	}
	public static void sendMessage(ChatMessage m){
//		sendMessage(m, Game.g.currentPlayer.id);
	}
	public static void sendMessage(ChatMessage m, int idPlayer){
		if(m.idPlayer==idPlayer){
			toSendThisTurn.chat.addElement(m.toString());
		}
		//if(!m.message.equals("/gillesBombe"))
		ChatHandler.messages.addElement(m);
		if(m.message.charAt(0)=='/'){
			Taunts.playTaunt(m.message.substring(1).toLowerCase());
		}
	}

	

}
