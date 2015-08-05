package model;

import org.newdawn.slick.geom.Point;

public class Checkpoint extends Objet {
	
	public Checkpoint(float x, float y){
		this.lifePoints=1f;
		this.collisionBox = new Point(x,y);
		this.setXY(x, y);
				
	}
}
