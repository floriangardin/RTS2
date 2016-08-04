package model;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

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
}
