package model;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class Horse extends RidableObjet{

	public Horse(float x, float y, Plateau p, Objet owner){
		this.x = x;
		this.y = y;
		this.p = p;
		float collisionWidth = 5f;
		float collisionLength = 10f;
		this.collisionBox = new Rectangle(x-collisionWidth/2f,y-collisionLength/2f,collisionWidth,collisionLength);
		this.color = Color.orange;
		this.lifePoints = 1.0f;
		this.velocity = 5f;
		this.owner = owner;
	}
	
	public void collision(Objet o){
		//TODO: collision
	}
	
	public void draw(Objet o){
		//TODO: draw according to orientation of owner
	}
	
	

}
