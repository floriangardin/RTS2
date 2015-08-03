package model;

import org.newdawn.slick.geom.Circle;

public class Bow extends RangeWeapon{
	
	public Bow(Plateau p,Character owner){
		this.p = p;
		this.weight = 0.1f;
		this.lifePoints = 1f;
		p.addEquipmentObjets(this);
		this.chargeTime = 10f;
		this.name = "Bow";
		this.state = 0f;
		this.range = 100f;
		this.collisionBox = new Circle(owner.getX(),owner.getY(),this.range);
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
		Character target =(Character) this.owner.target;
		if(this.owner.team==target.team){
			return;
		}

		if(this.state<this.chargeTime+2f){
			this.state += 0.1f;
		}
		if(target.lifePoints<=0f){
			this.owner.target=null;
			this.state=0f;
			return;
		}
		// Launch bullet
		if(this.state>this.chargeTime){
			// Launch a bullet
			Circle circle = new Circle(this.getX(),this.getY(),this.range);

			if(target.collisionBox.intersects(circle)){
				new Arrow(this.p,this.owner);
				this.state = 0f;
			}
		}
	}
}
