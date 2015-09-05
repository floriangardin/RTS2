package model;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;

public class Barrack extends ProductionBuilding{

	
	public Barrack(Plateau plateau, Game g, float f, float h) {
		teamCapturing= 0;
		team = 0;
		isCapturing=false;
		maxLifePoints = 200f;
		this.p = plateau ;
		p.addBuilding(this);
		this.lifePoints = 200f;
		this.g = g;
		this.x = f;
		this.y = h;
		this.sizeX = 120f; 
		this.sizeY = 120f;
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY,sizeX,sizeY);
		this.image = this.p.images.tent;
		// List of potential production (Spearman
		this.productionList = new Vector<UnitsList>();
		this.productionList.addElement(UnitsList.Spearman);
		this.productionList.addElement(UnitsList.Bowman);
		
	}
	
	public void product(int unit){

		if(unit<this.productionList.size()){
			// Create a copy instead
			this.queue.add(this.productionList.get(unit));
		}
		Character.createCharacter(p, team, x, y, this.queue.get(0));
	}
	
	public void action(){
		//Do the action of Barrack
		//Product, increase state of the queue
		// if reach production reset and create first unit in the queue
		if(this.lifePoints<10){
			this.team = this.teamCapturing;
		}
	}
	public Graphics draw(Graphics g){
		float r = collisionBox.getBoundingCircleRadius();
		g.drawImage(this.image,this.getX()-sizeX/2f,this.getY()-sizeY,this.getX()+sizeX/2f,this.getY()+1f*sizeY/6f,0f,0f,this.image.getWidth(),this.image.getHeight());
		if(this.lifePoints<this.maxLifePoints){
			g.setColor(Color.red);
			g.draw(new Line(this.getX()-r,this.getY()-r-30f,this.getX()+r,this.getY()-r-30f));
			float x = this.lifePoints*2f*r/this.maxLifePoints;
			g.setColor(Color.green);
			g.draw(new Line(this.getX()-r,this.getY()-r-30f,this.getX()-r+x,this.getY()-r-30f));
		}
		return g;
	}
	
}
