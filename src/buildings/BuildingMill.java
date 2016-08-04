package buildings;

import java.util.Vector;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import display.DisplayRessources;
import main.Main;
import model.Data;
import model.Game;
import model.Plateau;
import technologies.Technologie;

public class BuildingMill extends BuildingTech{
	
	public int chargeTime;
	
	public Image millarms;
	public int bonusProd;
	
	public BuildingMill(Plateau p,Game g,float f, float h, int team){
		
		this.p =p;
		this.setTeam(team);
		this.type = 1;
		this.selection_circle = Game.g.images.get("rectSelectsizeBuilding");
		this.name= "mill";
		this.printName = "Ferme";
		this.maxLifePoints = getGameTeam().data.millLifePoints;
		this.chargeTime = getGameTeam().data.millChargeTime;
		this.lifePoints = getGameTeam().data.millLifePoints;
		this.sizeX = Data.millSizeX; 
		this.sizeY = Data.millSizeY;
		this.sight = Game.g.players.get(getTeam()).data.millSight;
		this.initialize(f, h);
		this.productionList = new Vector<Technologie>();
		this.updateProductionList();
	}

	
	public void action(){
		giveUpProcess();
		if(underAttackRemaining>0f){
			this.underAttackRemaining-=Main.increment;
		}
		else{
			this.underAttack = false;
		}
		this.state+=Main.increment;
		if(getTeam()!=0)
			this.animation+=2f;
		if(animation>120f)
			animation = 0;
		
		if(state >= chargeTime && getTeam()!=0){
			this.getGameTeam().food+=7+this.getGameTeam().data.bonusFood;
			if(this.gameteam==Game.g.currentPlayer.getGameTeam()){
				Game.g.addDisplayRessources(new DisplayRessources(7+this.getGameTeam().data.bonusFood, "food", this.x, this.y));
			}
			state = 0;
		}
		
		
		//Do the action of prod tech
		//Product, increase state of the queue
		if(this.queue!=null){
			if(!this.isProducing){
				this.isProducing = true;
			}
			this.animation=(int) ((this.animation+2f)%120);
				
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
			g.drawImage(Game.g.images.get("smoke"), this.x+1f/18f*sizeX-50f, this.y-159f,this.x+1f/18f*sizeX+36f, this.y-101f, (int)(animation/30f)*64, 64, ((int)(animation/30f)+1)*64, 128);
		}
	}
}
