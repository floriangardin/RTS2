package model;

import org.newdawn.slick.geom.Circle;

public class RangeWeapon extends Weapon{
	protected float range;
	protected Circle areaEffect;
	
	
	
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
		// Launch bullet
		if(this.state>this.chargeTime){
			// Launch a bullet 
			new Arrow(this.p,this.owner);
			this.state = 0f;
		}
	}
}
