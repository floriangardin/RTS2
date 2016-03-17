package buildings;

import java.util.Vector;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

import model.Checkpoint;
import model.Game;
import model.Map;
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
		this.sizeX = this.getGameTeam().data.barrackSizeX; 
		this.sizeY = this.getGameTeam().data.barrackSizeY;
		this.sight = this.getGameTeam().data.barrackSight;
		this.name = "barrack";
		this.soundSelection = new Vector<Sound>();
		this.soundSelection.addElement(this.g.sounds.barrackSound);
		this.selection_circle = this.p.g.images.get("rectSelect").getScaledCopy(4f);
		type= 3;
		this.initialize(f, h);
		this.g = g;
		if(getTeam()==1){
			this.image = this.p.g.images.get("buildingBarrackBlue");
		} else if(getTeam()==2){
			this.image = this.p.g.images.get("buildingBarrackRed");
		} else {
			this.image = this.p.g.images.get("buildingBarrackNeutral");
		}
		// List of potential production (Spearman)
		this.queue = new Vector<Integer>();
		this.productionList = new Vector<UnitsList>();
		this.productionList.addElement(UnitsList.Spearman);
		this.productionList.addElement(UnitsList.Crossbowman);
		this.rallyPoint = new Checkpoint(p,this.x,this.y+this.sizeY/2);
	}

	

}
