package buildings;

import java.util.Vector;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

import model.Checkpoint;
import model.Game;
import model.Plateau;
import technologies.Technologie;

public class BuildingMill extends BuildingTech{
	
	public int chargeTime;
	public float state;
	public Image millarms;
	public int bonusProd;
	
	public BuildingMill(Plateau p,Game g,float x, float y){
		
		teamCapturing= 0;
		team = 0;

		this.x = x;
		this.y = y;
		this.animation = -1f;
		this.p =p;
		this.g =g;
		this.id = p.g.idChar;
		p.g.idChar+=1;
		this.type = 1;
		this.selection_circle = this.p.g.images.selection_rectangle.getScaledCopy(4f);
		this.name= "mill";
		
		this.maxLifePoints = p.g.players.get(team).data.millLifePoints;
		this.chargeTime = p.g.players.get(team).data.millChargeTime;
		this.lifePoints = p.g.players.get(team).data.millLifePoints;
		this.sizeX = p.g.players.get(team).data.millSizeX; 
		this.sizeY = p.g.players.get(team).data.millSizeY;
		p.addBuilding(this);
		this.sight = p.g.players.get(team).data.millSight;
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY/2f,sizeX,sizeY);
		if(team==1){
			this.image = this.p.g.images.buildingMillBlue;
		} else if(team==2){
			this.image = this.p.g.images.buildingMillRed;
		} else {
			this.image = this.p.g.images.buildingMillNeutral;
		}
		this.rallyPoint = new Checkpoint(p,this.x,this.y+this.sizeY/2);
		this.productionList = new Vector<Technologie>();
		this.updateProductionList();
		this.updateImage();
	}

	
	public void action(){
		this.state+=0.1f;
		if(this.team!=0)
			this.animation+=2f;
		if(animation>120f)
			animation = 0f;
		
		if(state >= chargeTime && team!=0){
			this.p.g.players.get(team).food+=2+this.p.g.players.get(team).data.bonusFood;
			state = 0;
		}
		
		
		//Do the action of prod tech
		//Product, increase state of the queue
		if(this.queue!=null){
			if(!this.isProducing){
				this.isProducing = true;
			}
			this.animation+=2f;
			if(animation>120f)
				animation = 0f;
			this.charge+=0.1f;
			if(this.charge>=this.queue.tech.prodTime){
				this.techTerminate(this.queue);

			}
		}
		else if(this.isProducing){
			this.isProducing = false;
			this.animation = -1f;
		}

	}


	
	public void drawAnimation(Graphics g){
		if(animation>=0f){
			g.drawImage(this.p.g.images.smoke, this.x+1f/18f*sizeX-12f, this.y-144f,this.x+1f/18f*sizeX+36f, this.y-96f, (int)(animation/30f)*64, 64, ((int)(animation/30f)+1)*64, 128);
		}
	}
}
