package model;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;

public class Water extends NaturalObjet {

	public Water(float x, float y, Plateau p) {
		this.x = x;
		this.y = y;
		float size = 1.0f;
		this.collisionBox = new Rectangle(x-size/2,y-size/2,size,size);
		this.color = Color.blue;
		this.p = p;
		this.lifePoints = 1.0f;
	}
	
	public void collision(Objet o){
		//TODO: collision
	}
	
	public void draw(Objet o){
		//TODO: draw according to orientation of owner
	}

}
