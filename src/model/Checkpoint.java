package model;

import org.newdawn.slick.geom.Point;

public class Checkpoint extends Objet {
	
	public Checkpoint(float x, float y){
		this.collisionBox = new Point(x,y);
		this.setXY(x, y);
				
	}
}
