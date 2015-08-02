package model;

import org.newdawn.slick.geom.Circle;

public class Balista extends RangeWeapon{

	public Balista(Plateau p ,Character owner){
		this.p = p;
		p.addEquipmentObjets(this);
		this.range = 100;
		this.chargeTime = 10f;
		this.setOwner(owner);
		this.collisionBox = new Circle(owner.getX(),owner.getY(),owner.collisionBox.getBoundingCircleRadius());
		this.setXY(owner.getX(),owner.getY());
	}
	
}
