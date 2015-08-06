package model;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Circle;

public class Arrow extends Bullet{

	protected float angle= 0f;
	
	public Arrow(Plateau p,Character owner,float damage){
		// Parameters
		this.damage = 3f;
		float size = 2f;
		float Vmax = 200f;
		
		// 
		this.p = p;
		this.damage = damage;
		p.addBulletObjets(this);
		this.lifePoints = 1f;
		this.owner = owner;
		this.collisionBox = new Circle(owner.getX(),owner.getY(),size);
		this.setXY(owner.getX(),owner.getY());
		this.vx = this.owner.getTarget().getX()-this.owner.getX();
		this.vy = this.owner.getTarget().getY()-this.owner.getY();
		//Normalize speed : 
		float norm = this.vx*this.vx+this.vy*this.vy;
		norm  = (float)Math.sqrt(norm)*this.p.constants.FRAMERATE;
		this.vx = Vmax*this.vx/norm;
		this.vy = Vmax*this.vy/norm;
		this.angle = (float) (Math.atan(vy/(vx+0.00001f))*180/Math.PI);
		if(this.vx<0)
			this.angle+=180;
		if(this.angle<0)
			this.angle+=360;
		this.image = p.images.arrow.getScaledCopy(1f);
		this.image.rotate(this.angle);
		this.sound = p.sounds.arrow;
		this.sound.play();
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
		g.drawImage(this.image,this.getX()-5f,this.getY()-5f);
		//g.setColor(Color.white);
		//g.fill(this.collisionBox);
		return g;
	}
	public void action(){
		this.setXY(this.getX()+this.vx, this.getY()+this.vy);
		if(this.x>this.p.maxX || this.x<0 || this.y>this.p.maxY||this.y<0){
			this.lifePoints=-1f;
		}
	}
	
	
	// For MenuArrow
	public Arrow(){
		
	}
}
