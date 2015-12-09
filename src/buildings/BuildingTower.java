package buildings;

import java.util.Vector;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

import bullets.Fireball;
import main.Main;
import model.Checkpoint;
import model.Game;
import model.Map;
import model.Plateau;
import model.Utils;
import units.Character;

public class BuildingTower extends Building{


	public float chargeTime;
	public boolean canAttack;
	public Character target;
	public float damage;

	public Image animationBleu;
	public Image animationRouge;

	public BuildingTower(Plateau p,Game g,float f, float h){
		teamCapturing = 0;
		this.p =p;
		this.g =g;
		this.setTeam(0);
		this.damage = 20f;
		this.maxLifePoints = 20f;
		this.sizeX = 2f*Map.stepGrid;
		this.sizeY = 2f*Map.stepGrid;
		this.name = "tower";
		this.chargeTime = 5f;
		this.selection_circle = this.p.g.images.selection_rectangle.getScaledCopy(4f);
		this.soundSelection = new Vector<Sound>();
		this.soundSelection.addElement(this.g.sounds.towerSound);
		if(getTeam()==1){
			this.image = this.p.g.images.buildingTowerBlue;
		} else if(getTeam()==2){
			this.image = this.p.g.images.buildingTowerRed;
		} else {
			this.image = this.p.g.images.buildingTowerNeutral;
		}
		this.sight = 550f;
		this.rallyPoint = new Checkpoint(p,this.x,this.y+this.sizeY/2);
		this.initialize(f, h);
		canAttack = false;
		this.animationBleu = this.p.g.images.buildingTowerBlueAnimation;
		this.animationRouge = this.p.g.images.buildingTowerRedAnimation;
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
		if(this.charge>this.chargeTime && this.getGameTeam().id!=0){
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
			if(target==null || this.target.lifePoints<0f ||Utils.distance(this, this.target)>sight){
				Vector<Character> target= this.p.getEnnemiesInSight(this);
				if(target.size()>0){
					this.target = target.get(0);
				}
			}

			//Launch a fireball on target
			if(target!=null && Utils.distance(this, this.target)<this.sight){
				new Fireball(this.p,this,this.getTarget().getX(),this.getTarget().getY(),this.getTarget().getX()-this.getX(),this.getTarget().getY()-this.getY(),this.damage,-1);
				this.canAttack= false;
				this.charge = 0f;
			}
		}

	}

	public Character getTarget(){
		return this.target;
	}

	
	public void drawAnimation(Graphics g){
		if(getTeam()==1){
			g.drawImage(this.animationBleu, this.x-(this.sizeX/1.8f)/3, this.y-this.sizeY,this.x+(this.sizeX/1.8f)/3, this.y-this.sizeY+this.sizeY*3f/8f, (int)(animation/30f)*100, 0, ((int)(animation/30f)+1)*100, 100);
		}
		if(getTeam()==2){
			g.drawImage(this.animationRouge, this.x-(this.sizeX/1.8f)/3, this.y-this.sizeY,this.x+(this.sizeX/1.8f)/3, this.y-this.sizeY+this.sizeY*3f/8f, (int)(animation/30f)*100, 0, ((int)(animation/30f)+1)*100, 100);
		}
	}
}
