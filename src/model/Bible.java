package model;

import org.newdawn.slick.geom.Circle;

public class Bible extends ContactWeapon{

	public Bible(Plateau p, Character owner){
		this.p = p;
		p.addEquipmentObjets(this);
		this.name = "Bible";
		this.state = 0f;
		this.damage = -0.2f;
		this.chargeTime = 0.2f;
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
		if(!(this.owner.getTarget() instanceof Character)){
			return;
		}
		this.setTarget(this.owner.getTarget());
		this.state += 0.1f;
	}

	public void collision(Character c){
		if(c!=this.owner && c.team==this.owner.team && c==this.getTarget() && this.state>this.chargeTime && !this.owner.isMobile()){
			//Heal !
			if(c.lifePoints>=c.maxLifePoints){
				if(this.owner.getTarget()==c){
					this.owner.setTarget(null);
				}
				return;
			}
			c.lifePoints= (float)Math.min(c.maxLifePoints, c.lifePoints-this.damage);

			// Reset the state
			this.state = 0f;
		}
	}
}