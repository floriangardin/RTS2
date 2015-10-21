package multiplaying;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import model.Game;

public class MultiReceiver extends Thread{

	Game g;
	int port;
	public int received = 0;

	public DatagramSocket server;
	byte[] message;
	DatagramPacket packet;


	// DEBUGGING
	private boolean debug = true;

	public MultiReceiver(Game g, int port){
		this.g = g;
		this.port = port;
	}

	@Override
	public void run(){
		try{
			this.server = new DatagramSocket(port);
			if(debug)
				System.out.println("Cr�ation d'un receiver - " + port);
			while(!server.isClosed()){
				message = new byte[8000];
				packet = new DatagramPacket(message, message.length);
				try{
					server.receive(packet);
				} catch(java.net.SocketException e){
					break;
				}
				
				String msg = new String(packet.getData());
				if(debug) System.out.println("port : " + port + " message received: " + msg);
				if(msg.length()>0){
					int c = Integer.parseInt(msg.substring(0,1));
					switch(c){
					case 0: InputModel im = new InputModel(msg.substring(1, msg.length()));this.g.inputs.add(im);break;
					case 1: this.g.outputs.addElement(msg.substring(1, msg.length()));;break;
					case 2: this.g.connexions.add(msg.substring(1, msg.length()));
					if(!this.g.host)
						this.g.addressHost = packet.getAddress();
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
