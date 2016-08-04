package buildings;

import java.util.Vector;

import model.Checkpoint;
import model.Data;
import model.Game;
import model.Plateau;
import units.UnitsList;

public class BuildingAcademy extends BuildingProduction {

	
	public BuildingAcademy(float f, float h, int team) {
		teamCapturing= 0;
		//this.animation=-1f;
		this.setTeam(team);
		maxLifePoints = this.getGameTeam().data.academyLifePoints;
		this.sizeX = Data.academySizeX; 
		this.sizeY = Data.academySizeY;
		this.sight = this.getGameTeam().data.academySight;
		this.initialize(f, h);
		this.name = "academy";
		this.printName = "Académie";
		type= 4;
		// List of potential production (Spearman
		this.queue = new Vector<Integer>();
		this.productionList = new Vector<UnitsList>();
		this.productionList.addElement(UnitsList.Inquisitor);
		this.rallyPoint = new Checkpoint(this.x,this.y+this.sizeY/2);
	}

	
}
