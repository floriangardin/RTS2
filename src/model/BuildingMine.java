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
		this.constructionPhase = false;
		isCapturing=false;
		p.addBuilding(this);
		this.x = x;
		this.y = y;
		this.p =p;
		this.g =g;
		this.id = p.g.idBuilding;
		p.g.idBuilding+=1;
		this.type = 0;
		this.selection_circle = this.p.images.selection_circle.getScaledCopy(4f);
		this.sight = 300f;
		this.name= "Mine";
		this.maxLifePoints = p.constants.millLifePoints;
		this.chargeTime = p.constants.mineChargeTime;
		this.lifePoints = p.constants.mineLifePoints;
		this.sizeX = 150f; 
		this.sizeY = 200f;
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY,sizeX,sizeY);
		this.image = this.p.images.windmill;
		
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
		this.lifePoints = ocb.lifepoints;
		this.sizeX = ocb.sizeX; 
		this.sizeY = ocb.sizeY;
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY,sizeX,sizeY);
		this.image = this.p.images.windmill;
	}
	
	public void action(){
		this.state+=0.1f;
		if(state >= chargeTime && team!=0){
			this.p.g.players.get(team).gold+=1;
			this.state = 0f;
		}
		if(this.lifePoints<10f){
			this.team = this.teamCapturing;
			this.lifePoints=this.maxLifePoints;
			this.constructionPhase = true;
		}
	}
	
	public Graphics draw(Graphics g){
		float r = collisionBox.getBoundingCircleRadius();
		g.drawImage(this.image, this.x-152f, this.y-320f);
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
