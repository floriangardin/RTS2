package model;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
// Handle cosmetic for lan game to avoid feeling of latency
public class Cosmetic {
	
	Rectangle selection;
	float recX;
	float recY;
	
	public Cosmetic(){

	}
	
	public Graphics draw(Graphics g){
		g.setLineWidth(1f);
		g.draw(selection);
		
//		System.out.println("v1 : "+selection.getX()+" "+selection.getY());
		// Draw Input handler for debug
	

		return g;
	}
	
}
