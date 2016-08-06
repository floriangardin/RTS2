package buildings;

import java.util.Vector;

import data.Attributs;
import main.Main;
import model.Checkpoint;
import model.Player;
import technologies.Technologie;

public class BuildingUniversity extends BuildingTech {

	public Player player;
	
	public BuildingUniversity( float f, float h, int team) {
		// Init ProductionList
		this.productionList = new Vector<Technologie>();
		this.setTeam(team);
		
		this.productionList = new Vector<Technologie>();
			//this.productionList.addElement(new DualistAge2(this.p,this.player));
		
		this.queue = null;
		teamCapturing= getTeam();

		this.name = "university";
		this.initialize(f, h);
		type= 6;
		// List of potential production (Spearman
		
		this.updateProductionList();
		this.rallyPoint = new Checkpoint(this.x,this.y+this.getAttribut(Attributs.sizeY)/2);
	}
	public void removeProd() {
		if(this.queue!=null){
			this.getGameTeam().food += queue.tech.foodPrice;
			this.getGameTeam().gold += queue.tech.goldPrice;
			this.queue=null;
			this.setCharge(0f);
		}
	}

	public boolean product(int unit){
		if(this.queue==null && unit<this.productionList.size()){
			if(this.productionList.get(unit).tech.foodPrice<=this.getGameTeam().food
					&& this.productionList.get(unit).tech.goldPrice<=this.getGameTeam().gold){
				this.queue=this.productionList.get(unit);
				this.getGameTeam().gold-=this.productionList.get(unit).tech.goldPrice;
				this.getGameTeam().food-=this.productionList.get(unit).tech.foodPrice;
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
		if(this.queue!=null){
			this.setCharge(this.charge+Main.increment);
			if(this.charge>=this.queue.tech.prodTime){
				this.techTerminate(this.queue);
			}
		}
	}	
}
