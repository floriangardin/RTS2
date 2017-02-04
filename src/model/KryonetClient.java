package model;

import java.io.IOException;
import java.net.InetAddress;

import com.esotericsoftware.kryonet.Client;

import multiplaying.MultiMessage;

public class KryonetClient {

	public Client client;
	private boolean isConnected;
	
	public KryonetClient(){
		this.client = new Client();
		client.start();
	}
	
	public void connect(InetAddress address, int portTCP, int portUDP){
		try {
			client.connect(5000, address.toString().substring(1), portTCP, portUDP);
			isConnected = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void send(MultiMessage msg){
		if(isConnected){
			this.client.sendTCP(msg);			
		}
	}
}
