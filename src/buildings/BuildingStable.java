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
		this.productionTime.addElement(this.p.g.players.get(team).data.knightProdTime);
		this.productionList.addElement(UnitsList.Priest);
		this.productionTime.addElement(this.p.g.players.get(team).data.priestProdTime);
		this.rallyPoint = new Checkpoint(p,this.x,this.y+this.sizeY/2);
	}

	public BuildingStable(OutputBuilding ocb, Plateau p){
		team = ocb.team;
		type= 3;
		maxLifePoints = ocb.maxlifepoints;
		this.p = p;
		p.addBuilding(this);
		this.lifePoints = this.maxLifePoints;
		this.name = "Stable";
		this.g = p.g;
		this.x = ocb.x;
		this.y = ocb.y;
		this.id = ocb.id;
		this.sizeX = this.p.g.players.get(team).data.stableSizeX; 
		this.sizeY = this.p.g.players.get(team).data.stableSizeY;
		this.sight = this.p.g.players.get(team).data.stableSight;
		this.selection_circle = this.p.images.selection_rectangle.getScaledCopy(4f);
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY,sizeX,sizeY);
		if(ocb.team==1){
			this.image = this.p.images.buildingStableBlue;
		} else if(ocb.team==2){
			this.image = this.p.images.buildingStableRed;
		} else {
			this.image = this.p.images.buildingStableNeutral;
		}
		
		// List of potential production (Spearman
		this.queue = new Vector<Integer>();
		this.productionTime = new Vector<Float>();
		this.productionList = new Vector<UnitsList>();
		this.productionList.addElement(UnitsList.Knight);
		this.productionTime.addElement(this.p.g.players.get(team).data.knightProdTime);
		this.productionList.addElement(UnitsList.Priest);
		this.productionTime.addElement(this.p.g.players.get(team).data.priestProdTime);
	}

	
	public Graphics draw(Graphics g){
		float r = collisionBox.getBoundingCircleRadius();
		g.drawImage(this.image, this.x-this.sizeX/2, this.y-this.sizeY, this.x+this.sizeX/2f, this.y+this.sizeY/2f, 0, 0, 291, 291);
		if(animation>=0f){
			g.drawImage(this.p.images.fountain, this.x-6f/18f*sizeX-48f, this.y-128f,this.x-6f/18f*sizeX+48f, this.y-32f, (int)(animation/30f)*96, 0, ((int)(animation/30f)+1)*96, 96);
		}
		//g.drawImage(this.image,this.getX()-sizeX/2f,this.getY()-sizeY,this.getX()+sizeX/2f,this.getY()+1f*sizeY/6f,0f,0f,this.image.getWidth(),this.image.getHeight());
		if(this.lifePoints<this.maxLifePoints){
			// Lifepoints
			g.setColor(Color.red);
			g.draw(new Line(this.getX()-r,this.getY()-r-30f,this.getX()+r,this.getY()-r-30f));
			float x = this.lifePoints*2f*r/this.maxLifePoints;
			g.setColor(Color.green);
			g.draw(new Line(this.getX()-r,this.getY()-r-30f,this.getX()-r+x,this.getY()-r-30f));

		}
		// Construction points
		if(this.constructionPoints<this.maxLifePoints && this.constructionPoints>0){
			g.setColor(Color.white);
			g.draw(new Line(this.getX()-r,this.getY()-r-50f,this.getX()+r,this.getY()-r-50f));
			float x = this.constructionPoints*2f*r/this.maxLifePoints;
			g.setColor(Color.blue);
			g.draw(new Line(this.getX()-r,this.getY()-r-50f,this.getX()-r+x,this.getY()-r-50f));
		}
		return g;
	}

}

