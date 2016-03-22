package model;

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
		return g;
	}
	
}
