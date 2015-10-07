package model;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;

public class Checkpoint extends ActionObjet {
	float printed;
	public Checkpoint(Plateau p , float x, float y){
		this.lifePoints=1f;
		this.p = p;
		//p.addEquipmentObjets(this);
		this.x = x;
		this.y = y;
		this.collisionBox = new Point(x,y);
		this.setXY(x, y);
		try {
			this.image = new Image("pics/cross.png");
			this.image = this.image.getScaledCopy(1f/8f);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.printed=0f;
	}
	
	public Checkpoint(float x, float y){
		this.x= x;
		this.y=y;
		this.lifePoints = 1f;
		this.collisionBox = new Point(x,y);
	}
	
	public void action(){
		if(printed<=2f){
			printed+=0.1f;
		}
		
	}
	
	public Graphics draw(Graphics g){
		
		if(printed<=1f){
			g.drawImage(this.image,this.x-8f,this.y-8f);
		}
		return g;
	}
}
