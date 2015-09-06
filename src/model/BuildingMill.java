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

		p.addBuilding(this);
		this.x = x;
		this.y = y;
		this.animation = -1f;
		this.p =p;
		this.g =g;
		this.id = p.g.idChar;
		p.g.idChar+=1;
		this.type = 1;
		this.selection_circle = this.p.images.selection_rectangle.getScaledCopy(4f);
		this.name= "Mill";
		this.maxLifePoints = p.constants.millLifePoints;
		this.chargeTime = p.constants.millChargeTime;
		this.lifePoints = p.constants.millLifePoints;
		this.sizeX = p.constants.millSizeX; 
		this.sizeY = p.constants.millSizeY;
		this.sight = p.constants.millSight;
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY/2f,sizeX,sizeY);
		if(team==1){
			this.image = this.p.images.buildingMillBlue;
		} else if(team==2){
			this.image = this.p.images.buildingMillRed;
		} else {
			this.image = this.p.images.buildingMillNeutral;
		}
		
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
		this.sight = ocb.sight;
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY/2f,sizeX,sizeY);
		this.selection_circle = this.p.images.selection_rectangle.getScaledCopy(4f);
		if(ocb.team==1){
			this.image = this.p.images.buildingMillBlue;
		} else if(ocb.team==2){
			this.image = this.p.images.buildingMillRed;
		} else {
			this.image = this.p.images.buildingMillNeutral;
		}
	}
	
	public void action(){
		this.state+=0.1f;
		if(this.team!=0)
			this.animation+=2f;
		if(animation>120f)
			animation = 0f;
		
		if(state >= chargeTime && team!=0){
			this.p.g.players.get(team).food+=1;
			state = 0;
		}
		if(this.lifePoints<10f){
			
			this.team = this.teamCapturing;
			if(team==1){
				this.image = this.p.images.buildingMillBlue;
			} else if(team==2){
				this.image = this.p.images.buildingMillRed;
			} else {
				this.image = this.p.images.buildingMillNeutral;
			}
			this.lifePoints=this.maxLifePoints;
			
		}
	}

	
	public Graphics draw(Graphics g){
		float r = collisionBox.getBoundingCircleRadius();
		g.drawImage(this.image, this.x-this.sizeX/2, this.y-this.sizeY, this.x+this.sizeX/2f, this.y+this.sizeY/2f, 0, 0, 295, 295);
		if(animation>=0f){
			g.drawImage(this.p.images.smoke, this.x+1f/18f*sizeX-12f, this.y-144f,this.x+1f/18f*sizeX+36f, this.y-96f, (int)(animation/30f)*64, 64, ((int)(animation/30f)+1)*64, 128);
		}
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
