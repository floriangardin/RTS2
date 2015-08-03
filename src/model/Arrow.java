package model;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

public class Arrow extends Bullet{

	public Arrow(Plateau p,Character owner){
		this.p = p;
		p.addBulletObjets(this);
		this.damage = 3f;
		this.lifePoints = 1f;
		this.owner = owner;
		this.collisionBox = new Circle(owner.getX(),owner.getY(),2f);
		this.setXY(owner.getX(),owner.getY());
		this.vx = this.owner.target.getX()-this.owner.getX();
		this.vy = this.owner.target.getY()-this.owner.getY();
		//Normalize speed : 
		float norm = this.vx*this.vx+this.vy*this.vy;
		norm  = (float)Math.sqrt(norm)*this.p.constants.FRAMERATE;
		float Vmax = 100f;
		this.vx = Vmax*this.vx/norm;
		this.vy = Vmax*this.vy/norm;

	}
	public void collision(Character c){
		if(c.team!=this.owner.team){
			// Attack if armor<damage and collision
			if(c.getArmor()!=null){
				if(c.getArmor().damageReductor<=this.damage){
					c.lifePoints+=c.getArmor().damageReductor-this.damage;
				}
			}
			else{
				c.lifePoints-=this.damage;
			}
			this.lifePoints=-1f;
		}

	}
	public Graphics draw(Graphics g){
		g.setColor(Color.white);
		g.fill(this.collisionBox);
		return g;
	}
	public void action(){
		this.setXY(this.getX()+this.vx, this.getY()+this.vy);
		if(this.x>this.p.maxX || this.x<0 || this.y>this.p.maxY||this.y<0){
			this.lifePoints=-1f;
		}
	}
}
