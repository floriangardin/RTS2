package buildings;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;

import model.Game;
import model.Plateau;
import multiplaying.OutputModel.OutputBuilding;

public class BuildingMine extends Building{
	
	public int chargeTime;
	public float state;
	
	public BuildingMine(Plateau p,Game g,float x, float y){
		teamCapturing= 0;
		team = 0;

		p.addBuilding(this);
		this.x = x;
		this.y = y;
		this.p =p;
		this.g =g;
		this.id = p.g.idChar;
		p.g.idChar+=1;
		this.type = 0;
		this.selection_circle = this.p.images.selection_rectangle.getScaledCopy(4f);
		this.name= "Mine";
		this.maxLifePoints = p.g.players.get(team).data.millLifePoints;
		this.chargeTime = p.g.players.get(team).data.mineChargeTime;
		this.lifePoints = p.g.players.get(team).data.mineLifePoints;
		this.sizeX = p.g.players.get(team).data.mineSizeX;
		this.sizeY = p.g.players.get(team).data.mineSizeY;
		this.sight = p.g.players.get(team).data.mineSight;
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY/2f,sizeX,sizeY);
		if(team==1){
			this.image = this.p.images.buildingMineBlue;
		} else if(team==2){
			this.image = this.p.images.buildingMineRed;
		} else {
			this.image = this.p.images.buildingMineNeutral;
		}
		
	}
	
	public BuildingMine(OutputBuilding ocb, Plateau p){
		p.addBuilding(this);
		this.x = ocb.x;
		this.y = ocb.y;
		this.p =p;
		this.id = ocb.id;
		this.g =p.g;
		this.team=ocb.team;
		this.chargeTime = p.g.players.get(team).data.millChargeTime;
		this.selection_circle = this.p.images.selection_rectangle.getScaledCopy(4f);
		this.lifePoints = ocb.lifepoints;
		this.sizeX = ocb.sizeX; 
		this.name= "Mine";
		this.sizeY = ocb.sizeY;
		this.sight = ocb.sight;
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY/2f,sizeX,sizeY);
		if(team==1){
			this.image = this.p.images.buildingMineBlue;
		} else if(team==2){
			this.image = this.p.images.buildingMineRed;
		} else {
			this.image = this.p.images.buildingMineNeutral;
		}
	}
	
	public void action(){
		this.state+=0.1f;
		if(state >= chargeTime && team!=0){
			this.p.g.players.get(team).gold+=1;
			this.state = 0f;
		}
		
	}
	
	public Graphics draw(Graphics g){
		float r = collisionBox.getBoundingCircleRadius();
		g.drawImage(this.image, this.x-this.sizeX/2, this.y-this.sizeY, this.x+this.sizeX/2f, this.y+this.sizeY/2f, 0, 0, 291, 291);
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
