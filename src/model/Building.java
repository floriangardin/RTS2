package model;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import multiplaying.OutputModel.OutputBuilding;

public class Building extends ActionObjet{
	Game g;
	public float sizeX;
	public float sizeY;
	int teamCapturing;
	boolean isCapturing;
	boolean destructionPhase;
	public float maxLifePoints;
	public float constructionPoints;
	boolean constructionPhase;

	public int type;

	public Building(){}

	public Building(Plateau p,Game g,float x, float y){
		p.addBuilding(this);
		this.constructionPoints = 0f;
		
		destructionPhase = true;
		isCapturing = false;
		teamCapturing = 0;
		this.x = x;
		this.y = y;
		this.p =p;
		this.g =g;
		this.team=0;
		this.lifePoints = 1f;
		this.sizeX = 120f; 
		this.sizeY = 120f;
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY,sizeX,sizeY);
		this.image = this.p.images.tent;
		this.sight = 300f;
	}


	public void collision(Weapon w){
		if(!isCapturing){

			isCapturing = true;
			teamCapturing = w.owner.team;
		}

		if(constructionPhase && w.owner.team==this.team){

			this.constructionPoints+=0.1f;
			if(this.constructionPoints>maxLifePoints){
				this.constructionPhase=false;
				this.destructionPhase = true;
				this.constructionPoints = 0f;
				this.isCapturing = false;
				
			}
		}

	}

	public void change(OutputBuilding ocb) {
		this.lifePoints = ocb.lifepoints;
		this.team = ocb.team;	
		this.maxLifePoints = ocb.maxlifepoints;
		this.constructionPoints = ocb.constrpoints;
	}

	public Building(OutputBuilding ocb, Plateau p){
		switch(ocb.typeBuilding){
		case 0: new BuildingMill(ocb,p); break;
		case 1: break;
		case 2: break;
		case 3: new Barrack(ocb,p); break;
		case 4: break;
		default:
		}
	}

	public void drawIsSelected(Graphics g){
		g.setColor(Color.green);
		
		
		g.drawImage(this.selection_circle,-60f+this.getX()-this.collisionBox.getBoundingCircleRadius()/2f,-100f+this.getY()-this.collisionBox.getBoundingCircleRadius()/2f);
		//g.draw(new Ellipse(this.getX(),this.getY()+4f*r/6f,r,r-5f));

	}	


}
