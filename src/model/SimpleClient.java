package model;


import java.io.IOException;
import java.util.Vector;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import control.InputObject;
import plateau.Plateau;


public class SimpleClient extends Listener {

	Client client;
	String ip = "localhost";
	int port = 27960;
	Plateau plateau;
	Vector<InputObject> inputs = new Vector<InputObject>();
	
	public static void main(String[] args) throws IOException, InterruptedException{
		SimpleClient client = new SimpleClient();
		client.connect();
		byte[] input = Serializer.serialize(new InputObject());
		while(true){
			client.client.sendTCP(input);
			Thread.sleep(100);
		}	
	}
	
	public void send(InputObject im){
		client.sendTCP(Serializer.serialize(im));
	}

	public synchronized Plateau getPlateau(){
		return plateau;
	}
	public synchronized void setPlateau(Plateau plateau){
		this.plateau = plateau;
	}

	public void connect(){
		client = new Client(5000000, 5000000);
		client.getKryo().register(byte[].class);
		client.getKryo().register(String.class);
		client.addListener(this);
		client.start();
		try {
			client.connect(5000, ip, port, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public synchronized void addInput(InputObject im){
		inputs.add(im);
	}
	public synchronized Vector<InputObject> getInputs(){
		Vector<InputObject> res = new Vector<InputObject>();
		res.addAll(inputs);
		inputs.clear();
		return res;
	}
	public void received(Connection c, Object o){
		if(o instanceof byte[]){
			InputObject im= Serializer.deserialize((byte[])o); 
			addInput(im);
			
//			try {
//				this.setPlateau((Plateau) Serializer.deserializePlateau((byte[])o) );
//			} catch (ClassNotFoundException | IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
		}else if(o instanceof String){
			String s = (String) o;
			System.out.println(s);
		}
	}
}
