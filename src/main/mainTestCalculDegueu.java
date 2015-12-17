package main;

import java.util.GregorianCalendar;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;

import model.Utils;

public class mainTestCalculDegueu {
	
	public static void main(String[] args) throws SlickException {
		String format = "dd/MM/yyyy";
		
		String test = "a;b;c;";
		String[] a = test.split(";");
		
		for(int i =0; i<a.length;i++){
			System.out.println(a[i]);
		}
		System.out.println("end");
	}
}
