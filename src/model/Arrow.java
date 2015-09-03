package model;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Point;

import multiplaying.OutputModel.*;

public class Arrow extends Bullet{

	protected float angle= 0f;

	public Arrow(Plateau p,Character owner,float damage){
		// Parameters
		this.damage = 3f;
		float size = 2f;
		float Vmax = 200f;
		// 
		this.p = p;
		this.id = p.g.idBullet;
		p.g.idBullet++;
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
		this.sound.play(1f,this.p.soundVolume);
	}
	public Arrow(OutputBullet ocb ,Plateau p){
		// Only used to display on client screen
		// Parameters
		this.p = p;
		this.id = ocb.id;
		p.addBulletObjets(this);
		this.lifePoints = 1f;
		this.collisionBox = new Point(x,y);
		this.setXY(ocb.x,ocb.y);
		this.vx = ocb.vx;
		this.vy = ocb.vy;
		this.angle = (float) (Math.atan(vy/(vx+0.00001f))*180/Math.PI);
		if(this.vx<0)
			this.angle+=180;
		if(this.angle<0)
			this.angle+=360;
		this.image = p.images.arrow.getScaledCopy(1f);
		this.image.rotate(this.angle);
		this.sound = p.sounds.arrow;
		this.sound.play(1f,this.p.soundVolume);
	}

	public void collision(Character c){
		if(c.team!=this.owner.team){
			// Attack if armor<damage and collision
			float damage = this.damage;
			if(c.horse==null){
				damage = damage * this.p.constants.bonusBowFoot;
			}
			if(c.getArmor()<=damage){
				c.lifePoints+=c.getArmor()-damage;
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
