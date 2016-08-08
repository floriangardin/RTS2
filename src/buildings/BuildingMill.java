package buildings;

import java.util.Vector;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import data.Attributs;
import display.DisplayRessources;
import main.Main;
import model.Game;
import technologies.Technologie;
import utils.ObjetsList;

public class BuildingMill extends Building{
	
	public float chargeTime;
	
	public int bonusProd;
	
	public BuildingMill(float f, float h, int team){
		
		super(ObjetsList.Mill,f,h);
		this.setTeam(team);
		this.type = 1;
		this.chargeTime = getAttribut(Attributs.maxChargetime);
		this.lifePoints = getAttribut(Attributs.maxLifepoints);
		this.initialize(f, h);
	
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
			if(this.team==Game.g.currentPlayer.getGameTeam().id){
				Game.g.addDisplayRessources(new DisplayRessources(7+this.getGameTeam().data.bonusFood, "food", this.x, this.y));
			}
			state = 0;
		}
		
		
		//Do the action of prod tech
		//Product, increase state of the queue
		if(this.queueTechnology!=null){
			if(!this.isProducing){
				this.isProducing = true;
			}
			this.animation=(int) ((this.animation+2f)%120);
				
			this.charge+=Main.increment;
			if(this.charge>=this.queueTechnology.tech.prodTime){
				this.techTerminate(this.queueTechnology);

			}
		}
		else if(this.isProducing){
			this.isProducing = false;
			
		}

	}


	
	public void drawAnimation(Graphics g){
		if(getTeam()!=0){
			g.drawImage(Game.g.images.get("smoke"), this.x+1f/18f*getAttribut(Attributs.sizeX)-50f, this.y-159f,this.x+1f/18f*getAttribut(Attributs.sizeX)+36f, this.y-101f, (int)(animation/30f)*64, 64, ((int)(animation/30f)+1)*64, 128);
		}
	}
}
