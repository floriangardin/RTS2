package model;

import org.newdawn.slick.geom.Circle;

public class Sword extends ContactWeapon {
	
	
	public Sword(Plateau p ,Character owner){
		this.p = p;
		this.weight = 0.2f;
		p.addEquipmentObjets(this);
		this.state = 0f;
		this.damage = 10f;
		this.chargeTime = 10f;
		this.lifePoints = 1f;
		this.collisionBox = new Circle(owner.getX(),owner.getY(),owner.collisionBox.getBoundingCircleRadius()+10f);
		this.setOwner(owner);
		this.name = "Sword";
		
		
	}
	

	
}
