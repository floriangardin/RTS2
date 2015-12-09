package model;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Point;

import main.Main;
import units.Character;

public class Checkpoint extends ActionObjet {
	float printed;
	int mode;
	float maxDuration=10f;
	float state;
	float maxRadius = 20f;
	int lastRoundUpdate =0;
	Circle drawShape;
	
	
	public Checkpoint(Plateau p , float x, float y){
		this.lifePoints=1f;
		this.p = p;
		//p.addEquipmentObjets(this);
		this.x = x;
		this.y = y;
		
		this.collisionBox = new Circle(x,y,3f);
		this.drawShape = new Circle(x,y,maxRadius);
		drawShape.setCenterX(x);
		drawShape.setCenterY(y);
		
		this.selectionBox = this.collisionBox;
		this.setXY(x, y);
		this.printed=0f;
		
	}
	

	
	public void action(){

		
		
	}
	
	public Graphics draw(Graphics g){
		if(this.lastRoundUpdate==this.p.g.round){
			return g;
		}
		if(state<=maxDuration){
			state+=3f*Main.increment;
		}
		
		if(state<=maxDuration){
			g.setLineWidth(1f);
			drawShape.setRadius(maxRadius*(1-state/maxDuration));
			drawShape.setCenterX(x);
			drawShape.setCenterY(y);
			g.fill(new Circle(x,y,2f));
			g.draw(drawShape);
			this.lastRoundUpdate = this.p.g.round;
		}
		return g;
	}

	@Override
	public void collision(Character c) {
		// TODO Auto-generated method stub
		
	}
}
