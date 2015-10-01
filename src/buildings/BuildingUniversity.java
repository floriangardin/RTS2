package buildings;

import java.util.Vector;

import multiplaying.OutputModel.OutputBuilding;
import technologies.*;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;

import model.Checkpoint;
import model.Game;
import model.Plateau;
import model.Player;

public class BuildingUniversity extends BuildingTech {



	
	boolean isProducing;
	
	public Player player;
	
	public BuildingUniversity(Plateau plateau, Game g, float f, float h) {
		// Init ProductionList
		
		this.p = plateau ;
		this.player = this.p.g.players.get(team);
		this.productionList = new Vector<Technologie>();
		if(this.p.g.players.get(team).civ==0){
			this.productionList = new Vector<Technologie>();
			
			//this.productionList.addElement(new DualistAge2(this.p,this.player));

		}
		else if(this.p.g.players.get(team).civ==1){
			this.productionList = new Vector<Technologie>();
		}
		else{
			this.productionList = new Vector<Technologie>();
		}
		this.queue = null;
		teamCapturing= team;

		this.sizeX = this.p.g.players.get(team).data.universitySizeX; 
		this.sizeY = this.p.g.players.get(team).data.universitySizeY;
		this.sight = this.p.g.players.get(team).data.universitySight;
		maxLifePoints = this.p.g.players.get(team).data.universityLifePoints;
		this.name = "university";
		this.selection_circle = this.p.images.selection_rectangle.getScaledCopy(4f);
		type= 6;
		this.lifePoints = this.maxLifePoints;
		this.g = g;
		this.id = p.g.idChar;
		p.g.idChar+=1;
		this.x = f;
		this.y = h;
		p.addBuilding(this);
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY/2f,sizeX,sizeY);
		if(this.team == 1){
			this.image = this.p.images.buildingUniversityBlue;
		}
		else if(this.team == 2){
			this.image = this.p.images.buildingUniversityRed;
		}
		else {
			this.image = this.p.images.buildingUniversityNeutral;
		}
		// List of potential production (Spearman
		
		this.updateProductionList();
		this.rallyPoint = new Checkpoint(p,this.x,this.y+this.sizeY/2);
		this.updateImage();


	}
	public void removeProd() {
		if(this.queue!=null){
			this.p.g.players.get(this.team).food += queue.tech.foodPrice;
			this.p.g.players.get(this.team).gold += queue.tech.goldPrice;
			this.queue=null;
			this.charge = 0f;
		}
	}



	public void product(int unit){
		if(this.queue==null && unit<this.productionList.size()){
			if(this.productionList.get(unit).tech.foodPrice<=this.p.g.players.get(team).food
					&& this.productionList.get(unit).tech.goldPrice<=this.p.g.players.get(team).gold){
				this.queue=this.productionList.get(unit);
				this.p.g.players.get(team).gold-=this.productionList.get(unit).tech.goldPrice;
				this.p.g.players.get(team).food-=this.productionList.get(unit).tech.foodPrice;
			}
		}
	}
	public void action(){

		//Do the action of Barrack
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
	
}
