package buildings;

import java.util.Vector;

import data.Attributs;
import model.Checkpoint;
import utils.BuildingsList;
import utils.UnitsList;

public class BuildingStable extends BuildingProduction{


	public BuildingStable( float f, float h, int team) {
		teamCapturing= 0;
		
		this.setTeam(team);
		this.name = BuildingsList.Stable.name();
		type= 2;
		this.initialize(f, h);
		// List of potential production (Spearman
		this.queue = new Vector<Integer>();
		this.productionList = new Vector<UnitsList>();
		this.productionList.addElement(UnitsList.Knight);
		this.productionList.addElement(UnitsList.Inquisitor);
		//this.productionList.addElement(UnitsList.Priest);
		this.rallyPoint = new Checkpoint(this.x,this.y+this.getAttribut(Attributs.sizeY)/2);
	}

	

	
}

