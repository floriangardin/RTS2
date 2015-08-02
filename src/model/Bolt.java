package model;

import org.newdawn.slick.geom.Circle;

public class Bolt extends Bullet {

	
	public Bolt(Character owner){
		this.damage = 10f;
		this.lifePoints = 1f;
		this.owner = owner;
		this.collisionBox = new Circle(owner.getX(),owner.getY(),owner.collisionBox.getBoundingCircleRadius());
		this.setXY(owner.getX(),owner.getY());
	}
	
	
}
