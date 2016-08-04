package buildings;

import java.util.Vector;

import model.Checkpoint;
import model.Data;
import model.Game;
import model.Plateau;
import units.UnitsList;

public class BuildingAcademy extends BuildingProduction {

	
	public BuildingAcademy(Plateau plateau, Game g, float f, float h, int team) {
		teamCapturing= 0;
		//this.animation=-1f;
		this.p = plateau ;
		this.setTeam(team);
		maxLifePoints = this.getGameTeam().data.academyLifePoints;
		this.sizeX = Data.academySizeX; 
		this.sizeY = Data.academySizeY;
		this.sight = this.getGameTeam().data.academySight;
		this.initialize(f, h);
		this.name = "academy";
		this.printName = "Académie";
		this.selection_circle = Game.g.images.get("rectSelectsizeBuilding");
		type= 4;
		Game.g = g;
		// List of potential production (Spearman
		this.queue = new Vector<Integer>();
		this.productionList = new Vector<UnitsList>();
		this.productionList.addElement(UnitsList.Inquisitor);
		this.rallyPoint = new Checkpoint(p,this.x,this.y+this.sizeY/2);
	}

	
}
