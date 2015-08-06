package model;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Circle;

public class Sword extends ContactWeapon {
	
	
	public Sword(Plateau p ,Character owner){
		// Parameters
		this.weight = 0.2f;
		this.damage = 5f;
		this.chargeTime = 5f;
		float extraSize = 10f;
		
		//
		
		this.p = p;
		p.addEquipmentObjets(this);
		this.state = 0f;
		this.lifePoints = 1f;
		this.collisionBox = new Circle(owner.getX(),owner.getY(),owner.collisionBox.getBoundingCircleRadius()+extraSize);
		this.setOwner(owner);
		this.name = "Sword";
		this.sound = p.sounds.sword;
		
	}
	

	
}
