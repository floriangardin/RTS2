package buildings;

import java.util.Vector;

import model.Checkpoint;
import model.Data;
import model.Game;
import model.Plateau;
import units.UnitsList;

public class BuildingBarrack extends BuildingProduction{
	
	public static final int SPEARMAN = 0;
	public static final int CROSSBOWMAN = 1;

	public BuildingBarrack(Plateau plateau, Game g, float f, float h, int team) {
		teamCapturing= 0;
		//this.animation=-1f;
		this.p = plateau ;
		this.g = g;
		this.setTeam(team);
		maxLifePoints = this.getGameTeam().data.barrackLifePoints;
		//maxLifePoints = 10f;
		this.sizeX = Data.barrackSizeX; 
		this.sizeY = Data.barrackSizeY;
		this.sight = this.getGameTeam().data.barrackSight;
		this.name = "barrack";
		this.selection_circle = this.p.g.images.get("rectSelectsizeBuilding");
		type= 3;
		this.initialize(f, h);
		this.g = g;
		// List of potential production (Spearman)
		this.queue = new Vector<Integer>();
		this.productionList = new Vector<UnitsList>();
		this.productionList.addElement(UnitsList.Spearman);
		this.productionList.addElement(UnitsList.Crossbowman);
		this.rallyPoint = new Checkpoint(p,this.x,this.y+this.sizeY/2);
	}

	

}
