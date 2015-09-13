package weapon;

import buildings.Building;
import units.Character;

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
		System.out.println("vaneau 1 "+this.owner.id+" "+this.owner.team+" "+!(this.owner.getTarget() instanceof Character) + "  "+((Character)this.owner.getTarget()==null));
		if(!(this.owner.getTarget() instanceof Character) || (Character)this.owner.getTarget()==null)
			return;
		System.out.println("vaneau 2");
		if(c.team!=this.owner.team && ((Character)this.owner.getTarget()).id==c.id && this.state>this.chargeTime && !this.owner.isMobile()){
			//Attack !
			// Attack sound
			float damage = this.damage;
			this.sound.play(1f,this.p.soundVolume);
			if(this instanceof Spear && c.horse!=null)
				damage = damage*this.p.constants.bonusSpearHorse;
			if(this instanceof Sword && c.weapon instanceof Bow)
				damage = damage*this.p.constants.bonusSwordBow;
			if(c.getArmor()<damage){
				c.lifePoints+=c.getArmor()-damage;
			}
			// Reset the state
			this.state = 0f;
		}
	}
	public void collision(Building c){
		

		
	}
}
