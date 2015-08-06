package model;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;

public class MenuFireball extends Fireball {
	
	Menu m;
	
	public MenuFireball(float x1, float y1, float x2, float y2, Menu m){
		super();
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
		this.target = new Checkpoint(x2,y2);
		this.areaEffect = 40f;
		this.collisionBox = new Circle(x1,y1,50f);
		this.setXY(x1,y1);
		this.vx = x2-x1;
		this.vy = y2-y1+altitude;
		//Normalize speed : 
		float norm = this.vx*this.vx+this.vy*this.vy;
		norm  = (float)Math.sqrt(norm)*60f;
		float Vmax = 120f;
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
		//this.sound = p.sounds.fireball;
		//this.sound.play();
	}
	
	
	public void explode(){
		this.explosion = true;
	}
}
