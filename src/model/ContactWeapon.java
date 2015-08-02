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
		if(this.state<this.chargeTime+1f)
			this.state += 0.1f;
	}

	public void collision(Character c){
		
		
		if(c.team!=this.owner.team && this.state>this.chargeTime && !this.owner.isMobile()){
			//Attack !
			if(c.getArmor()!=null && c.getArmor().damageReductor<this.damage){
				c.lifePoints+=c.getArmor().damageReductor-this.damage;
				if(c.target==null){
					c.target=this.owner;
				}
			}
			else{
				c.lifePoints-=this.damage;
				if(c.target==null){
					c.target=this.owner;
				}
			}
			// Reset the state
			this.state = 0f;
		}
	}
}
