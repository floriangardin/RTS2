package model;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;

import multiplaying.OutputModel.OutputBuilding;

public class BuildingMine extends Building{
	
	public int chargeTime;
	public float state;
	
	public BuildingMine(Plateau p,Game g,float x, float y){
		teamCapturing= 0;
		team = 0;

		p.addBuilding(this);
		this.x = x;
		this.y = y;
		this.p =p;
		this.g =g;
		this.id = p.g.idChar;
		p.g.idChar+=1;
		this.type = 0;
		this.selection_circle = this.p.images.selection_rectangle.getScaledCopy(4f);
		this.name= "Mine";
		this.maxLifePoints = p.constants.millLifePoints;
		this.chargeTime = p.constants.mineChargeTime;
		this.lifePoints = p.constants.mineLifePoints;
		this.sizeX = p.constants.mineSizeX;
		this.sizeY = p.constants.mineSizeY;
		this.sight = p.constants.mineSight;
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY/2f,sizeX,sizeY);
		if(team==1){
			this.image = this.p.images.buildingMineBlue;
		} else if(team==2){
			this.image = this.p.images.buildingMineRed;
		} else {
			this.image = this.p.images.buildingMineNeutral;
		}
		
	}
	
	public BuildingMine(OutputBuilding ocb, Plateau p){
		p.addBuilding(this);
		this.x = ocb.x;
		this.y = ocb.y;
		this.p =p;
		this.id = ocb.id;
		this.g =p.g;
		this.team=ocb.team;
		this.chargeTime = p.constants.millChargeTime;
		this.selection_circle = this.p.images.selection_rectangle.getScaledCopy(4f);
		this.lifePoints = ocb.lifepoints;
		this.sizeX = ocb.sizeX; 
		this.sizeY = ocb.sizeY;
		this.sight = ocb.sight;
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY/2f,sizeX,sizeY);
		if(team==1){
			this.image = this.p.images.buildingMineBlue;
		} else if(team==2){
			this.image = this.p.images.buildingMineRed;
		} else {
			this.image = this.p.images.buildingMineNeutral;
		}
	}
	
	public void action(){
		this.state+=0.1f;
		if(state >= chargeTime && team!=0){
			this.p.g.players.get(team).gold+=1;
			this.state = 0f;
		}
		if(this.lifePoints<10f){
			this.team = this.teamCapturing;
			if(team==1){
				this.image = this.p.images.buildingMineBlue;
			} else if(team==2){
				this.image = this.p.images.buildingMineRed;
			} else {
				this.image = this.p.images.buildingMineNeutral;
			}
			this.lifePoints=this.maxLifePoints;
			
		}
	}
	
	public Graphics draw(Graphics g){
		float r = collisionBox.getBoundingCircleRadius();
		g.drawImage(this.image, this.x-this.sizeX/2, this.y-this.sizeY, this.x+this.sizeX/2f, this.y+this.sizeY/2f, 0, 0, 291, 291);
		if(this.lifePoints<this.maxLifePoints){
			// Lifepoints
			g.setColor(Color.red);
			g.draw(new Line(this.getX()-r,this.getY()-r-30f,this.getX()+r,this.getY()-r-30f));
			float x = this.lifePoints*2f*r/this.maxLifePoints;
			g.setColor(Color.green);
			g.draw(new Line(this.getX()-r,this.getY()-r-30f,this.getX()-r+x,this.getY()-r-30f));

		}
		// Construction points
		if(this.constructionPoints<this.maxLifePoints && this.constructionPoints>0f){
			g.setColor(Color.white);
			g.draw(new Line(this.getX()-r,this.getY()-r-50f,this.getX()+r,this.getY()-r-50f));
			float x = this.constructionPoints*2f*r/this.maxLifePoints;
			g.setColor(Color.blue);
			g.draw(new Line(this.getX()-r,this.getY()-r-50f,this.getX()-r+x,this.getY()-r-50f));
		}
		return g;
	}
}
