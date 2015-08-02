package model;

import org.newdawn.slick.geom.Circle;

public class Bible extends ContactWeapon{

	public Bible(Character owner){
		this.state = 0f;
		this.damage = -10f;
		this.chargeTime = 10f;
		this.lifePoints = 1f;
		this.setOwner(owner);
		this.collisionBox = new Circle(owner.getX(),owner.getY(),owner.collisionBox.getBoundingCircleRadius());
		this.setXY(owner.getX(),owner.getY());
		this.setXY(owner.getX(),owner.getY());
	}
	
	
	public void action(){
		// Test if owner 
		if(this.owner==null){
			return;
		}
		// update x and y
		this.setXY(this.owner.getX(), this.owner.getY());
		// Test if target
		if(!(this.owner.target instanceof Character)){
			return;
		}
		Character target =(Character) this.owner.target;
		this.state += 0.1f;
		if(this.state>this.chargeTime){
			// Attack if armor<damage and collision
			if(target.getArmor().damageReductor<=this.damage && target.collisionBox.intersects(this.collisionBox)){
				target.lifePoints+=target.getArmor().damageReductor-this.damage;
			}
			// Reset state of weapon 
			this.state = 0f;
			
		}
	}
}