package model;

import java.util.Vector;

import multiplaying.OutputModel.OutputBuilding;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;

public class HeadQuarters extends Building {

	int charge ;

	int queue;
	boolean isProducing;
	Vector<Technologie> techsDiscovered;
	Vector<Technologie> techsUndiscovered;
	public Vector<Technologie> productionList;
	
	public HeadQuarters(Plateau plateau, Game g, float f, float h,int team) {
		this.queue = -1;
		teamCapturing= team;
		this.p = plateau ;
		this.team = team;
		this.sizeX = this.p.constants.headQuartersSizeX; 
		this.sizeY = this.p.constants.headQuartersSizeY;
		this.sight = this.p.constants.headQuartersSight;
		
		maxLifePoints = p.constants.headQuartersLifePoints;
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
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY/2f,sizeX,sizeY);
		if(this.team == 1){
			this.image = this.p.images.buildingHeadQuartersBlue;
		}
		else if(this.team == 2){
			this.image = this.p.images.buildingHeadQuartersRed;
		}
		else {
			this.image = this.p.images.tent;
		}
		// List of potential production (Spearman
		this.techsDiscovered = new Vector<Technologie>();
		this.techsUndiscovered = new Vector<Technologie>();
		this.productionList = new Vector<Technologie>();

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
		this.sizeX = this.p.constants.headQuartersSizeX; 
		this.sizeY = this.p.constants.headQuartersSizeY;
		this.sight = this.p.constants.headQuartersSight;
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY/2f,sizeX,sizeY);
		if(this.team == 1){
			this.image = this.p.images.buildingHeadQuartersBlue;
		}
		else if(this.team == 2){
			this.image = this.p.images.buildingHeadQuartersRed;
		}
		else {
			this.image = this.p.images.tent;
		}

		
	}
	
	public void product(int unit){
			
	}

	public void action(){

	}
	public Graphics draw(Graphics g){
		float r = collisionBox.getBoundingCircleRadius();
		g.drawImage(this.image, this.x-this.sizeX/2, this.y-this.sizeY, this.x+this.sizeX/2f, this.y+this.sizeY/2f, 0, 0, 224, 384);
		if(this.lifePoints<this.maxLifePoints){
			// Lifepoints
			g.setColor(Color.red);
			g.draw(new Line(this.getX()-r,this.getY()-r-30f,this.getX()+r,this.getY()-r-30f));
			float x = this.lifePoints*2f*r/this.maxLifePoints;
			g.setColor(Color.green);
			g.draw(new Line(this.getX()-r,this.getY()-r-30f,this.getX()-r+x,this.getY()-r-30f));

		}
		// Construction points
		if(this.constructionPoints<this.maxLifePoints){
			g.setColor(Color.white);
			g.draw(new Line(this.getX()-r,this.getY()-r-50f,this.getX()+r,this.getY()-r-50f));
			float x = this.constructionPoints*2f*r/this.maxLifePoints;
			g.setColor(Color.blue);
			g.draw(new Line(this.getX()-r,this.getY()-r-50f,this.getX()-r+x,this.getY()-r-50f));
		}
		return g;
	}
}
