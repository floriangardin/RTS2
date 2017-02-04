package model;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import multiplaying.MultiMessage;

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
}
