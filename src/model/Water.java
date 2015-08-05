package model;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

public class Water extends NaturalObjet {
	
	public Water(float x, float y, Plateau p) {
		float size = 32.0f;
		try {
			this.image= new Image("pics/water.jpg");
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.collisionBox = new Rectangle(x-size/2,y-size/2,size,size);
		this.color = Color.blue;
		this.p = p;
		p.addNaturalObjets(this);
		this.lifePoints = 1.0f;
		this.setXY(x, y);
	}
	
	public void collision(Objet o){
		//TODO: collision
	}
	
	public Graphics draw(Graphics g){
		g.drawImage(this.image,this.collisionBox.getX(),this.collisionBox.getY());
		return g;
	}
}
