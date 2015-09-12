package buildings;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;

import model.Checkpoint;
import model.Game;
import model.Plateau;
import multiplaying.OutputModel.OutputBuilding;
import units.UnitsList;

public class BuildingStable extends BuildingProduction{


	public BuildingStable(Plateau plateau, Game g, float f, float h) {
		teamCapturing= 0;
		this.animation=-1f;
		team = 0;
		this.p = plateau ;
		maxLifePoints = p.g.players.get(team).data.stableLifePoints;
		this.sizeX = this.p.g.players.get(team).data.stableSizeX; 
		this.sizeY = this.p.g.players.get(team).data.stableSizeY;
		this.sight = this.p.g.players.get(team).data.stableSight;
		this.name = "stable";
		p.addBuilding(this);
		this.selection_circle = this.p.images.selection_rectangle.getScaledCopy(4f);
		type= 2;
		this.lifePoints = this.maxLifePoints;
		this.g = g;
		this.id = p.g.idChar;
		p.g.idChar+=1;
		this.x = f;
		this.y = h;
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY/2f,sizeX,sizeY);
		if(team==1){
			this.image = this.p.images.buildingStableBlue;
		} else if(team==2){
			this.image = this.p.images.buildingStableRed;
		} else {
			this.image = this.p.images.buildingStableNeutral;
		}
		// List of potential production (Spearman
		this.queue = new Vector<Integer>();
		this.productionTime = new Vector<Float>();
		this.productionList = new Vector<UnitsList>();
		this.productionList.addElement(UnitsList.Knight);
		this.productionTime.addElement(UnitsList.Knight.time);
		this.productionList.addElement(UnitsList.Priest);
		this.productionTime.addElement(UnitsList.Priest.time);
		this.rallyPoint = new Checkpoint(p,this.x,this.y+this.sizeY/2);
		this.updateImage();
	}

	

	public void drawAnimation(Graphics g){
		if(animation>=0f){
			this.drawAnimation(g);
			g.drawImage(this.p.images.fountain, this.x-6f/18f*sizeX-48f, this.y-128f,this.x-6f/18f*sizeX+48f, this.y-32f, (int)(animation/30f)*96, 0, ((int)(animation/30f)+1)*96, 96);
		}
	}

}

