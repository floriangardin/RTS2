package model;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Shape;

public class LightArmor extends Armor {

	public LightArmor(float x, float y, Plateau p, Character owner) {
		this.weight = 0.2f;
		this.damageReductor = 0.5f;
		this.color = Color.gray;
		this.p = p;
		this.collisionBox = new Point(x,y);
		this.setXY(x,y);
		this.setOwner(owner);
		this.lifePoints = 1.0f;
		this.name = "Light Armor";
	}
	
	public void draw(Objet o){
		//TODO: draw according to orientation of owner
	}
}
