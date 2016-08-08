package buildings;

import java.util.Vector;

import data.Attributs;
import model.Checkpoint;
import utils.ObjetsList;
import utils.ObjetsList;

public class BuildingBarrack extends Building{
	
	public static final int SPEARMAN = 0;
	public static final int CROSSBOWMAN = 1;

	public BuildingBarrack(float f, float h, int team) {
		
		super(ObjetsList.Barracks,f ,h);
		teamCapturing= 0;
		this.setTeam(team);
		
		type= 3;
		this.initialize(f, h);
		// List of potential production (Spearman)
		
		
	}

	

}
