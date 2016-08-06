package main;

import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Vector;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;

import ressources.Map;
import utils.Utils;
import data.Data;
import units.Character;
public class mainTestCalculDegueu {
	public static class Test1 {
		
	}
	public static class Test2 extends Test1{
		
	}
	public static class Test3 extends Test1{
		
	}
	public static void main(String[] args) throws SlickException {
		System.out.println(-1%5);
		Test1 t1 = new Test1();
		Test2 t2 = new Test2();
		
		System.out.println(Test2.class.isAssignableFrom(Test3.class));
		
		
	}
}
