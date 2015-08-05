package model;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;

public class Bolt extends Bullet {

	protected float altitude;
	protected int animation;
	protected float angle;
	protected Image image1, image2, boom;
	protected boolean explosion= false;

	public Bolt(Plateau p,Character owner,float damage){
		this.p = p;
		p.addBulletObjets(this);
		this.p = p;
		this.damage = damage;
		try {
			this.image = (new Image("pics/Fireball.png")).getSubImage(0, 150, 75, 75);
			this.image1 = (new Image("pics/Fireball.png")).getSubImage(75, 150, 75, 75);
			this.image2 = (new Image("pics/Fireball.png")).getSubImage(150, 150, 75, 75);
			this.boom = new Image("pics/Explosion.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		this.altitude = 0f;
		this.animation = 0;
		this.lifePoints = 30f;
		this.owner = owner;
		this.target = new Checkpoint(owner.target.getX(),owner.target.getY());
		this.areaEffect = 40f;
		this.collisionBox = new Circle(owner.getX(),owner.getY(),10f);
		this.setXY(owner.getX(),owner.getY()-altitude);
		this.vx = this.owner.target.getX()-this.owner.getX();
		this.vy = this.owner.target.getY()-this.owner.getY()+altitude;
		//Normalize speed : 
		float norm = this.vx*this.vx+this.vy*this.vy;
		norm  = (float)Math.sqrt(norm)*this.p.constants.FRAMERATE;
		float Vmax = 170f;
		this.vx = Vmax*this.vx/norm;
		this.vy = Vmax*this.vy/norm;
		this.angle = (float) (Math.atan(vy/(vx+0.00001f))*180/Math.PI);
		if(this.vx<0)
			this.angle+=180;
		if(this.angle<0)
			this.angle+=360;
		this.image.rotate(this.angle);
		this.image1.rotate(this.angle);
		this.image2.rotate(this.angle);
	}
	public void action(){
		if(explosion){
			this.lifePoints-=1f;
			return;
		}
		this.setXY(this.getX()+this.vx, this.getY()+this.vy);
		this.animation+=1;
		if(this.animation>=9)
			this.animation = 0;
		if(this.collisionBox.contains(this.target.collisionBox)){
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
		this.explosion = true;
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
	}

	public Graphics draw(Graphics g){
		if(this.explosion){
			float r = this.boom.getWidth()/5f;
			if(lifePoints>=24f)
				g.drawImage(this.boom, this.getX()-40f, this.getY()-40f, this.getX()+40f, this.getY()+40f,0f,0f,r,r);
			else if(lifePoints>=18f)
				g.drawImage(this.boom, this.getX()-40f, this.getY()-40f, this.getX()+40f, this.getY()+40f,r,0f,2*r,r);
			else if(lifePoints>=12f)
				g.drawImage(this.boom, this.getX()-40f, this.getY()-40f, this.getX()+40f, this.getY()+40f,2*r,0f,3*r,r);
			else if(lifePoints>=6f)
				g.drawImage(this.boom, this.getX()-40f, this.getY()-40f, this.getX()+40f, this.getY()+40f,3*r,0f,4*r,r);
			else 
				g.drawImage(this.boom, this.getX()-40f, this.getY()-40f, this.getX()+40f, this.getY()+40f,4*r,0f,5*r,r);
			
		} else {
			if(animation<3)	
				g.drawImage(this.image, this.getX()-28f, this.getY()-28f, this.getX()+28f, this.getY()+28f,0f,0f,this.image.getWidth(),this.image.getHeight());
			else if(animation<6)	
				g.drawImage(this.image1, this.getX()-28f, this.getY()-28f, this.getX()+28f, this.getY()+28f,0f,0f,this.image.getWidth(),this.image.getHeight());
			else	
				g.drawImage(this.image2, this.getX()-28f, this.getY()-28f, this.getX()+28f, this.getY()+28f,0f,0f,this.image.getWidth(),this.image.getHeight());
		}
		//g.setColor(Color.black);
		//g.fill(new Circle(this.collisionBox.getCenterX(),this.collisionBox.getCenterY(),this.collisionBox.getBoundingCircleRadius()));
		return g;
	}
}
