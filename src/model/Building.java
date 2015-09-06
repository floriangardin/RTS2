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
	public float maxLifePoints;
	public float constructionPoints;
	public float animation;
	public int potentialTeam;
	public int type;

	public Building(){}

	public Building(Plateau p,Game g,float x, float y){
		p.addBuilding(this);
		this.constructionPoints = 0f;
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
		if(this.potentialTeam!=w.owner.team){
			if(this.constructionPoints<=0f){
				this.potentialTeam = w.owner.team;
				
			}
			this.constructionPoints-=0.1f;
		}
		else if(this.constructionPoints<this.maxLifePoints){
			this.constructionPoints+=0.1f;
		}
		else{
			if(this.potentialTeam!=this.team){
				this.team = this.potentialTeam;
			}
		}
	}

	public void change(OutputBuilding ocb) {
		this.lifePoints = ocb.lifepoints;
		this.team = ocb.team;	
		this.maxLifePoints = ocb.maxlifepoints;
		this.constructionPoints = ocb.constrpoints;
		this.animation = ocb.animation;
		this.sight = ocb.sight;
	}

	public Building(OutputBuilding ocb, Plateau p){
		switch(ocb.typeBuilding){
		case 0: new BuildingMine(ocb,p); break;
		case 1: new BuildingMill(ocb,p); break;
		case 2: break;
		case 3: new Barrack(ocb,p); break;
		case 4: break;
		default:
		}
	}

	public void drawIsSelected(Graphics g){


		g.drawImage(this.selection_circle,this.getX()-5f-this.collisionBox.getWidth()/2,this.getY()-this.collisionBox.getHeight()/2-5f,this.getX()+this.collisionBox.getWidth()/2+5f,this.getY()+this.collisionBox.getHeight()/2+5f,0,0,this.selection_circle.getWidth(),this.selection_circle.getHeight());
		//g.draw(new Ellipse(this.getX(),this.getY()+4f*r/6f,r,r-5f));

	}	


}
