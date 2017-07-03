package model;

import java.io.IOException;
import java.io.Serializable;

import control.InputObject;
import plateau.Plateau;

public class Message implements Serializable{
	
	public final transient static int PLATEAU = 0;
	public final transient static int INPUTOBJECT = 1;
	private byte[] objet ;
	private int type = -1;
	
	InputObject input;
	
	public Message(){};
	public Message(Plateau plateau){
		objet = Serializer.serialize(plateau);
		type = PLATEAU;
	}
	public Message(InputObject im){
		objet = Serializer.serialize(im);
		type = INPUTOBJECT;
	}
	
	public int getType(){
		return type;
	}
	public Object get(){
		return Serializer.deserialize(objet);
	}

}
