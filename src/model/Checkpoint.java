package model;

import main.Main;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

import units.Character;

public class Checkpoint extends Objet {
	float printed;
	int mode;
	public Color color;
	public float maxDuration=10f;
	public float state;
	float maxRadius = 20f;
	int lastRoundUpdate =0;
	Circle drawShape;
	Circle drawShape2;
	public boolean toDraw=false;
	public boolean alwaysDraw = false;
	
	public Checkpoint(Plateau p , float x, float y){
		this.lifePoints=1f;
		this.p = p;
		//p.addEquipmentObjets(this);
		this.x = x;
		this.y = y;
		color = Colors.team2;
		this.collisionBox = new Circle(x,y,3f);
		this.drawShape = new Circle(x,y,maxRadius);
		drawShape.setCenterX(x);
		drawShape.setCenterY(y);
		if(!(this instanceof MarkerBuilding))
			this.p.checkpoints.addElement(this);
		this.drawShape2 = new Circle(x,y,0);
		drawShape2.setCenterX(x);
		drawShape2.setCenterY(y);
		
		this.selectionBox = null;
		this.setXY(x, y);
		this.printed=0f;
		
	}
	
	public Checkpoint(Plateau p , float x, float y,boolean toDraw,Color color){
		this.lifePoints=1f;
		this.p = p;
		//p.addEquipmentObjets(this);
		this.x = x;
		this.y = y;
		this.alwaysDraw = toDraw;
		this.color = color;
		this.collisionBox = new Circle(x,y,3f);
		this.drawShape = new Circle(x,y,maxRadius);
		drawShape.setCenterX(x);
		drawShape.setCenterY(y);
		this.p.checkpoints.addElement(this);
		this.drawShape2 = new Circle(x,y,1f);
		drawShape2.setCenterX(x);
		drawShape2.setCenterY(y);
		
		this.selectionBox = null;
		this.setXY(x, y);
		this.printed=0f;
		
	}
	

	
	public void action(){

		toDraw = false;

		if(state<=maxDuration){
			state+=3f*Main.increment;
		}else{
			this.lifePoints=-1f;
		}
		
	}
	
	public Graphics draw(Graphics g){
		if(!toDraw && !alwaysDraw){
			return g;
		}
		if(this.lastRoundUpdate==Game.g.round){
			return g;
		}

		g.setAntiAlias(true);
		g.setColor(Colors.team0);
		if(state<=maxDuration){
			if(color!=null){
				
				g.setLineWidth(2f*Main.ratioSpace);
				
			}
			g.setLineWidth(2f);
			drawShape.setRadius(maxRadius*(1-2*state/maxDuration));
			drawShape.setCenterX(x);
			drawShape.setCenterY(y);
			
			
			drawShape2.setRadius((maxRadius)*(state/maxDuration));
			drawShape2.setCenterX(x);
			drawShape2.setCenterY(y);
			g.fill(new Circle(x,y,2f));
			
			g.draw(drawShape2);
			g.setColor(color);
			g.draw(drawShape);
			this.lastRoundUpdate = Game.g.round;
		}
		g.setAntiAlias(false);
		return g;
	}

	@Override
	public void collision(Character c) {
		// TODO Auto-generated method stub
		
	}
}
