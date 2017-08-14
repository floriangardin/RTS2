package model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import control.InputObject;
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
		
		} catch (Exception e) {
			System.out.println(e);
		}
		return serializedObject;		
	}
	
	public static Object deserialize(byte[] serializedObject){
		// deserialize the object
		ByteArrayInputStream bi = new ByteArrayInputStream(serializedObject);
		ObjectInputStream si;
		try {
			si = new ObjectInputStream(bi);
			return si.readObject();
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
}
