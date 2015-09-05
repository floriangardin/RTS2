package model;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;

import multiplaying.OutputModel.OutputBuilding;

public class Barrack extends ProductionBuilding{


	public Barrack(Plateau plateau, Game g, float f, float h) {
		teamCapturing= 0;
		team = 0;
		constructionPhase = false;
		destructionPhase = true;
		isCapturing = false;
		this.sight = 300f;
		this.p = plateau ;
		maxLifePoints = p.constants.barrackLifePoints;
		this.name = "Barrack";
		p.addBuilding(this);
		this.selection_circle = this.p.images.selection_circle.getScaledCopy(4f);
		type= 3;
		this.lifePoints = this.maxLifePoints;
		this.g = g;
		this.id = p.g.idBuilding;
		p.g.idBuilding+=1;
		this.x = f;
		this.y = h;
		this.sizeX = 120f; 
		this.sizeY = 120f;
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY,sizeX,sizeY);
		this.image = this.p.images.tent;
		// List of potential production (Spearman
		this.queue = new Vector<Integer>();
		this.productionTime = new Vector<Float>();
		this.productionList = new Vector<UnitsList>();
		this.productionList.addElement(UnitsList.Spearman);
		this.productionTime.addElement(this.p.constants.spearmanProdTime);
		this.productionList.addElement(UnitsList.Bowman);
		this.productionTime.addElement(this.p.constants.bowmanProdTime);
		
		
		

	}

	public Barrack(OutputBuilding ocb, Plateau p){
		team = ocb.team;
		type= 3;
		maxLifePoints = ocb.maxlifepoints;
		this.p = p;
		p.addBuilding(this);
		this.lifePoints = this.maxLifePoints;
		this.g = p.g;
		this.x = ocb.x;
		this.y = ocb.y;
		this.id = ocb.id;
		this.sizeX = 120f; 
		this.sizeY = 120f;
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY,sizeX,sizeY);
		this.image = this.p.images.tent;
		// List of potential production (Spearman
		this.queue = new Vector<Integer>();
		this.productionTime = new Vector<Float>();
		this.productionList = new Vector<UnitsList>();
		this.productionList.addElement(UnitsList.Spearman);
		this.productionTime.addElement(this.p.constants.spearmanProdTime);
		this.productionList.addElement(UnitsList.Bowman);
		this.productionTime.addElement(this.p.constants.bowmanProdTime);
		
		
	}

	public void product(int unit){

		if(unit<this.productionList.size()){
			
			this.queue.add(unit);
		}
	}

	public void action(){
		//Do the action of Barrack
		//Product, increase state of the queue
		if(this.queue.size()>0){
			if(!this.isProducing){
				this.isProducing = true;
			}
			this.charge+=0.1f;
			if(this.charge>=this.productionTime.get(this.queue.get(0))){
				this.charge=0f;
				Character.createCharacter(p, team, x+(float)Math.random(), y+this.sizeY/2, this.productionList.get(this.queue.get(0)));
				this.queue.remove(0);
				if(this.queue.size()==0){
					this.isProducing =false;
				}
			}
		}
		else if(this.isProducing){
			this.isProducing = false;
		}
		// if reach production reset and create first unit in the queue
		
		if(this.lifePoints<10f){

			this.team = this.teamCapturing;
			this.lifePoints=this.maxLifePoints;
			this.constructionPhase = true;
		}
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
