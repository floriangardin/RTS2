package model;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Point;

public class MediumArmor extends Armor {
	public MediumArmor(float x, float y, Plateau p, Character owner) {
		this.weight = 0.3f;
		this.damageReductor = 1.5f;
		this.color = Color.gray;
		this.p = p;
		this.collisionBox = new Point(x,y);
		this.setXY(x,y);
		this.setOwner(owner);
		this.lifePoints = 1.0f;
		this.name = "Medium Armor";
	}
	
	public void draw(Objet o){
		//TODO: draw according to orientation of owner
	}
}
