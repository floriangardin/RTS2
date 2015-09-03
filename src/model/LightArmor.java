package model;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Point;

public class LightArmor extends Armor {

	public LightArmor(float x, float y, Plateau p, Character owner) {
		// Parameters
		this.weight = 0.2f;
		this.damageReductor = 0.5f;
		this.name = "Light Armor";
		
		//
		
		this.color = Color.gray;
		this.p = p;
		this.collisionBox = new Point(x,y);
		this.setXY(x,y);
		this.setOwner(owner);
		this.lifePoints = 1.0f;
	}
	
	public void draw(Objet o){
		//TODO: draw according to orientation of owner
	}
}
