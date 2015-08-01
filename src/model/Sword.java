package model;

import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;

public class Sword extends ContactWeapon {
	protected Line collisionBox ;
	
	
	public Sword(Character owner){
		this.damage = 10f;
		this.frequency = 10f;
		this.lifePoints = 1f;
		this.setOwner(owner);
		this.collisionBox = new Line(0f,0f,10f,0f);
		
		
		this.setXY(owner.getX(),owner.getY());
		
	}
}
