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
		this.client.addListener(new Listener() {
			public void received (Connection connection, Object object) {
				if (object instanceof SerializedMessage) {
					MultiMessage request = MultiMessage.getMessageFromString(((SerializedMessage)object).msg);
					System.out.println("message reçu");
					Game.g.kryonetBuffer.add(request);
					MultiMessage response = new MultiMessage(null);
					response.text = "Thanks";
					connection.sendTCP(response);
				}
			}
		});
		
		Kryo kryo = client.getKryo();
	    kryo.register(SerializedMessage.class);
	    kryo.register(byte[].class);
	    kryo.register(byte.class);
	}
	
	public void send(MultiMessage msg){
		if(isConnected){
			this.client.sendTCP(new SerializedMessage(Serializer.serialize(msg)));			
		}
	}
}
