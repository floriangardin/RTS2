package model;

import org.newdawn.slick.geom.Rectangle;

public class Building extends ActionObjet{
	Game g;
	float sizeX, sizeY;
	
	public Building(){}
	
	public Building(Plateau p,Game g,float x, float y){
		p.addBuilding(this);
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
}
