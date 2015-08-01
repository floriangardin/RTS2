package model;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Shape;

public class LightArmor extends Armor {

	public LightArmor(float x, float y, Plateau p) {
		this.weight = 0.8f;
		this.damageReductor = 0.5f;
		this.color = Color.gray;
		this.p = p;
		this.x = x;
		this.y = y;
		this.collisionBox = new Point(x,y);
		this.lifePoints = 1.0f;
		
	}
	
	public void draw(Objet o){
		//TODO: draw according to orientation of owner
	}
}
