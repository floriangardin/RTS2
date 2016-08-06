package buildings;

import java.util.Vector;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import bullets.Fireball;
import data.Attributs;
import main.Main;
import model.Checkpoint;
import model.Game;
import units.Character;
import utils.Utils;

public class BuildingTower extends Building{


	public boolean canAttack;
	public Character target;

	public String animationBleu;
	public String animationRouge;

	public BuildingTower(float f, float h, int team){
		teamCapturing = 0;
		this.setTeam(team);
		this.name = "tower";
//		if(getTeam()==1){
//			this.image = this.p.g.images.get("buildingTowerBlue");
//		} else if(getTeam()==2){
//			this.image = this.p.g.images.get("buildingTowerRed");
//		} else {
//			this.image = this.p.g.images.get("buildingTowerNeutral");
//		}
		this.initialize(f, h);
		this.rallyPoint = new Checkpoint(this.x,this.y+this.getAttribut(Attributs.sizeY)/2);
		canAttack = false;
		this.animationBleu ="buildingTowerBlueAnimation";
		this.animationRouge = "buildingTowerRedAnimation";
	}

	public void action(){
		giveUpProcess();
		if(underAttackRemaining>0f){
			this.underAttackRemaining-=Main.increment;
		}
		else{
			this.underAttack = false;
		}
		if(!this.canAttack)
			this.setCharge(this.charge+Main.increment);
		if(this.charge>this.getAttribut(Attributs.chargeTime) && this.getGameTeam().id!=0){
			this.canAttack = true;
			this.charge = 0f;

		}
		if(getTeam()!=0)
			this.animation+=2f;
		if(this.animation>120f)
			this.animation = 1;
		
		if(this.target!=null && this.target.getTeam()==this.getTeam()){
			this.target = null;
		}
		if(canAttack){
			if(target==null || this.target.lifePoints<0f ||Utils.distance(this, this.target)>getAttribut(Attributs.sight)){
				Vector<Character> target= Game.g.plateau.getEnnemiesInSight(this);
				if(target.size()>0){
					this.target = target.get(0);
				}
			}

			//Launch a fireball on target
			if(target!=null && Utils.distance(this, this.target)<this.getAttribut(Attributs.sight)){
				new Fireball(this,this.getTarget().getX(),this.getTarget().getY(),this.getTarget().getX()-this.getX(),this.getTarget().getY()-this.getY(),this.getAttribut(Attributs.damage),-1);
				this.canAttack= false;
				this.charge = 0f;
			}
		}

	}

	public Character getTarget(){
		return this.target;
	}

	
	public void drawAnimation(Graphics g){
		float sizeX = getAttribut(Attributs.sizeX);
		float sizeY = getAttribut(Attributs.sizeY);
		if(getTeam()==1){
			g.drawImage(Game.g.images.get(this.animationBleu), this.x-(sizeX/1.8f)/3, this.y-sizeY,this.x+(sizeX/1.8f)/3, this.y-sizeY+sizeY*3f/8f, (int)(animation/30f)*100, 0, ((int)(animation/30f)+1)*100, 100);
		}
		if(getTeam()==2){
			g.drawImage(Game.g.images.get(this.animationRouge), this.x-(sizeX/1.8f)/3, this.y-sizeY,this.x+(sizeX/1.8f)/3, this.y-sizeY+sizeY*3f/8f, (int)(animation/30f)*100, 0, ((int)(animation/30f)+1)*100, 100);
		}
	}
}
