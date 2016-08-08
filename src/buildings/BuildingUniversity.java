package buildings;

import java.util.Vector;

import data.Attributs;
import main.Main;
import model.Checkpoint;
import model.Player;
import technologies.Technologie;
import utils.ObjetsList;

public class BuildingUniversity extends Building{

	public Player player;
	
	public BuildingUniversity( float f, float h, int team) {
		// Init ProductionList
		super(ObjetsList.University,f ,h);
		this.setTeam(team);
		

		this.initialize(f, h);
		type= 6;
		// List of potential production (Spearman
		
		this.updateProductionList();
		this.rallyPoint = new Checkpoint(this.x,this.y+this.getAttribut(Attributs.sizeY)/2);
	}
	public void removeProd() {
		if(this.queueTechnology!=null){
			this.getGameTeam().food += queueTechnology.tech.foodPrice;
			this.getGameTeam().gold += queueTechnology.tech.goldPrice;
			this.queueTechnology=null;
			this.setCharge(0f);
		}
	}

	public boolean product(int unit){
		if(this.queueTechnology==null && unit<getTechnologyList().size()){
			float goldCost = getAttribut(getTechnologyList().get(unit),Attributs.goldCost);
			float foodCost = getAttribut(getTechnologyList().get(unit),Attributs.foodCost);
			float faithCost = getAttribut(getTechnologyList().get(unit),Attributs.faithCost);
			float prodTime = getAttribut(getTechnologyList().get(unit),Attributs.prodTime);
			if(foodCost<=this.getGameTeam().food
					&& goldCost<=this.getGameTeam().gold){
				this.queueTechnology = Technologie.technologie(getTechnologyList().get(unit), this.getGameTeam().id);
				this.getGameTeam().gold-=goldCost;
				this.getGameTeam().food-=foodCost;
				return true;
			}
		}
		return false;
	}
	
	public void action(){
		giveUpProcess();
		if(underAttackRemaining>0f){
			this.underAttackRemaining-=Main.increment;
		}
		else{
			this.underAttack = false;
		}
		//Do the action of Barrack
		//Product, increase state of the queue
		if(this.queueTechnology!=null){
			this.setCharge(this.charge+Main.increment);
			if(this.charge>=this.queueTechnology.tech.prodTime){
				this.techTerminate(this.queueTechnology);
			}
		}
	}	
}
