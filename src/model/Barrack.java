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
		this.animation=-1f;
		team = 0;
		constructionPhase = false;
		destructionPhase = true;
		isCapturing = false;
		this.p = plateau ;
		maxLifePoints = p.constants.barrackLifePoints;
		this.sizeX = this.p.constants.barrackSizeX; 
		this.sizeY = this.p.constants.barrackSizeY;
		this.sight = this.p.constants.barrackSight;
		this.name = "Barrack";
		p.addBuilding(this);
		this.selection_circle = this.p.images.selection_rectangle.getScaledCopy(4f);
		type= 3;
		this.lifePoints = this.maxLifePoints;
		this.g = g;
		this.id = p.g.idChar;
		p.g.idChar+=1;
		this.x = f;
		this.y = h;
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY/2f,sizeX,sizeY);
		if(team==1){
			this.image = this.p.images.buildingBarrackBlue;
		} else if(team==2){
			this.image = this.p.images.buildingBarrackRed;
		} else {
			this.image = this.p.images.buildingBarrackNeutral;
		}
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
		this.sizeX = this.p.constants.barrackSizeX; 
		this.sizeY = this.p.constants.barrackSizeY;
		this.sight = this.p.constants.barrackSight;
		this.selection_circle = this.p.images.selection_rectangle.getScaledCopy(4f);
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY,sizeX,sizeY);
		if(ocb.team==1){
			this.image = this.p.images.buildingBarrackBlue;
		} else if(ocb.team==2){
			this.image = this.p.images.buildingBarrackRed;
		} else {
			this.image = this.p.images.buildingBarrackNeutral;
		}
		
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
			this.animation+=3f;
			if(animation>120f)
				animation = 0f;
			this.charge+=0.1f;
			if(this.charge>=this.productionTime.get(this.queue.get(0))){
				this.charge=0f;
				Character.createCharacter(p, team, x+(float)Math.random(), y+this.sizeY/2, this.productionList.get(this.queue.get(0)));
				this.queue.remove(0);
				if(this.queue.size()==0){
					this.isProducing =false;
					this.animation = -1f;
				}
			}
		}
		else if(this.isProducing){
			this.isProducing = false;
			this.animation = -1f;
		}
		// if reach production reset and create first unit in the queue
		
		if(this.lifePoints<10f){

			this.team = this.teamCapturing;
			if(this.team==1)
				this.image = this.p.images.buildingBarrackBlue;
			if(this.team==2)
				this.image = this.p.images.buildingBarrackRed;
			this.lifePoints=this.maxLifePoints;
			this.constructionPhase = true;
		}
	}
	public Graphics draw(Graphics g){
		float r = collisionBox.getBoundingCircleRadius();
		g.drawImage(this.image, this.x-this.sizeX/2, this.y-this.sizeY, this.x+this.sizeX/2f, this.y+this.sizeY/2f, 0, 0, 285, 285);
		if(animation>=0f){
			g.drawImage(this.p.images.fire, this.x+5f/18f*sizeX, this.y-24f,this.x+5f/18f*sizeX+32f, this.y+24f, (int)(animation/30f)*32, 96, ((int)(animation/30f)+1)*32, 144);
			g.drawImage(this.p.images.fire, this.x-9f/18f*sizeX-2f, this.y-84f,this.x-9f/18f*sizeX+32f-2f, this.y-36f, (int)(animation/30f)*32, 96, ((int)(animation/30f)+1)*32, 144);
		}
		//g.drawImage(this.image,this.getX()-sizeX/2f,this.getY()-sizeY,this.getX()+sizeX/2f,this.getY()+1f*sizeY/6f,0f,0f,this.image.getWidth(),this.image.getHeight());
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
