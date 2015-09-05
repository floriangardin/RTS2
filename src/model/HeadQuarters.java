package model;

import java.util.Vector;

import multiplaying.OutputModel.OutputBuilding;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;

public class HeadQuarters extends Building {

	int charge ;

	Technologie production;
	boolean isProducing;
	Vector<Technologie> techsUndiscovered;
	Vector<Technologie> techsDiscovered;
	
	public HeadQuarters(Plateau plateau, Game g, float f, float h) {
		teamCapturing= 0;
		team = 0;
		constructionPhase = false;
		destructionPhase = true;
		isCapturing = false;
		this.sight = 300f;
		this.p = plateau ;
		maxLifePoints = p.constants.barrackLifePoints;
		this.name = "HeadQuarters";
		p.addBuilding(this);
		this.selection_circle = this.p.images.selection_rectangle.getScaledCopy(4f);
		type= 5;
		this.lifePoints = this.maxLifePoints;
		this.g = g;
		this.id = p.g.idChar;
		p.g.idChar+=1;
		this.x = f;
		this.y = h;
		this.sizeX = 120f; 
		this.sizeY = 120f;
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY,sizeX,sizeY);
		this.image = this.p.images.tent;
		// List of potential production (Spearman
		this.techsDiscovered = new Vector<Technologie>();
		this.techsUndiscovered = new Vector<Technologie>();

		
		

	}

	public HeadQuarters(OutputBuilding ocb, Plateau p){
		team = ocb.team;
		type= 5;
		maxLifePoints = ocb.maxlifepoints;
		this.p = p;
		p.addBuilding(this);
		this.lifePoints = this.maxLifePoints;
		this.g = p.g;
		this.x = ocb.x;
		this.y = ocb.y;
		this.selection_circle = this.p.images.selection_rectangle.getScaledCopy(4f);
		this.id = ocb.id;
		this.sizeX = 120f; 
		this.sizeY = 120f;
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY,sizeX,sizeY);
		this.image = this.p.images.tent;


		
		
	}



	public void action(){

	}
	public Graphics draw(Graphics g){
		float r = collisionBox.getBoundingCircleRadius();
		g.drawImage(this.image,this.getX()-sizeX/2f,this.getY()-sizeY,this.getX()+sizeX/2f,this.getY()+1f*sizeY/6f,0f,0f,this.image.getWidth(),this.image.getHeight());
		if(this.lifePoints<this.maxLifePoints){
			// Lifepoints
			g.setColor(Color.red);
			g.draw(new Line(this.getX()-r,this.getY()-r-30f,this.getX()+r,this.getY()-r-30f));
			float x = this.lifePoints*2f*r/this.maxLifePoints;
			g.setColor(Color.green);
			g.draw(new Line(this.getX()-r,this.getY()-r-30f,this.getX()-r+x,this.getY()-r-30f));

		}
		// Construction points
		if(this.constructionPoints<this.maxLifePoints && constructionPhase){
			g.setColor(Color.white);
			g.draw(new Line(this.getX()-r,this.getY()-r-50f,this.getX()+r,this.getY()-r-50f));
			float x = this.constructionPoints*2f*r/this.maxLifePoints;
			g.setColor(Color.blue);
			g.draw(new Line(this.getX()-r,this.getY()-r-50f,this.getX()-r+x,this.getY()-r-50f));
		}
		return g;
	}
}
