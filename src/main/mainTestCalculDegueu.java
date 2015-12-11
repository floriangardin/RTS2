package main;

import java.util.GregorianCalendar;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;

import model.Utils;

public class mainTestCalculDegueu {
	
	public static void main(String[] args) throws SlickException {
		String format = "dd/MM/yyyy";

		java.text.SimpleDateFormat formater = new java.text.SimpleDateFormat( format );
		java.util.Date date = new java.util.Date();

		System.out.println( formater.format( date ) );
	}
}
