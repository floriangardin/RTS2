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

public class BuildingAcademy extends BuildingProduction {

	
	public BuildingAcademy(Plateau plateau, Game g, float f, float h) {
		teamCapturing= 0;
		this.animation=-1f;
		team = 0;
		this.p = plateau ;
		maxLifePoints = p.g.players.get(team).data.academyLifePoints;
		this.sizeX = this.p.g.players.get(team).data.academySizeX; 
		this.sizeY = this.p.g.players.get(team).data.academySizeY;
		this.sight = this.p.g.players.get(team).data.academySight;
		this.name = "academy";
		p.addBuilding(this);
		this.selection_circle = this.p.images.selection_rectangle.getScaledCopy(4f);
		type= 4;
		this.lifePoints = this.maxLifePoints;
		this.g = g;
		this.id = p.g.idChar;
		p.g.idChar+=1;
		this.x = f;
		this.y = h;
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY/2f,sizeX,sizeY);
		if(team==1){
			this.image = this.p.images.buildingAcademyBlue;
		} else if(team==2){
			this.image = this.p.images.buildingAcademyRed;
		} else {
			this.image = this.p.images.buildingAcademyNeutral;
		}
		// List of potential production (Spearman
		this.queue = new Vector<Integer>();
		this.productionTime = new Vector<Float>();
		this.productionList = new Vector<UnitsList>();
		this.productionList.addElement(UnitsList.Inquisitor);
		this.productionTime.addElement(this.p.g.players.get(team).data.spearmanProdTime);
		this.rallyPoint = new Checkpoint(p,this.x,this.y+this.sizeY/2);
	}

	public BuildingAcademy(OutputBuilding ocb, Plateau p){
		team = ocb.team;
		type= 4;
		maxLifePoints = ocb.maxlifepoints;
		this.p = p;
		p.addBuilding(this);
		this.lifePoints = this.maxLifePoints;
		this.g = p.g;
		this.x = ocb.x;
		this.y = ocb.y;
		this.id = ocb.id;
		this.sizeX = this.p.g.players.get(team).data.academySizeX; 
		this.name = "Academy";
		this.sizeY = this.p.g.players.get(team).data.academySizeY;
		this.sight = this.p.g.players.get(team).data.academySight;
		this.selection_circle = this.p.images.selection_rectangle.getScaledCopy(4f);
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY,sizeX,sizeY);
		if(ocb.team==1){
			this.image = this.p.images.buildingAcademyBlue;
		} else if(ocb.team==2){
			this.image = this.p.images.buildingAcademyRed;
		} else {
			this.image = this.p.images.buildingAcademyNeutral;
		}
		
		// List of potential production (Spearman
		this.queue = new Vector<Integer>();
		this.productionTime = new Vector<Float>();
		this.productionList = new Vector<UnitsList>();
		this.productionList.addElement(UnitsList.Inquisitor);
		this.productionTime.addElement(this.p.g.players.get(team).data.spearmanProdTime);

	}


	public Graphics draw(Graphics g){
		float r = collisionBox.getBoundingCircleRadius();
		g.drawImage(this.image, this.x-this.sizeX/2, this.y-this.sizeY, this.x+this.sizeX/2f, this.y+this.sizeY/2f, 0, 0, 291, 291);
//		if(animation>=0f){
//			g.drawImage(this.p.images.fire, this.x+5f/18f*sizeX, this.y-24f,this.x+5f/18f*sizeX+32f, this.y+24f, (int)(animation/30f)*32, 96, ((int)(animation/30f)+1)*32, 144);
//			g.drawImage(this.p.images.fire, this.x-9f/18f*sizeX-2f, this.y-84f,this.x-9f/18f*sizeX+32f-2f, this.y-36f, (int)(animation/30f)*32, 96, ((int)(animation/30f)+1)*32, 144);
//		}
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
		if(this.constructionPoints<this.maxLifePoints && this.constructionPoints>0f){
			g.setColor(Color.white);
			g.draw(new Line(this.getX()-r,this.getY()-r-50f,this.getX()+r,this.getY()-r-50f));
			float x = this.constructionPoints*2f*r/this.maxLifePoints;
			g.setColor(Color.blue);
			g.draw(new Line(this.getX()-r,this.getY()-r-50f,this.getX()-r+x,this.getY()-r-50f));
		}
		return g;
	}	
	
}
