package buildings;

import java.util.Vector;

import data.Attributs;
import model.Checkpoint;
import utils.ObjetsList;
import utils.ObjetsList;

public class BuildingStable extends Building{


	public BuildingStable( float f, float h, int team) {
		super(ObjetsList.Stable,f,h);
		teamCapturing= 0;
		
		this.setTeam(team);
		type= 2;
		this.initialize(f, h);
		// List of potential production (Spearman
		this.queue = new Vector<Integer>();

		//this.productionList.addElement(UnitsList.Priest);
		this.rallyPoint = new Checkpoint(this.x,this.y+this.getAttribut(Attributs.sizeY)/2);
	}

	

	
}

