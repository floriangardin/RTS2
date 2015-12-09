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
import model.Player;
import technologies.Technologie;

public class BuildingUniversity extends BuildingTech {

	public Player player;
	
	public BuildingUniversity(Plateau plateau, Game g, float f, float h) {
		// Init ProductionList
		this.p = plateau ;
		this.g = g;
		this.setTeam(0);
		this.productionList = new Vector<Technologie>();
		if(this.getGameTeam().civ==0){
			this.productionList = new Vector<Technologie>();
			//this.productionList.addElement(new DualistAge2(this.p,this.player));
		}
		else if(this.getGameTeam().civ==1){
			this.productionList = new Vector<Technologie>();
		}
		else{
			this.productionList = new Vector<Technologie>();
		}
		this.queue = null;
		teamCapturing= getTeam();

		this.sizeX = getGameTeam().data.universitySizeX; 
		this.sizeY = getGameTeam().data.universitySizeY;
		this.sight = getGameTeam().data.universitySight;
		maxLifePoints = getGameTeam().data.universityLifePoints;
		this.name = "university";
		this.soundSelection = new Vector<Sound>();
		this.soundSelection.addElement(this.g.sounds.universitySound);
		this.selection_circle = this.p.g.images.selection_rectangle.getScaledCopy(4f);
		type= 6;
		this.g = g;
		this.initialize(f, h);
		if(this.getTeam() == 1){
			this.image = this.p.g.images.buildingUniversityBlue;
		}
		else if(getTeam() == 2){
			this.image = this.p.g.images.buildingUniversityRed;
		}
		else {
			this.image = this.p.g.images.buildingUniversityNeutral;
		}
		// List of potential production (Spearman
		
		this.updateProductionList();
		this.rallyPoint = new Checkpoint(p,this.x,this.y+this.sizeY/2);
		this.updateImage();


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
