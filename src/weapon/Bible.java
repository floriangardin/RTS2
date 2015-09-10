package weapon;

import org.newdawn.slick.geom.Circle;

import model.Plateau;
import units.Character;

public class Bible extends ContactWeapon{

	public Bible(Plateau p, Character owner){
		// Parameters
		this.state = 0f;
		this.damage = owner.damage;
		this.chargeTime = owner.chargeTime;
		this.name = "bible";
		float extraRange = 30f;
		this.p = p;
		this.lifePoints = 1f;
		p.addEquipmentObjets(this);
		this.collisionBox = new Circle(owner.getX(),owner.getY(),owner.collisionBox.getBoundingCircleRadius()+extraRange);
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