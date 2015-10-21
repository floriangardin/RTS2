package buildings;

import java.util.Vector;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;

import model.Checkpoint;
import model.Game;
import model.Plateau;
import multiplaying.OutputModel.OutputBuilding;
import units.UnitsList;

public class BuildingBarrack extends BuildingProduction{


	public BuildingBarrack(Plateau plateau, Game g, float f, float h) {
		teamCapturing= 0;
		//this.animation=-1f;
		team = 0;
		this.p = plateau ;
		maxLifePoints = p.g.players.get(team).data.barrackLifePoints;
		this.sizeX = this.p.g.players.get(team).data.barrackSizeX; 
		this.sizeY = this.p.g.players.get(team).data.barrackSizeY;
		this.sight = this.p.g.players.get(team).data.barrackSight;
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
		if(team==1){
			this.image = this.p.g.images.buildingBarrackBlue;
		} else if(team==2){
			this.image = this.p.g.images.buildingBarrackRed;
		} else {
			this.image = this.p.g.images.buildingBarrackNeutral;
		}
		// List of potential production (Spearman
		//TODO Merge production time and production list in vector of UnitsList
		this.queue = new Vector<Integer>();
		this.productionList = new Vector<UnitsList>();
		this.productionList.addElement(UnitsList.Spearman);
		this.productionList.addElement(UnitsList.Crossbowman);
		this.rallyPoint = new Checkpoint(p,this.x,this.y+this.sizeY/2);
		this.updateImage();
	}

	public BuildingBarrack(OutputBuilding ocb, Plateau p){
		team = ocb.team;
		new BuildingBarrack(p,p.g,ocb.x,ocb.y);
		this.id = ocb.id;
	}

	public void drawAnimation(Graphics g){
		if(animation>=0f){
			g.drawImage(this.p.g.images.fire, this.x+5f/18f*sizeX, this.y-24f,this.x+5f/18f*sizeX+32f, this.y+24f, (int)(animation/30f)*32, 96, ((int)(animation/30f)+1)*32, 144);
			g.drawImage(this.p.g.images.fire, this.x-9f/18f*sizeX-2f, this.y-84f,this.x-9f/18f*sizeX+32f-2f, this.y-36f, (int)(animation/30f)*32, 96, ((int)(animation/30f)+1)*32, 144);
		}
	}

}
