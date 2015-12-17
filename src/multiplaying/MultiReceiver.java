package multiplaying;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import main.Main;
import model.Game;

public class MultiReceiver extends Thread{
	Game g;
	int port;
	public int received = 0;

	public DatagramSocket server;
	byte[] message;
	DatagramPacket packet;


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
			this.server.setBroadcast(true);
			if(Game.debugReceiver)
				System.out.println("Creation d'un receiver - " + port);
			while(!server.isClosed()){
				if(Game.debugThread){
					System.out.println(this.getName());
				}
				message = new byte[4000];
				packet = new DatagramPacket(message, message.length);
				try{
					server.receive(packet);
				} catch(java.net.SocketException e){
					break;
				}
				String msg = new String(packet.getData());
				this.g.nbPaquetReceived++;
				if(msg.length()>0 && !packet.getAddress().equals(InetAddress.getLocalHost())){
					if(Game.debugReceiver) System.out.println("port : " + port + " message received: " + msg.substring(0,1));
					switch(msg.substring(0,1)){
					case "0":this.actionConnexion(msg.substring(1)); break;
					case "1":this.actionInput(msg.substring(1)); break;
					case "2":this.actionValidation(msg.substring(1)); break;
					case "3":this.actionResynchro(msg.substring(1)); break;
					case "4":this.actionPing(msg.substring(1)); break;
					case "5":this.actionChecksum(msg.substring(1)); break;
					case "6":this.actionChat(msg.substring(1)); break;
					default:
					}
					this.action(msg.substring(1));
				}
				Thread.sleep(0);
			}
		} catch (IOException | InterruptedException e) {
		} 
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
	public void actionInput(String message){
		//System.out.println(msg);
		InputObject io = new InputObject(message,g);
		if(Game.debugValidation){
			System.out.println("MultiReceiver line 63 input received at round "+ this.g.round);
		}
		//Send the validation for other players if the round is still ok
		if(this.g.round<io.round+InputHandler.nDelay){
			this.g.sendValidation(io.getMessageValidationToSend(g), io.player.id);
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
		// Ressources partag� le vecteur d'inputs de la mailbox..
		this.g.inputsHandler.validate(round, idPlayer,idValidator);
	}
	public void actionResynchro(String msg){
		System.out.println("Receive resynchro message");
		this.g.processSynchro = true;
		this.g.toParse= msg;
	}
	public void actionPing(String msg){
		System.out.println("Ping !");
		String[] valMessage = msg.split("\\|");
		int id = Integer.parseInt(valMessage[1]);
		if(g.host){
			if(id<g.players.size()){
				g.sendPing(msg);
			}
		} else {
			long time =Long.parseLong(valMessage[0]);
			this.g.clock.updatePing(time);
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
		System.out.println("Je regarde mon checksum");
		int round = Integer.parseInt(u[0]); 
		if(g.host){
		long ping =Long.parseLong( u[u.length-2]);
		this.g.clock.ping = ping;
		}
		//Calcul du delta
		int delta =(int) (this.g.clock.ping*Main.framerate/(1e9));
		int deltaMesure = this.g.round - round ;
		if((deltaMesure-delta)>1){
			g.antidrop = true;
		}
	}
	public void actionChat(String message){
		this.g.chatHandler.messages.add(new ChatMessage(message));
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

