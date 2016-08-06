package multiplaying;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.util.Vector;

import control.InputObject;
import model.Plateau;

public class MultiMessage implements java.io.Serializable{

	
	public InetAddress address;
	
	public Vector<String> connexion;
	public Vector<InputObject> input;
	public Vector<String> validation;
	public Plateau resynchro;
	public Vector<String> ping;
	public Vector<String> checksum;
	public Vector<String> chat;
	
	
	/**
	 * 0 connexion
	 * 1 input
	 * 2 validation
	 * 3 resynchro
	 * 4 ping
	 * 5 checksum
	 * 6 chat
	 * 
	 */
	
	public MultiMessage(InetAddress address) {
		this.address = address;
		this.connexion = new Vector<String>();
		this.input = new Vector<InputObject>();
		this.validation = new Vector<String>();
		this.resynchro = null;
		this.ping = new Vector<String>();
		this.checksum = new Vector<String>();
		this.chat = new Vector<String>();
	}
	
	public static MultiMessage getMessageFromString(byte[] serializedObject){
		// deserialize the object
		try {
			ByteArrayInputStream bi = new ByteArrayInputStream(serializedObject);
			ObjectInputStream si = new ObjectInputStream(bi);
			return (MultiMessage) si.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			return new MultiMessage(null);
		}

	}
	
	
}
