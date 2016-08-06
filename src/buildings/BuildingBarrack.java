package buildings;

import java.util.Vector;

import data.Attributs;
import model.Checkpoint;
import utils.BuildingsList;
import utils.UnitsList;

public class BuildingBarrack extends BuildingProduction{
	
	public static final int SPEARMAN = 0;
	public static final int CROSSBOWMAN = 1;

	public BuildingBarrack(float f, float h, int team) {
		teamCapturing= 0;
		this.setTeam(team);
		this.name = BuildingsList.Barracks.name();
		type= 3;
		this.initialize(f, h);
		// List of potential production (Spearman)
		this.queue = new Vector<Integer>();
		this.productionList = new Vector<UnitsList>();
		this.productionList.addElement(UnitsList.Spearman);
		this.productionList.addElement(UnitsList.Crossbowman);
		this.rallyPoint = new Checkpoint(this.x,this.y+this.getAttribut(Attributs.sizeY)/2);
	}

	

}
