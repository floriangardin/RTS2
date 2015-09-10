package weapon;


import org.newdawn.slick.geom.Circle;

import model.Plateau;
import units.Character;

public class Sword extends ContactWeapon {
	
	
	public Sword(Plateau p ,Character owner){
		// Parameters
		this.damage = owner.damage;
		this.chargeTime = owner.chargeTime;
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
