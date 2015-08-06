package model;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;

public class MenuArrow extends Arrow {
	
	Menu menu;

	public MenuArrow(float x1, float x2, float y1, float y2, Menu m) {
		this.menu = m;
		this.lifePoints = 1f;
		this.collisionBox = new Circle(x1,x2,2f);
		this.setXY(x1,y1);
		this.vx = x2-x1;
		this.vy = y2-y1;
		float norm = this.vx*this.vx+this.vy*this.vy;
		norm  = (float)Math.sqrt(norm)*60f;
		float Vmax = 200f;
		this.vx = Vmax*this.vx/norm;
		this.vy = Vmax*this.vy/norm;
		this.angle = (float) (Math.atan(vy/(vx+0.00001f))*180/Math.PI);
		if(this.vx<0)
			this.angle+=180;
		if(this.angle<0)
			this.angle+=360;
		try {
			this.image = new Image("pics/Arrow.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		this.image.rotate(this.angle);
		//this.sound = menu.sounds.arrow;
		//this.sound.play();
	}
	
	public void action(){
		this.setXY(this.getX()+this.vx, this.getY()+this.vy);
		if(this.x>this.menu.game.resX || this.x<0 || this.y>this.menu.game.resY||this.y<0){
			this.lifePoints=-1f;
		}
	}
	
}
