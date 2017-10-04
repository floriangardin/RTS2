package main;

import java.io.File;
import java.io.IOException;

import org.newdawn.slick.SlickException;

import com.esotericsoftware.kryonet.Server;

import model.Options;

public strictfp class mainTestCalculDegueu {
	public static strictfp class Test1 {
		
	}
	public static strictfp class Test2 extends Test1{
		
	}
	public static strictfp class Test3 extends Test1{
		
	}
	public static void main(String[] args) throws SlickException {
		try {
			ProcessBuilder pb = new ProcessBuilder("/usr/local/bin/python3","python/main.py", ""+1);
			//pb.directory(new File("/usr/local/bin/"));
			pb.inheritIO();
			pb.start();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Plateau plateau = Map.createPlateau("testIA");
	}
}
