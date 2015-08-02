package model;

import org.newdawn.slick.geom.Circle;

public class Bible extends ContactWeapon{

	public Bible(Character owner){
		this.damage = -10f;
		this.frequency = 10f;
		this.lifePoints = 1f;
		this.setOwner(owner);
		this.collisionBox = new Circle(owner.getX(),owner.getY(),owner.collisionBox.getBoundingCircleRadius());
		this.setXY(owner.getX(),owner.getY());
		this.setXY(owner.getX(),owner.getY());
	}
}