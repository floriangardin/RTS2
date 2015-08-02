package model;

import org.newdawn.slick.geom.Circle;

public class Bow extends RangeWeapon{
	
	public Bow(Character owner){
		
		this.range = 100;
		this.setOwner(owner);
		this.collisionBox = new Circle(owner.getX(),owner.getY(),owner.collisionBox.getBoundingCircleRadius());
		this.setXY(owner.getX(),owner.getY());

	}
}
