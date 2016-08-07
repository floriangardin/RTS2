package buildings;

import java.util.Vector;

import data.Attributs;
import model.Checkpoint;
import utils.ObjetsList;
import utils.ObjetsList;

public class BuildingAcademy extends Building {

	
	public BuildingAcademy(float f, float h, int team) {
		super(ObjetsList.Academy,f ,h);
		teamCapturing= 0;
		//this.animation=-1f;
		this.setTeam(team);
		this.name = ObjetsList.Academy.name().toLowerCase();
		this.initialize(f, h);
		type= 4;
		// List of potential production (Spearman
		this.queue = new Vector<Integer>();
		
		this.rallyPoint = new Checkpoint(this.x,this.y+this.getAttribut(Attributs.sizeY)/2);
	}

	
}
