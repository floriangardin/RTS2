package weapon;


import org.newdawn.slick.geom.Circle;

import bullets.Arrow;
import model.Plateau;
import units.Character;

public class Bow extends RangeWeapon{
	
	public Bow(Plateau p,Character owner){
		// Parameters

		this.range = owner.range;
		this.damage = owner.damage;
		this.chargeTime = owner.chargeTime;
		
		//
		
		this.p = p;
		this.lifePoints = 1f;
		p.addEquipmentObjets(this);
		this.name = "bow";
		this.state = this.chargeTime;
		this.collisionBox = new Circle(owner.getX(),owner.getY(),this.range);
		this.setOwner(owner);
		this.team = owner.team;

	}
	public void action(){
		if(this.state<this.chargeTime+2f){
			this.state += 0.1f;
		}
		// Test if owner 
		if(this.owner==null){
			return;
		}
		// update x and y
		this.setXY(this.owner.getX(), this.owner.getY());
		if(this.team!=this.p.g.currentPlayer)
			return;

		if(!(this.owner.getTarget() instanceof Character)){
			return;
		}
		Character target =(Character) this.owner.getTarget();
		if(this.owner.team==target.team){
			return;
		}
		if(target.lifePoints<=0f){
			this.owner.setTarget(null);
			this.state=0f;
			return;
		}
		// Launch bullet
		if(this.state>this.chargeTime){
			// Launch a bullet
			Circle circle = new Circle(this.getX(),this.getY(),this.range);
			if(target.collisionBox.intersects(circle)){
				new Arrow(this.p,this.owner,this.damage);
				this.state = 0f;
			}
		}
	}
}
