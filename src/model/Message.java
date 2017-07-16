package model;

import java.io.Serializable;

import control.InputObject;
import multiplaying.Checksum;
import plateau.Plateau;

public class Message implements Serializable{
	
	public final transient static int PLATEAU = 0;
	public final transient static int INPUTOBJECT = 1;
	public final transient static int CHECKSUM = 2;
	public final transient static int CONNECTION = 3;
	
	private byte[] objet ;
	private int type = -1;
	
	public Message(){};
	public Message(Plateau plateau){
		objet = Serializer.serialize(plateau);
		type = PLATEAU;
	}
	public Message(InputObject im){
		objet = Serializer.serialize(im);
		type = INPUTOBJECT;
	}
	public Message(Checksum checksum){
		objet = Serializer.serialize(checksum);
		type = CHECKSUM;
	}
	public Message(int connectionId){
		objet = Serializer.serialize(connectionId);
		type = CONNECTION;
	}
	
	public int getType(){
		return type;
	}
	public Object get(){
		return Serializer.deserialize(objet);
	}

}
