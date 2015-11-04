package main;

import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;

import model.Utils;

public class mainTestCalculDegueu {

	public static void main(String[] args) {

		
		Shape c1 = new Circle(0,0,20);
		Shape c2 = new Circle(15,0,20);
		long time = System.currentTimeMillis();
		boolean test;
		for(int i=0; i<20000000; i++){
			test = c1.intersects(c2);
		}
		System.out.println("intersects " + (System.currentTimeMillis()-time));
		time = System.currentTimeMillis();
		for(int i=0; i<20000000; i++){
			test = Utils.distance(c1.getCenterX(), c1.getCenterY(), c2.getCenterX(), c2.getCenterY())<c1.getBoundingCircleRadius();
		}
		System.out.println("getbcr " + (System.currentTimeMillis()-time));
		time = System.currentTimeMillis();
		for(int i=0; i<20000000; i++){
			test = Utils.distance(c1.getCenterX(), c1.getCenterY(), c2.getCenterX(), c2.getCenterY())<20;
		}
		System.out.println("getbcr " + (System.currentTimeMillis()-time));

		
	}


	
}
