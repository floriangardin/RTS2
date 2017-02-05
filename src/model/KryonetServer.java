package model;

import java.io.IOException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import multiplaying.MultiMessage;
import multiplaying.SerializedMessage;

public class KryonetServer {

	public Server server;
	public Server serverResynchro;

	public KryonetServer(int portTCP, int portUDP, int portTCPResynchro,int portUDPResynchro){
		this.server = new Server();
		this.serverResynchro = new Server(500000, 500000);
		this.server.start();
		this.serverResynchro.start();
		try {
			this.server.bind(portTCP, portUDP);
			this.serverResynchro.bind(portTCPResynchro, portUDPResynchro);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.server.addListener(new Listener() {
			public void received (Connection connection, Object object) {
				if (object instanceof SerializedMessage) {
					MultiMessage request = MultiMessage.getMessageFromString(((SerializedMessage)object).msg);
					Game.g.kryonetBuffer.add(request);
//					MultiMessage response = new MultiMessage(null);
//					response.text = "Thanks";
//					connection.sendTCP(response);
				}
			}
		});
		Kryo kryo = server.getKryo();
	    kryo.register(SerializedMessage.class);
	    kryo.register(byte[].class);
	    kryo.register(byte.class);
	    
		Kryo kryoResynchro = serverResynchro.getKryo();
	    kryoResynchro.register(SerializedMessage.class);
	    kryoResynchro.register(byte[].class);
	    kryoResynchro.register(byte.class);

	}

	public void send(MultiMessage msg){
		if(this.server.getConnections().length>0){
			this.server.getConnections()[0].sendTCP(new SerializedMessage(Serializer.serialize(msg)));
		}
	}
	public void sendResynchro(MultiMessage msg){
		if(this.serverResynchro.getConnections().length>0){
			this.serverResynchro.getConnections()[0].sendTCP(new SerializedMessage(Serializer.serialize(msg)));
		}
	}
}
