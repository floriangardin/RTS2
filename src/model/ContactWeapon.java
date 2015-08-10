package model;

public class ContactWeapon extends Weapon {

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
		
		
		if(c.team!=this.owner.team && this.owner.target!=null && this.owner.target==c && this.state>this.chargeTime){
			//Attack !
			// Attack sound
			this.sound.play(1f,this.p.soundVolume);
			if(c.getArmor()!=null && c.getArmor().damageReductor<this.damage){
				c.lifePoints+=c.getArmor().damageReductor-this.damage;

			}
			else{
				c.lifePoints-=this.damage;
			}
			// Reset the state
			this.state = 0f;
		}
	}
}
