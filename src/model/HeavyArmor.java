package model;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Point;

public class HeavyArmor extends Armor {
	public HeavyArmor(float x, float y, Plateau p, Character owner) {
		// Parameters
		this.weight = 0.4f;
		this.damageReductor = 2.5f;
		
		//
		
		this.color = Color.gray;
		this.p = p;
		this.collisionBox = new Point(x,y);
		this.setXY(x,y);
		this.setOwner(owner);
		this.lifePoints = 1.0f;
		this.name = "Heavy Armor";
	}
	
	public void draw(Objet o){
		//TODO: draw according to orientation of owner
	}
}
