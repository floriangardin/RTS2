package buildings;

import java.util.Vector;

import data.Attributs;
import display.DisplayRessources;
import main.Main;
import model.Game;
import ressources.Map;
import technologies.Technologie;
import utils.ObjetsList;

public class BuildingMine extends Building{
	
	public float chargeTime;
	public int bonusProd;
	
	public BuildingMine(float f, float h, int team){
		super(ObjetsList.Mine,f,h);
		if(underAttackRemaining>0f){
			this.underAttackRemaining-=Main.increment;
		}
		else{
			this.underAttack = false;
		}
		teamCapturing= 0;

		this.x = x*Map.stepGrid;
		this.y = y*Map.stepGrid;
		this.type = 0;
		this.name= ObjetsList.Mine.name().toLowerCase();
		this.setTeam(team);
		this.chargeTime = getAttribut(Attributs.maxChargetime);
		this.lifePoints = getAttribut(Attributs.maxLifepoints);
		this.initialize(f, h);
		
		this.updateProductionList();
	}
	
	
	public void action(){
		giveUpProcess();
		this.state+=Main.increment;

		
		if(state >= chargeTime && getTeam()!=0){
			getGameTeam().gold+=6+getGameTeam().data.bonusGold;
			if(this.team==Game.g.currentPlayer.getGameTeam().id){
				Game.g.addDisplayRessources(new DisplayRessources(6+getGameTeam().data.bonusGold, "gold", this.x, this.y));
			}
			state = 0;
		}
			
		//Do the action of Barrack
		//Product, increase state of the queue
		if(this.queueTechnology!=null){
			if(!this.isProducing){
				this.isProducing = true;
			}
			this.animation=(int) ((this.animation+2f)%120);
			this.charge+=Main.increment;
			if(this.charge>=this.queueTechnology.tech.prodTime){
				this.techTerminate(this.queueTechnology);

			}
		}
		else if(this.isProducing){
			this.isProducing = false;
			
		}

		
	}
	
	
}
