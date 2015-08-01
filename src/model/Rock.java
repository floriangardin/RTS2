package model;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;

public class Rock extends NaturalObjet {

	public Rock(float x, float y, Plateau p) {
		float size = 1.0f;
		this.collisionBox = new Rectangle(x-size/2,y-size/2,size,size);
		this.color = Color.black;
		this.p = p;
		this.lifePoints = 1.0f;
		this.setXY(x, y);
	}
	
	public void collision(Objet o){
		//TODO: collision
	}
	
	public void draw(Objet o){
		//TODO: draw according to orientation of owner
	}
}
