package buildings;

import java.util.Vector;

import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;

import model.Checkpoint;
import model.Game;
import model.Map;
import model.Plateau;
import units.UnitsList;

public class BuildingAcademy extends BuildingProduction {

	
	public BuildingAcademy(Plateau plateau, Game g, float f, float h, int team) {
		teamCapturing= 0;
		//this.animation=-1f;
		this.p = plateau ;
		this.g = g;
		this.setTeam(team);
		maxLifePoints = this.getGameTeam().data.academyLifePoints;
		this.sizeX = this.getGameTeam().data.academySizeX; 
		this.sizeY = this.getGameTeam().data.academySizeY;
		this.sight = this.getGameTeam().data.academySight;
		this.initialize(f, h);
		this.name = "academy";
		this.selection_circle = this.p.g.images.selection_rectangle.getScaledCopy(4f);
		type= 4;
		this.soundSelection = new Vector<Sound>();
		this.soundSelection.addElement(this.g.sounds.academySound);
		this.g = g;
		if(this.getTeam()==1){
			this.image = this.p.g.images.buildingAcademyBlue;
		} else if(this.getTeam()==2){
			this.image = this.p.g.images.buildingAcademyRed;
		} else {
			this.image = this.p.g.images.buildingAcademyNeutral;
		}
		// List of potential production (Spearman
		this.queue = new Vector<Integer>();
		this.productionList = new Vector<UnitsList>();
		this.productionList.addElement(UnitsList.Inquisitor);
		this.rallyPoint = new Checkpoint(p,this.x,this.y+this.sizeY/2);
	}

	
}
