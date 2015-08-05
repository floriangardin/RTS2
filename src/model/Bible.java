package model;

import org.newdawn.slick.geom.Circle;

public class Bible extends ContactWeapon{

	public Bible(Plateau p, Character owner){
		this.p = p;
		p.addEquipmentObjets(this);
		this.name = "Bible";
		this.state = 0f;
		this.damage = -0.1f;
		this.chargeTime = 0.6f;
		this.lifePoints = 1f;
		this.collisionBox = new Circle(owner.getX(),owner.getY(),owner.collisionBox.getBoundingCircleRadius()+10f);
		this.setOwner(owner);
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
		this.state += 0.1f;
		Character target =(Character) this.owner.target;
		if(target.team==this.owner.team && this.state>this.chargeTime){
			// Attack if armor<damage and collision
			if(target!=this.owner && target.lifePoints<target.maxLifePoints && target.collisionBox.intersects(this.collisionBox)){
				target.lifePoints=(float)Math.min(target.maxLifePoints, target.lifePoints-this.damage);
			}
			// Reset state of weapon 
			this.state = 0f;
			
		}
	}
}