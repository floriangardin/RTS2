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

	public KryonetServer(int portTCP, int portUDP){
		this.server = new Server();
		this.server.start();
		try {
			this.server.bind(portTCP, portUDP);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.server.addListener(new Listener() {
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
		Kryo kryo = server.getKryo();
	    kryo.register(SerializedMessage.class);
	    kryo.register(byte[].class);
	    kryo.register(byte.class);

	}

	public void send(MultiMessage msg){
		if(this.server.getConnections().length>0){
			this.server.getConnections()[0].sendTCP(new SerializedMessage(Serializer.serialize(msg)));
		}
	}
}
