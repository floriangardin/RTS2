package model;

import org.newdawn.slick.geom.Circle;

public class Bow extends RangeWeapon{
	
	public Bow(Plateau p,Character owner){
		this.p = p;
		p.addEquipmentObjets(this);
		this.state = 0f;
		this.range = 100;
		this.setOwner(owner);
		this.chargeTime = 10f;
		this.collisionBox = new Circle(owner.getX(),owner.getY(),owner.collisionBox.getBoundingCircleRadius());
		this.setXY(owner.getX(),owner.getY());

	}
}
