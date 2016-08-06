package buildings;

import java.util.Vector;

import data.Attributs;
import model.Checkpoint;
import utils.BuildingsList;
import utils.UnitsList;

public class BuildingAcademy extends BuildingProduction {

	
	public BuildingAcademy(float f, float h, int team) {
		teamCapturing= 0;
		//this.animation=-1f;
		this.setTeam(team);
		this.name = BuildingsList.Academy.name();
		this.initialize(f, h);
		type= 4;
		// List of potential production (Spearman
		this.queue = new Vector<Integer>();
		this.productionList = new Vector<UnitsList>();
		this.productionList.addElement(UnitsList.Inquisitor);
		this.rallyPoint = new Checkpoint(this.x,this.y+this.getAttribut(Attributs.sizeY)/2);
	}

	
}
