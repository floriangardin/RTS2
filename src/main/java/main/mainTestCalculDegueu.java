package main;

import org.newdawn.slick.SlickException;

import com.esotericsoftware.kryonet.Server;

public class mainTestCalculDegueu {
	public static class Test1 {
		
	}
	public static class Test2 extends Test1{
		
	}
	public static class Test3 extends Test1{
		
	}
	public static void main(String[] args) throws SlickException {
		Server server = new Server(500000, 50000);
		//Plateau plateau = Map.createPlateau("testIA");
	}
}
