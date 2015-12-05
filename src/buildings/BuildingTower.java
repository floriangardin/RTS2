package buildings;

import java.util.Vector;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

import bullets.Fireball;
import main.Main;
import model.Checkpoint;
import model.Game;
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

	public BuildingTower(Plateau p,Game g,float x, float y){
		p.addBuilding(this);
		this.constructionPoints = 0f;
		teamCapturing = 0;
		this.x = x;
		this.y = y;
		this.p =p;
		this.g =g;
		this.setTeam(0);
		this.damage = 20f;
		this.maxLifePoints = 20f;
		this.name = "tower";
		this.lifePoints = this.maxLifePoints;
		this.chargeTime = 5f;
		this.sizeX = 220f; 
		this.sizeY = 220f;
		this.collisionBox= new Rectangle(0,0,sizeX,sizeY);
		this.selection_circle = this.p.g.images.selection_rectangle.getScaledCopy(4f);
		this.selectionBox = this.collisionBox;
		this.setXY(x, y);
		if(getTeam()==1){
			this.image = this.p.g.images.buildingTowerBlue;
		} else if(getTeam()==2){
			this.image = this.p.g.images.buildingTowerRed;
		} else {
			this.image = this.p.g.images.buildingTowerNeutral;
		}
		this.sight = 550f;
		this.rallyPoint = new Checkpoint(p,this.x,this.y+this.sizeY/2);
		this.updateImage();
		canAttack = false;
		this.animationBleu = this.p.g.images.buildingTowerBlueAnimation;
		this.animationRouge = this.p.g.images.buildingTowerRedAnimation;
	}

	public void action(){
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
		
		if(canAttack){
			if(target==null || this.target.lifePoints<0f){
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
			//g.drawImage(this.image, this.x-this.sizeX/1.8f, this.y-this.sizeY, this.x+this.sizeX/1.8f, this.y+this.sizeY/2f, 0, 0, this.image.getWidth(), this.image.getHeight());
			
			g.drawImage(this.animationBleu, this.x-(this.sizeX/1.8f)/3, this.y-this.sizeY,this.x+(this.sizeX/1.8f)/3, this.y-this.sizeY+this.sizeY*3f/8f, (int)(animation/30f)*100, 0, ((int)(animation/30f)+1)*100, 100);
		}
	}
}
