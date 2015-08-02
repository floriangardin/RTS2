package model;

public class ContactWeapon extends Weapon {
	protected float damage;
	
	
	
	
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
