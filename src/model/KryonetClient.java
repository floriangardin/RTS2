package model;

import java.io.IOException;
import java.net.InetAddress;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import multiplaying.MultiMessage;
import multiplaying.SerializedMessage;

public class KryonetClient {

	public Client client;
	public Client clientResynchro;
	private boolean isConnected;
	
	public KryonetClient(){
		this.client = new Client();
		this.clientResynchro = new Client(5000000, 5000000);
		client.start();
		clientResynchro.start();
	}
	
	public void connect(InetAddress address, int portTCP, int portUDP, int portTCPResynchro, int portUDPResynchro){
		try {
			client.connect(5000, address.toString().substring(1), portTCP, portUDP);
			clientResynchro.connect(5000, address.toString().substring(1), portTCPResynchro, portUDPResynchro);
			isConnected = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.client.addListener(new Listener() {
			public void received (Connection connection, Object object) {
				if (object instanceof SerializedMessage) {
					MultiMessage request = MultiMessage.getMessageFromString(((SerializedMessage)object).msg);
					Game.g.kryonetBuffer.add(request);
				}
			}
		});
		this.clientResynchro.addListener(new Listener() {
			public void received (Connection connection, Object object) {
//				System.out.println("Kryonet client l47 : has received resynchro message");
//				System.out.println(object.getClass());
				if (object instanceof SerializedMessage) {
//					System.out.println("message Resynchro recu 1");
					MultiMessage request = MultiMessage.getMessageFromString(((SerializedMessage)object).msg);
//					System.out.println("message Resynchro recu 2");
					Game.g.kryonetBuffer.add(request);
				}
			}
		});
		
		Kryo kryo = client.getKryo();
	    kryo.register(SerializedMessage.class);
	    kryo.register(byte[].class);
	    kryo.register(byte.class);
	    
		Kryo kryoResynchro = clientResynchro.getKryo();
	    kryoResynchro.register(SerializedMessage.class);
	    kryoResynchro.register(byte[].class);
	    kryoResynchro.register(byte.class);
	}
	
	public void send(MultiMessage msg){
		if(isConnected){
			this.client.sendTCP(new SerializedMessage(Serializer.serialize(msg)));			
		}
	}
	
}
