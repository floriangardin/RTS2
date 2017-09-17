package main;

import java.io.IOException;

import org.newdawn.slick.SlickException;

import com.esotericsoftware.kryonet.Server;

public strictfp class mainTestCalculDegueu {
	public static strictfp class Test1 {
		
	}
	public static strictfp class Test2 extends Test1{
		
	}
	public static strictfp class Test3 extends Test1{
		
	}
	public static void main(String[] args) throws SlickException {
		try {
			Process p = Runtime.getRuntime().exec("python python/main.py " + 1);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Plateau plateau = Map.createPlateau("testIA");
	}
}
