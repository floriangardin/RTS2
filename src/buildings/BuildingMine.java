package buildings;

import java.util.Vector;

import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

import main.Main;
import model.Checkpoint;
import model.Game;
import model.Map;
import model.Plateau;
import technologies.Technologie;

public class BuildingMine extends BuildingTech{
	
	public int chargeTime;
	public int bonusProd;
	
	public BuildingMine(Plateau p,Game g,float f, float h, int team){
		if(underAttackRemaining>0f){
			this.underAttackRemaining-=Main.increment;
		}
		else{
			this.underAttack = false;
		}
		teamCapturing= 0;

		this.x = x*Map.stepGrid;
		this.y = y*Map.stepGrid;
		this.p =p;
		this.g =g;
		this.type = 0;
		this.selection_circle = this.p.g.images.selection_rectangle.getScaledCopy(4f);
		this.name= "mine";
		this.setTeam(team);
		this.soundSelection = new Vector<Sound>();
		this.soundSelection.addElement(this.g.sounds.mineSound);
		this.maxLifePoints = getGameTeam().data.millLifePoints;
		this.chargeTime = getGameTeam().data.mineChargeTime;
		this.lifePoints = getGameTeam().data.mineLifePoints;
		this.sizeX = getGameTeam().data.mineSizeX;
		this.sizeY = getGameTeam().data.mineSizeY;
		this.sight = getGameTeam().data.mineSight;
		this.initialize(f, h);
		if(getTeam()==1){
			this.image = this.p.g.images.buildingMineBlue;
		} else if(getTeam()==2){
			this.image = this.p.g.images.buildingMineRed;
		} else {
			this.image = this.p.g.images.buildingMineNeutral;
		}
		this.productionList = new Vector<Technologie>();
		this.updateProductionList();
	}
	
	public void action(){
		giveUpProcess();
		this.state+=Main.increment;

		
		if(state >= chargeTime && getTeam()!=0){
			getGameTeam().gold+=3+getGameTeam().data.bonusGold;
			state = 0;
		}
		
		
		//Do the action of Barrack
		//Product, increase state of the queue
		if(this.queue!=null){
			if(!this.isProducing){
				this.isProducing = true;
			}
			this.animation+=2f*Game.ratio;
			if(animation>120f)
				
			this.charge+=Main.increment;
			if(this.charge>=this.queue.tech.prodTime){
				this.techTerminate(this.queue);

			}
		}
		else if(this.isProducing){
			this.isProducing = false;
			
		}

		
	}
	
	
}
