package model;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

import multiplaying.OutputModel.OutputBuilding;

public class BuildingMill extends Building{
	
	public int chargeTime;
	public int state;
	public Image millarms;
	
	public BuildingMill(Plateau p,Game g,float x, float y){
		p.addBuilding(this);
		this.x = x;
		this.y = y;
		this.id = p.g.idBuilding;
		p.g.idBuilding+=1;
		this.p =p;
		this.g =g;
		this.team=0;
		this.chargeTime = p.constants.millChargeTime;
		this.lifePoints = p.constants.millLifePoints;
		this.sizeX = 220f; 
		this.sizeY = 220f;
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY,sizeX,sizeY);
		this.image = this.p.images.windmill;
		this.millarms = this.p.images.windmillarms.getSubImage(0, 0, 288, 320);
		
	}
	
	public BuildingMill(OutputBuilding ocb, Plateau p){
		p.addBuilding(this);
		this.x = ocb.x;
		this.y = ocb.y;
		this.p =p;
		this.g =p.g;
		this.team=ocb.team;
		this.chargeTime = p.constants.millChargeTime;
		this.lifePoints = ocb.lifepoints;
		this.sizeX = ocb.sizeX; 
		this.sizeY = ocb.sizeY;
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY,sizeX,sizeY);
		this.image = this.p.images.windmill;
		this.millarms = this.p.images.windmillarms.getSubImage(0, 0, 288, 320);
	}
	
	public void action(){
		this.state+=1;
		this.millarms.rotate(0.2f);
		if(state == chargeTime && team!=0){
			this.p.g.players.get(team).food+=1;
			state = 0;
		}
	}
	
	public Graphics draw(Graphics g){
		g.drawImage(this.image, this.x-112f, this.y-220f, this.x+112f, this.y+100f, 0f,0f,224f,320f);
		g.drawImage(this.millarms,this.x-144f,this.y-320f);
		return g;
	}
}
