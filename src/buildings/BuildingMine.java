package buildings;

import java.util.Vector;

import org.newdawn.slick.geom.Rectangle;

import model.Checkpoint;
import model.Game;
import model.Plateau;
import technologies.Technologie;

public class BuildingMine extends BuildingTech{
	
	public int chargeTime;
	public float state;
	public int bonusProd;
	
	public BuildingMine(Plateau p,Game g,float x, float y){
		
		teamCapturing= 0;
		team = 0;

		this.x = x;
		this.y = y;
		this.p =p;
		this.g =g;
		this.id = p.g.idChar;
		p.g.idChar+=1;
		this.type = 0;
		this.selection_circle = this.p.g.images.selection_rectangle.getScaledCopy(4f);
		this.name= "mine";
		this.maxLifePoints = p.g.players.get(team).data.millLifePoints;
		this.chargeTime = p.g.players.get(team).data.mineChargeTime;
		this.lifePoints = p.g.players.get(team).data.mineLifePoints;
		this.sizeX = p.g.players.get(team).data.mineSizeX;
		this.sizeY = p.g.players.get(team).data.mineSizeY;
		p.addBuilding(this);
		this.sight = p.g.players.get(team).data.mineSight;
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY/2f,sizeX,sizeY);
		if(team==1){
			this.image = this.p.g.images.buildingMineBlue;
		} else if(team==2){
			this.image = this.p.g.images.buildingMineRed;
		} else {
			this.image = this.p.g.images.buildingMineNeutral;
		}
		this.rallyPoint = new Checkpoint(p,this.x,this.y+this.sizeY/2);
		this.productionList = new Vector<Technologie>();
		this.updateProductionList();
		this.updateImage();
	}
	
	

	
	public void action(){
		this.state+=0.1f;

		
		if(state >= chargeTime && team!=0){
			this.p.g.players.get(team).gold+=1+this.p.g.players.get(team).data.bonusGold;
			state = 0;
		}
		
		
		//Do the action of Barrack
		//Product, increase state of the queue
		if(this.queue!=null){
			if(!this.isProducing){
				this.isProducing = true;
			}
			this.animation+=2f;
			if(animation>120f)
				
			this.charge+=0.1f;
			if(this.charge>=this.queue.tech.prodTime){
				this.techTerminate(this.queue);

			}
		}
		else if(this.isProducing){
			this.isProducing = false;
			
		}

		
	}
	
	
}
