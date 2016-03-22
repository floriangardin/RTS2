package buildings;

import java.util.Vector;

import display.DisplayRessources;
import main.Main;
import model.Data;
import model.Game;
import model.Plateau;
import ressources.Map;
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
		this.selection_circle = this.p.g.images.get("rectSelectsizeBuilding");
		this.name= "mine";
		this.setTeam(team);
		this.maxLifePoints = getGameTeam().data.millLifePoints;
		this.chargeTime = getGameTeam().data.mineChargeTime;
		this.lifePoints = getGameTeam().data.mineLifePoints;
		this.sizeX = Data.mineSizeX;
		this.sizeY = Data.mineSizeY;
		this.sight = getGameTeam().data.mineSight;
		this.initialize(f, h);
		this.productionList = new Vector<Technologie>();
		this.updateProductionList();
	}
	
	
	public void action(){
		giveUpProcess();
		this.state+=Main.increment;

		
		if(state >= chargeTime && getTeam()!=0){
			getGameTeam().gold+=6+getGameTeam().data.bonusGold;
			if(this.gameteam==Game.g.currentPlayer.getGameTeam()){
				Game.g.addDisplayRessources(new DisplayRessources(6+getGameTeam().data.bonusGold, "gold", this.x, this.y));
			}
			state = 0;
		}
		
		
		//Do the action of Barrack
		//Product, increase state of the queue
		if(this.queue!=null){
			if(!this.isProducing){
				this.isProducing = true;
			}
			this.animation=(int) ((this.animation+2f)%120);
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
