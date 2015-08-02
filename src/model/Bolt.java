package model;

import org.newdawn.slick.geom.Circle;

public class Bolt extends Bullet {

	
	public Bolt(Plateau p,Character owner){
		this.p = p;
		p.addBulletObjets(this);
		this.p = p;
		this.damage = 10f;
		this.lifePoints = 1f;
		this.owner = owner;
		this.collisionBox = new Circle(owner.getX(),owner.getY(),owner.collisionBox.getBoundingCircleRadius());
		this.setXY(owner.getX(),owner.getY());
		this.vx = this.owner.target.getX()-this.owner.getX();
		this.vy = this.owner.target.getY()-this.owner.getY();
		//Normalize speed : 
		float norm = this.vx*this.vx+this.vy*this.vy;
		norm  = (float)Math.sqrt(norm)*this.p.constants.FRAMERATE;
		float Vmax = 5f;
		this.vx = Vmax*this.vx/norm;
		this.vy = Vmax*this.vy/norm;
	}
	
	public void collision(Character c){
		if(c.team!=this.owner.team){
			// Attack if armor<damage and collision
			if(c.getArmor().damageReductor<=this.damage){
				target.lifePoints+=c.getArmor().damageReductor-this.damage;
			}
		this.lifePoints=-1f;
		}
	}
}
