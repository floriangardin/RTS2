package model;

import org.newdawn.slick.geom.Rectangle;

public class Building extends ActionObjet{
	Game g;
	float sizeX, sizeY;
	int teamCapturing;
	boolean isCapturing;
	boolean destructionPhase;
	float maxLifePoints;
	float constructionPoints;
	boolean constructionPhase;
	
	
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
		
	}
	
	
	public void collision(Weapon w){
		if(!isCapturing){
			isCapturing = true;
			teamCapturing = w.team;
		}
		if(constructionPhase){
			this.constructionPoints+=0.1f;
		}
		
	}

	
}
