package model;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

public class Bolt extends Bullet {

	
	public Bolt(Plateau p,Character owner){
		this.p = p;
		p.addBulletObjets(this);
		this.p = p;
		this.damage = 10f;
		this.lifePoints = 1f;
		this.owner = owner;
		this.target = new Checkpoint(owner.target.getX(),owner.target.getY());
		this.areaEffect = 100f;
		this.collisionBox = new Circle(owner.getX(),owner.getY(),10f);
		this.setXY(owner.getX(),owner.getY());
		this.vx = this.owner.target.getX()-this.owner.getX();
		this.vy = this.owner.target.getY()-this.owner.getY();
		//Normalize speed : 
		float norm = this.vx*this.vx+this.vy*this.vy;
		norm  = (float)Math.sqrt(norm)*this.p.constants.FRAMERATE;
		float Vmax = 50f;
		this.vx = Vmax*this.vx/norm;
		this.vy = Vmax*this.vy/norm;
	}
	public void action(){
		this.setXY(this.getX()+this.vx, this.getY()+this.vy);
		
		if(this.collisionBox.contains(this.target.collisionBox)){
			System.out.println("explosion");
			this.explode();
		}
	}
	public void explode(){
		Circle area = new Circle(this.getX(),this.getY(),this.areaEffect);
		for(Character c : this.p.characters){
			if(c.collisionBox.intersects(area)){
				this.boom(c);
				
			}
		}
		this.lifePoints=-1f;
	}
	public void boom(Character c){
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

	public Graphics draw(Graphics g){
		g.setColor(Color.black);
		g.fill(new Circle(this.collisionBox.getCenterX(),this.collisionBox.getCenterY(),this.collisionBox.getBoundingCircleRadius()+10f));
		return g;
	}
}
