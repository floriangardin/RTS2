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

		this.sizeX = this.p.constants.headQuartersSizeX; 
		this.sizeY = this.p.constants.headQuartersSizeY;
		this.sight = this.p.constants.headQuartersSight;
		maxLifePoints = p.constants.headQuartersLifePoints;
		this.name = "university";
		p.addBuilding(this);
		this.selection_circle = this.p.images.selection_rectangle.getScaledCopy(4f);
		type= 6;
		this.lifePoints = this.maxLifePoints;
		this.g = g;
		this.id = p.g.idChar;
		p.g.idChar+=1;
		this.x = f;
		this.y = h;
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


	}
	public void removeProd() {
		if(this.queue!=null){
			this.p.g.players.get(this.team).food += queue.tech.foodPrice;
			this.p.g.players.get(this.team).gold += queue.tech.goldPrice;
			this.queue=null;
			this.charge = 0f;
		}
	}

	public BuildingUniversity(OutputBuilding ocb, Plateau p,BuildingHeadQuarters hq){
		this.hq = hq;
		team = ocb.team;
		type= 5;
		maxLifePoints = ocb.maxlifepoints;
		this.p = p;
		p.addBuilding(this);
		this.lifePoints = this.maxLifePoints;
		this.g = p.g;
		this.x = ocb.x;
		this.y = ocb.y;
		this.selection_circle = this.p.images.selection_rectangle.getScaledCopy(4f);
		this.id = ocb.id;
		this.sizeX = this.p.constants.headQuartersSizeX; 
		this.sizeY = this.p.constants.headQuartersSizeY;
		this.sight = this.p.constants.headQuartersSight;
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY/2f,sizeX,sizeY);
		if(this.team == 1){
			this.image = this.p.images.buildingHeadQuartersBlue;
		}
		else if(this.team == 2){
			this.image = this.p.images.buildingHeadQuartersRed;
		}
		else {
			this.image = this.p.images.tent;
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
	public Graphics draw(Graphics g){
		float r = collisionBox.getBoundingCircleRadius();
		g.drawImage(this.image, this.x-this.sizeX/2, this.y-this.sizeY, this.x+this.sizeX/2f, this.y+this.sizeY/2f, 0, 0, 367, 355);
		if(this.lifePoints<this.maxLifePoints){
			// Lifepoints
			g.setColor(Color.red);
			g.draw(new Line(this.getX()-r,this.getY()-r-30f,this.getX()+r,this.getY()-r-30f));
			float x = this.lifePoints*2f*r/this.maxLifePoints;
			g.setColor(Color.green);
			g.draw(new Line(this.getX()-r,this.getY()-r-30f,this.getX()-r+x,this.getY()-r-30f));
		}
		this.drawConstructionBar(g);
		
		return g;
	}
}
