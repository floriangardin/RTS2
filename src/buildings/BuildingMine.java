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

		this.x = x;
		this.y = y;
		this.p =p;
		this.setTeam(0);
		this.g =g;
		this.id = p.g.idChar;
		p.g.idChar+=1;
		this.type = 0;
		this.selection_circle = this.p.g.images.selection_rectangle.getScaledCopy(4f);
		this.name= "mine";
		this.maxLifePoints = getGameTeam().data.millLifePoints;
		this.chargeTime = getGameTeam().data.mineChargeTime;
		this.lifePoints = getGameTeam().data.mineLifePoints;
		this.sizeX = getGameTeam().data.mineSizeX;
		this.sizeY = getGameTeam().data.mineSizeY;
		p.addBuilding(this);
		this.sight = getGameTeam().data.mineSight;
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY/2f,sizeX,sizeY);
		if(getTeam()==1){
			this.image = this.p.g.images.buildingMineBlue;
		} else if(getTeam()==2){
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

		
		if(state >= chargeTime && getTeam()!=0){
			getGameTeam().gold+=1+getGameTeam().data.bonusGold;
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
