package buildings;

import java.util.Vector;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import model.Checkpoint;
import model.Game;
import model.Plateau;
import units.UnitsList;

public class BuildingBarrack extends BuildingProduction{


	public BuildingBarrack(Plateau plateau, Game g, float f, float h) {
		teamCapturing= 0;
		//this.animation=-1f;
		this.p = plateau ;
		this.setTeam(0);
		maxLifePoints = this.getGameTeam().data.barrackLifePoints;
		//maxLifePoints = 10f;
		this.sizeX = this.getGameTeam().data.barrackSizeX; 
		this.sizeY = this.getGameTeam().data.barrackSizeY;
		this.sight = this.getGameTeam().data.barrackSight;
		this.name = "barrack";
		this.selection_circle = this.p.g.images.selection_rectangle.getScaledCopy(4f);
		type= 3;
		this.lifePoints = this.maxLifePoints;
		this.g = g;
		this.id = p.g.idChar;
		p.g.idChar+=1;
		this.x = f;
		this.y = h;
		p.addBuilding(this);
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY/2f,sizeX,sizeY);
		this.selectionBox = this.collisionBox;
		if(getTeam()==1){
			this.image = this.p.g.images.buildingBarrackBlue;
		} else if(getTeam()==2){
			this.image = this.p.g.images.buildingBarrackRed;
		} else {
			this.image = this.p.g.images.buildingBarrackNeutral;
		}
		// List of potential production (Spearman)
		this.queue = new Vector<Integer>();
		this.productionList = new Vector<UnitsList>();
		this.productionList.addElement(UnitsList.Spearman);
		this.productionList.addElement(UnitsList.Crossbowman);
		this.rallyPoint = new Checkpoint(p,this.x,this.y+this.sizeY/2);
		this.updateImage();
	}

	

}
