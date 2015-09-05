package model;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;

import multiplaying.OutputModel.OutputBuilding;

public class BuildingMill extends Building{
	
	public int chargeTime;
	public float state;
	public Image millarms;
	
	public BuildingMill(Plateau p,Game g,float x, float y){
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
		this.type = 1;
		this.selection_circle = this.p.images.selection_circle.getScaledCopy(4f);
		this.sight = 300f;
		this.name= "Mill";
		this.maxLifePoints = p.constants.millLifePoints;
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
		this.id = ocb.id;
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
		this.state+=0.1f;
		this.millarms.rotate(0.2f);
		
		if(state >= chargeTime && team!=0){
			
			this.p.g.players.get(team).food+=1;
			state = 0;
		}
		if(this.lifePoints<10f){
			
			this.team = this.teamCapturing;
			this.lifePoints=this.maxLifePoints;
			this.constructionPhase = true;
		}
	}

	
	public Graphics draw(Graphics g){
		float r = collisionBox.getBoundingCircleRadius();
		g.drawImage(this.image, this.x-152f, this.y-270f, this.x+112f, this.y+100f, 0f,0f,224f,320f);
		g.drawImage(this.millarms,this.x-194f,this.y-370f);
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
