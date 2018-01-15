package model;

import java.io.Serializable;

import control.InputObject;
import menuutils.Menu_Player;
import multiplaying.ChatMessage;
import multiplaying.Checksum;
import plateau.Plateau;

public strictfp class Message implements Serializable{
	
	public final transient static int PLATEAU = 0;
	public final transient static int INPUTOBJECT = 1;
	public final transient static int CHECKSUM = 2;
	public final transient static int MENUPLAYER = 3;
	public final transient static int CHATMESSAGE = 4;
	public final transient static int SYSTEMMESSAGE = 5;
	
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
	public Message(Menu_Player connectionId){
		objet = Serializer.serialize(connectionId);
		type = MENUPLAYER;
	}
	public Message(ChatMessage message){
		objet = Serializer.serialize(message);
		type = CHATMESSAGE;
	}
	public Message(String systemMessage){
		objet = Serializer.serialize(systemMessage);
		type = SYSTEMMESSAGE;
	}
	
	public int getType(){
		return type;
	}
	public Object get(){
		return Serializer.deserialize(objet);
	}

}
