package model;

import java.util.Vector;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import control.InputObject;
import multiplaying.MultiMessage;

public class SimpleMultiplayer extends Listener{
	public Vector<InputObject> messages = new Vector<InputObject>();
	static final int port = 27960;
	public Server server;
	public Client client;
	public String ip = "localhost";
	
	public SimpleMultiplayer(){
		server = new Server();
		client = new Client();
		server.start();
		client.start();
		
		client.getKryo().register(byte[].class);
		server.getKryo().register(byte[].class);
	}
	
	public void received(Connection c, Object o){
		byte[] receivedMessage = (byte[]) o;
		// De serialize
		InputObject im = Serializer.deserialize(receivedMessage);
		messages.add(im);
	}
	public void connected(Connection c){
		
	}
	public void disconnected(Connection c){
		
	}
	
	public static byte[] serialize(InputObject im){
		return Serializer.serialize(im);
	}
	public static InputObject serialize(byte[] im){
		return Serializer.deserialize(im);
	}
	
	public static void main(String[] args) {
		
		SimpleMultiplayer multi = new SimpleMultiplayer();
		
	}
	
	
	
}
