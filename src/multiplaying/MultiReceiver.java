package multiplaying;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;

import model.Game;
import model.Objet;

public class MultiReceiver extends Thread{

	Game g;
	int port;
	public int received = 0;

	public DatagramSocket server;
	byte[] message;
	DatagramPacket packet;


	// DEBUGGING


	public MultiReceiver(Game g, int port){
		this.g = g;
		this.port = port;
	}

	@Override
	public void run(){
		try{
			this.server = new DatagramSocket(port);
			if(Game.debugReceiver)
				System.out.println("Crï¿½ation d'un receiver - " + port);
			while(!server.isClosed()){
				if(g.isInMenu)
					message = new byte[256];
				else 
					message = new byte[8000];

				packet = new DatagramPacket(message, message.length);
				try{
					server.receive(packet);
				} catch(java.net.SocketException e){
					break;
				}
				String msg = new String(packet.getData());
				if(Game.debugReceiver) System.out.println("port : " + port + " message received: " + msg);
				this.g.nbPaquetReceived++;
				if(msg.length()>0){
					int c = Integer.parseInt(msg.substring(0,1));
					switch(c){
					case 2: 
						if(!this.g.host){
							this.g.addressHost = packet.getAddress();
						}
						HashMap<String, String> map = Objet.preParse(msg.substring(1));
						//						if(map.containsKey("clk")){
						//							long clockTime = Long.parseLong(map.get("clk"));
						//							if(!this.g.host){
						//								this.g.clock.synchro(clockTime);
						//							}
						//						}
						this.g.connexions.add(msg.substring(1, msg.length()));
						break;
					case 3:
						//Multi with sending inputs
						if(msg.length()>1){
							if(msg.substring(1, 2).equals("I")){
								InputObject io = new InputObject(msg.substring(2, msg.length()),g);

								if(Game.debugValidation){
									System.out.println("MultiReceiver line 63 input received at round "+ this.g.round);
								}
								//A message coming from other players is automatically validated for yourself


								//Send the validation for other players if the round is still ok
								if(this.g.round<io.round+InputHandler.nDelay){
									this.g.sendInputToPlayer(io.player, io.getMessageValidationToSend());
									this.g.inputsHandler.addToInputs(io);
									io.validate();
								}

							}

							else if(msg.substring(1, 2).equals("P")){
								System.out.println("Receive resynchro message");
								this.g.processSynchro = true;
								this.g.toParse= msg.substring(1);
							}
							//If validation message
							else if(msg.substring(1, 2).equals("V")){
								//Get the corresponding round and player
								String rawInput = msg.substring(1);

								if(Game.debugValidation){
									System.out.println("MultiReceiver line 69 validation received for round "+ this.g.round);	
								}
								String[] valMessage = rawInput.split("\\|");
								int round = Integer.parseInt(valMessage[1]);
								int idPlayer = Integer.parseInt(valMessage[2]);
								// Ressources partagï¿½ le vecteur d'inputs de la mailbox..
								this.g.inputsHandler.validate(round, g.getPlayerById(idPlayer));
							}
							//Checksum
							else if(msg.substring(1, 2).equals("C")){
								//Theoriquement je n'en reçois que si je suis host ( à deux joueurs )
								String[] mes = msg.substring(1).split("\\|");
								if(this.g.checksum.size()==0){
									System.out.println("Je suis en retard sur l'autre ! ");
								}
								else{
									int i = 0;
									while(i<this.g.checksum.size()){
										String[] checksum = this.g.checksum.get(i).substring(1).split("\\|");
										if(mes[1].equals(checksum[1]) && !mes[2].equals(checksum[2]) && !this.g.processSynchro){
											System.out.println("112 multireceiver : Desynchro ! "+mes[2]+" "+checksum[2]);
											System.out.println("112 Desynchro occured round "+mes[1]);
											System.out.println("112 And now in round "+this.g.round);
											//Si desynchro j'active le processus de parse
											this.g.processSynchro = true;
											this.g.sendParse = true;
										}
										i++;
									}
								}
							}

							else if(msg.substring(1, 2).equals("H")){
								String[] mes = msg.substring(1).split("\\|");


								if(this.g.clockSynchro.size()==0){
									System.out.println("Je suis en retard sur l'autre ! ");

								}
								else{
									int i = 0;
									while(i<this.g.clockSynchro.size()){
										String[] checksum = this.g.clockSynchro.get(i).substring(1).split("\\|");
										if(mes[1].equals(checksum[1]) && Math.abs(Long.parseLong(mes[2])-Long.parseLong((checksum[2])))>20000000L){
											//System.out.println("112 multireceiver : Clock Desynchro ! "+0.000001*(Long.parseLong(mes[2])-Long.parseLong((checksum[2])))+"ms");
										}
										i++;
									}
								}
							}
							
							else if(msg.substring(1, 2).equals("K")){
								String[] mes = msg.substring(1).split("\\|");
								System.out.println("Restart process to do");
								this.g.restartProcess = true;
								this.g.timeRestart = Long.parseLong(mes[1]);
							}
						}
						break;
					default:
					}
				}
			}

		} catch (SocketException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
