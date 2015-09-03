package model;

import org.newdawn.slick.geom.Rectangle;

public class BuildingMine extends Building{
	
	public int chargeTime;
	public int state;
	
	public BuildingMine(Plateau p,Game g,float x, float y){
		p.addBuilding(this);
		this.x = x;
		this.y = y;
		this.p =p;
		this.g =g;
		this.team=0;
		this.chargeTime = p.constants.mineChargeTime;
		this.lifePoints = p.constants.mineLifePoints;
		this.sizeX = 120f; 
		this.sizeY = 120f;
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY,sizeX,sizeY);
		this.image = this.p.images.tent;
	}
	
	public void action(){
		this.state+=1;
		if(state == chargeTime && team!=0){
			this.p.g.players.get(team).gold+=1;
		}
	}
}
