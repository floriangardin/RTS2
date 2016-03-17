package multiplaying;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import main.Main;
import model.Game;

import org.newdawn.slick.SlickException;

import tests.Test;

public class MultiReceiver extends Thread{
	public Game g;
	int port;
	public int received = 0;

	public DatagramSocket server;
	byte[] message;
	DatagramPacket packet;

	public int tempsReception = 0;
	public float moyenneReception = 0;
	public int nbReception = 0;
	public int dernierRoundRecu = 0;

	public static boolean debugReception = false;

	// DEBUGGING


	public MultiReceiver(Game g){
		this.g = g;
		this.port = g.port;
		this.setName("MultiReceiver "+port);
	}

	public void action(String msg){}

	@Override
	public void run(){
		try{
			this.server = new DatagramSocket(port);
			this.server.setSoTimeout(2);
			this.server.setBroadcast(true);
			if(Game.debugReceiver)
				System.out.println("Creation d'un receiver - " + port);
			while(!server.isClosed()){
				this.server.setBroadcast(g.isInMenu);
				if(Game.debugThread){
					System.out.println(this.getName());
				}
				message = new byte[10000];
				packet = new DatagramPacket(message, message.length);
				try{
					server.receive(packet);
					int a = (int)(System.nanoTime()/1e6);
					if(!Game.g.isInMenu){
						nbReception+=1;
						if(nbReception==1){
							tempsReception = (int) System.currentTimeMillis();
						}else{
							tempsReception = (int) (System.currentTimeMillis()-tempsReception);
						}
						if(debugReception)
							System.out.println("reception du message: "+ tempsReception);
						tempsReception = (int) System.currentTimeMillis();
					}
					//System.out.println("-------------- réception d'un message : " + a);
				} catch(java.net.SocketException e){
					break;
				}catch( java.net.SocketTimeoutException e){
					System.out.println("Multi Receiver : Socket Timeout");
					continue;
					
				}
				String msg = new String(packet.getData());
				if(Game.debugReceiver) 
					System.out.println(msg.substring(0, 200));
				//Split submessages
				String[] tab = msg.split("\\%");
				String temp;

				// TODO : check if input in message
				if(Game.tests && !this.g.isInMenu){
					Test.testIfInputInMessage(msg);
					int round = getRoundFromMessage(msg);
					Test.testOrderedMessages(round);
					Test.testNombreMessagesRecus(round);
					System.out.println("reception du message: "+ round+" on est au round " +Game.g.round);
				}

				for(int i =0; i<tab.length;i++){
					temp = tab[i];
					this.g.nbPaquetReceived++;
					if(temp.length()>0 && !packet.getAddress().equals(InetAddress.getLocalHost())){
						//if(Game.debugReceiver) System.out.println("port : " + port + " message received: " + temp);
						switch(temp.substring(0,1)){
						case "0":this.actionConnexion(temp.substring(1)); break;
						case "1":this.actionInput(temp.substring(1)); break;
						case "2":this.actionValidation(temp.substring(1)); break;
						case "3":this.actionResynchro(temp.substring(1)); break;
						case "4":this.actionPing(temp.substring(1)); break;
						case "5":this.actionChecksum(temp.substring(1)); break;
						case "6":this.actionChat(temp.substring(1)); break;
						default:
						}
						this.action(msg.substring(1));
					}
				}
				Thread.sleep(0);
			}
		} catch (IOException | InterruptedException | SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	public int getRoundFromMessage(String msg){
		String[] tab = msg.split("\\%");
		String temp;
		int ordre = 0;
		for(int i =0; i<tab.length;i++){
			temp = tab[i];
			if(temp.length()>0 && temp.substring(0,1).equals("1")){
				ordre = Integer.parseInt(temp.substring(1).split(",")[1].substring(4));
				break;
			}
		}
		return ordre;
	}

	public void shutdown(){
		this.server.close();
	}

	public void actionConnexion(String message){
		if(!this.g.host){
			this.g.addressHost = packet.getAddress();
		}
		//HashMap<String, String> map = Objet.preParse(msg.substring(1));
		this.g.receivedConnexion.add(message);		
	}
	public void actionInput(String message) throws SlickException{
		//System.out.println(msg);
		InputObject io = new InputObject(message,g);
		if(Game.debugValidation){
			System.out.println("MultiReceiver line 63 input received at round "+ this.g.round);
		}
		//Send the validation for other players if the round is still ok
		if(this.g.round<io.round+Main.nDelay){
			this.g.toSendThisTurn+="2"+io.getMessageValidationToSend(g)+"%";
			this.g.inputsHandler.addToInputs(io);
			io.validate();
		}
	}
	public void actionValidation(String msg){
		//Get the corresponding round and player
		String rawInput = msg;
		String[] valMessage = rawInput.split("\\|");
		int round = Integer.parseInt(valMessage[0]);
		int idPlayer = Integer.parseInt(valMessage[1]);
		int idValidator = Integer.parseInt(valMessage[2]);
		if(Game.debugValidation){
			System.out.println("MultiReceiver line 69 validation received for round "+ round);	
		}
		// Ressources partagï¿½ le vecteur d'inputs de la mailbox..
		this.g.inputsHandler.validate(round, idPlayer,idValidator);
	}
	public void actionResynchro(String msg){
		//		System.out.println("Receive resynchro message");
		this.g.processSynchro = true;
		this.g.toParse= msg;
	}
	public void actionPing(String msg){
		String[] valMessage = msg.split("\\|");
		int id = Integer.parseInt(valMessage[1]);

		if(id==g.currentPlayer.id) {
			long time =Long.parseLong(valMessage[0]);
			this.g.clock.updatePing(time);
		}else if(g.host){
			if(id<g.players.size()){
				g.toSendThisTurn+="4"+(msg)+"%";
			}
		}
	}
	public void actionChecksum(String msg){

		if(g.host){
			this.g.mutexChecksum.lock();
			this.g.receivedChecksum.addElement(new Checksum(msg));
			this.g.mutexChecksum.unlock();
		}
		// HANDLE ANTI-DROP 
		String[] u = msg.split("\\|");
		int round = Integer.parseInt(u[0]); 

		if(g.host){
			long ping =Long.parseLong( u[u.length-2]);
			this.g.clock.ping = ping;
		}
		//		System.out.println("Je regarde le checksum du round  "+round+ " au round " +g.round );
		//		System.out.println("Et le ping .. " +g.clock.ping);
		//Calcul du delta
		int delta =(int) (this.g.clock.ping*Main.framerate/(2e9));
		int deltaMesure = this.g.round - round ;
		if((deltaMesure-delta)>1){
			g.antidrop = true;
		}
	}
	public void actionChat(String message){
		ChatMessage cm = new ChatMessage(message);
		this.g.chatHandler.messages.add(cm);
		if(cm.message.charAt(0)=='/'){
			this.g.taunts.playTaunt(cm.message.substring(1).toLowerCase());
		}
	}


	public static class Checksum {
		public int round;
		public String checksum;
		public Checksum(String checksum){
			String[] tab = checksum.split("\\|");
			this.round = Integer.parseInt(tab[0]);
			this.checksum = tab[1];
		}
		public boolean[] comparison(Checksum c){
			boolean[] tab = new boolean[2];
			tab[0] = (c.round==this.round);
			tab[1] = (!c.checksum.equals(this.checksum));
			return tab;
		}
	}
}

