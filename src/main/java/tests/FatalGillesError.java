package tests;

import model.Game;

import org.newdawn.slick.SlickException;

public class FatalGillesError extends SlickException{
	
	
	
	public FatalGillesError(String message){
		super("Gilles pas content ===>  TEST FAILED : "+ message);
	}

}
