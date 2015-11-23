package buildings;

import java.util.Vector;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

import main.Main;
import model.Checkpoint;
import model.Game;
import model.Plateau;
import technologies.Technologie;

public class BuildingMill extends BuildingTech{
	
	public int chargeTime;
	public float state;
	public Image millarms;
	public int bonusProd;
	
	public BuildingMill(Plateau p,Game g,float x, float y){
		


		this.x = x;
		this.y = y;
		
		this.p =p;
		this.setTeam(0);
		this.g =g;
		this.id = p.g.idChar;
		p.g.idChar+=1;
		this.type = 1;
		this.selection_circle = this.p.g.images.selection_rectangle.getScaledCopy(4f);
		this.name= "mill";
		
		this.maxLifePoints = getGameTeam().data.millLifePoints;
		this.chargeTime = getGameTeam().data.millChargeTime;
		this.lifePoints = getGameTeam().data.millLifePoints;
		this.sizeX = getGameTeam().data.millSizeX; 
		this.sizeY = getGameTeam().data.millSizeY;
		p.addBuilding(this);
		this.sight = p.players.get(getTeam()).data.millSight;
		this.collisionBox= new Rectangle(x-sizeX/2f,y-sizeY/2f,sizeX,sizeY);
		this.selectionBox = this.collisionBox;
		if(getTeam()==1){
			this.image = this.p.g.images.buildingMillBlue;
		} else if(getTeam()==2){
			this.image = this.p.g.images.buildingMillRed;
		} else {
			this.image = this.p.g.images.buildingMillNeutral;
		}
		this.rallyPoint = new Checkpoint(p,this.x,this.y+this.sizeY/2);
		this.productionList = new Vector<Technologie>();
		this.updateProductionList();
		this.updateImage();
	}

	
	public void action(){
		this.state+=0.1f*Game.ratio;
		if(getTeam()!=0)
			this.animation+=2f;
		if(animation>120f)
			
		
		if(state >= chargeTime && getTeam()!=0){
			this.getGameTeam().food+=2+this.getGameTeam().data.bonusFood;
			state = 0;
		}
		
		
		//Do the action of prod tech
		//Product, increase state of the queue
		if(this.queue!=null){
			if(!this.isProducing){
				this.isProducing = true;
			}
			this.animation+=2f;
			if(animation>120f)
				
			this.charge+=Main.increment;
			if(this.charge>=this.queue.tech.prodTime){
				this.techTerminate(this.queue);

			}
		}
		else if(this.isProducing){
			this.isProducing = false;
			
		}

	}


	
	public void drawAnimation(Graphics g){
		if(getTeam()!=0){
			g.drawImage(this.p.g.images.smoke, this.x+1f/18f*sizeX-50f, this.y-159f,this.x+1f/18f*sizeX+36f, this.y-101f, (int)(animation/30f)*64, 64, ((int)(animation/30f)+1)*64, 128);
		}
	}
}
