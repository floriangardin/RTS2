package model;


import java.io.IOException;
import java.util.Vector;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import control.InputObject;
import plateau.Plateau;


public class SimpleClient extends Listener {
	
	//OPTIONS
	private static Client client;
	private static String ip = "localhost"; //FOR SINGLEPLAYER
	private static int port = 27960;
	// STATE
	private static Plateau plateau; // Mutable State side effect ...
	private static Vector<InputObject> inputs = new Vector<InputObject>();


	public static void init(Plateau plateau){
		SimpleClient.plateau = plateau;
		client = new Client(5000000, 5000000);
		client.getKryo().register(byte[].class);
		client.getKryo().register(Integer.class);
		client.getKryo().register(Message.class);
		client.getKryo().register(String.class);
		client.addListener(new Listener(){
			public void received(Connection c, Object o){
				
				if(o instanceof Message){
					Message m = (Message) o;
					int type = m.getType();
					if(type==Message.PLATEAU){
						System.out.println("Youpi un plateau");
						Plateau plateau = (Plateau) m.get();
						SimpleClient.setPlateau(plateau);
					}else if(type==Message.INPUTOBJECT){
						InputObject im = (InputObject)m.get();
						SimpleClient.addInput(im);
					}
				}
			}
		});
		client.start();
		try {
			client.connect(5000, ip, port, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void send(InputObject im){
		Message m = new Message(im);
		client.sendTCP(m);
	}
	
	public static void send(Plateau plateau){
		Message m = new Message(plateau);
		client.sendTCP(Serializer.serialize(m));
	}

	public synchronized static Plateau getPlateau(){
		return plateau;
	}
	public synchronized static void setPlateau(Plateau plateau){
		SimpleClient.plateau = plateau;
	}
	public synchronized static void addInput(InputObject im){
		inputs.add(im);
	}
	public synchronized static Vector<InputObject> getInputs(){
		Vector<InputObject> res = new Vector<InputObject>();
		res.addAll(inputs);
		inputs.clear();
		return res;
	}
	
	
}
