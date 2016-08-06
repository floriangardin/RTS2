package buildings;

import java.util.Vector;

import model.Checkpoint;
import model.Data;
import model.Game;
import model.Plateau;
import units.UnitsList;

public class BuildingStable extends BuildingProduction{


	public BuildingStable( float f, float h, int team) {
		teamCapturing= 0;
		
		this.setTeam(team);
		maxLifePoints = getGameTeam().data.stableLifePoints;
		this.sizeX = Data.stableSizeX; 
		this.sizeY = Data.stableSizeY;
		this.sight = this.getGameTeam().data.stableSight;
		this.name = "stable";
		this.printName = "Ecurie";
		type= 2;
		this.initialize(f, h);
		// List of potential production (Spearman
		this.queue = new Vector<Integer>();
		this.productionList = new Vector<UnitsList>();
		this.productionList.addElement(UnitsList.Knight);
		this.productionList.addElement(UnitsList.Inquisitor);
		//this.productionList.addElement(UnitsList.Priest);
		this.rallyPoint = new Checkpoint(this.x,this.y+this.sizeY/2);
	}

	

	
}

