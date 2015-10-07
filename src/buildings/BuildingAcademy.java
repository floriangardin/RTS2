package buildings;

import java.util.Vector;

import org.newdawn.slick.geom.Rectangle;

import model.Checkpoint;
import model.Game;
import model.Plateau;
import multiplaying.OutputModel.OutputBuilding;
import units.UnitsList;

public class BuildingAcademy extends BuildingProduction {

	
	public BuildingAcademy(Plateau plateau, Game g, float f, float h) {
		teamCapturing= 0;
		this.animation=-1f;
		team = 0;
		this.p = plateau ;
		maxLifePoints = p.g.players.get(team).data.academyLifePoints;
		this.sizeX = this.p.g.players.get(team).data.academySizeX; 
		this.sizeY = this.p.g.players.get(team).data.academySizeY;
		this.sight = this.p.g.players.get(team).data.academySight;
		this.name = "academy";
		this.selection_circle = this.p.g.images.selection_rectangle.getScaledCopy(4f);
		type= 4;
		this.lifePoints = this.maxLifePoints;
		this.g = g;
		this.id = p.g.idChar;
		p.g.idChar+=1;
		this.x = f;
		this.y = h;
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY/2f,sizeX,sizeY);
		p.addBuilding(this);
		if(team==1){
			this.image = this.p.g.images.buildingAcademyBlue;
		} else if(team==2){
			this.image = this.p.g.images.buildingAcademyRed;
		} else {
			this.image = this.p.g.images.buildingAcademyNeutral;
		}
		// List of potential production (Spearman
		this.queue = new Vector<Integer>();
		this.productionTime = new Vector<Float>();
		this.productionList = new Vector<UnitsList>();
		this.productionList.addElement(UnitsList.Inquisitor);
		this.productionTime.addElement(UnitsList.Inquisitor.time);
		this.rallyPoint = new Checkpoint(p,this.x,this.y+this.sizeY/2);
		this.updateImage();
	}

	public BuildingAcademy(OutputBuilding ocb, Plateau p){
		team = ocb.team;
		new BuildingAcademy(p,p.g,ocb.x,ocb.y);
		this.id = ocb.id;

	}


	
	
}
