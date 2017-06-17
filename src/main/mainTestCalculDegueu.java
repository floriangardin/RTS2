package main;

import org.newdawn.slick.SlickException;

import plateau.Plateau;
import ressources.Map;

public class mainTestCalculDegueu {
	public static class Test1 {
		
	}
	public static class Test2 extends Test1{
		
	}
	public static class Test3 extends Test1{
		
	}
	public static void main(String[] args) throws SlickException {
		Plateau plateau = Map.createPlateau("testIA");
	}
}
