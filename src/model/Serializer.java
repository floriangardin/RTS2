package model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import control.InputObject;
import multiplaying.MultiMessage;
import plateau.Plateau;

public class Serializer {
	public static byte[] serialize(Object o){
		byte[] serializedObject = new byte[0];
		// serialize the object
		try {
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream so = new ObjectOutputStream(bo);
			so.writeObject(o);
			so.flush();
			serializedObject = bo.toByteArray();
			if(o instanceof MultiMessage && ((MultiMessage)o).resynchro!=null ){
				System.out.println("Serializer line 16 size of buffer : "+ serializedObject.length);
			}
		
		} catch (Exception e) {
			System.out.println(e);
		}
		return serializedObject;		
	}
	
	public static InputObject deserialize(byte[] serializedObject){
		// deserialize the object
		try {
			ByteArrayInputStream bi = new ByteArrayInputStream(serializedObject);
			ObjectInputStream si = new ObjectInputStream(bi);
			return (InputObject) si.readObject();
		} catch (Exception e) {
			e.printStackTrace();
			return new InputObject();
		}

	}
	
	public static Plateau deserializePlateau(byte[] serializedObject) throws IOException, ClassNotFoundException{
		// deserialize the object
		
	
		ByteArrayInputStream bi = new ByteArrayInputStream(serializedObject);
		ObjectInputStream si;
		si = new ObjectInputStream(bi);
		return (Plateau) si.readObject();
		
		
	}
}
