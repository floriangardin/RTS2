package plateau;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;

import data.Attributs;
import events.EventHandler;
import events.EventNames;
import main.Main;
import model.Game;
import ressources.Images;
import spells.BurningArea;
import utils.ObjetsList;
import utils.Utils;

public class Fireball extends Bullet {

	public float altitude;
	public int animation;
	public float angle;
	public boolean explosion= false;

	public Fireball( Objet owner,float targetX,float targetY,float vx,float vy,float damage, Plateau plateau){
		//MULTI 
		// Parameters
		super(plateau);
		this.altitude = 0f;
		this.name = ObjetsList.Fireball;
		//
		this.size = 10f*Main.ratioSpace;
		plateau.addBulletObjets(this);
		this.damage = damage;
		this.animation = 0;
		this.lifePoints = 30f;
		this.owner = owner.id;
		this.team = owner.getTeam();
		this.areaEffect = getAttribut(Attributs.size)*Main.ratioSpace;
		float Vmax = getAttribut(Attributs.maxVelocity)*Main.ratioSpace;
		float size = 10f*Main.ratioSpace;
		this.setTarget(new Checkpoint(targetX,targetY, plateau), plateau);
		this.collisionBox = new Circle(owner.getX(),owner.getY(),size);
		this.setXY(owner.getX(),owner.getY()-altitude, plateau);
		this.vx = vx;
		this.vy = vy+altitude;
		//Normalize speed : 
		float norm = this.vx*this.vx+this.vy*this.vy;
		norm  = (float)Math.sqrt(norm)*Main.framerate;
		this.vx = Vmax*this.vx/norm;
		this.vy = Vmax*this.vy/norm;
		this.angle = (float) (Math.atan(vy/(vx+0.00001f))*180/Math.PI);
		if(this.vx<0)
			this.angle+=180;
		if(this.angle<0)
			this.angle+=360;
		this.soundLaunch = "fireball";
		EventHandler.addEvent(EventNames.FireBallLaunched, this, plateau);
	}

	public Fireball(Plateau plateau){
		super(plateau);
	}
	public void action(Plateau plateau){

		if(explosion){
			this.setLifePoints(lifePoints-10f*Main.increment, plateau);
			return;
		}
		this.setXY(this.getX()+this.vx, this.getY()+this.vy, plateau);
		this.animation+=1;
		if(this.animation>=9)
			this.animation = 0;


		if(Utils.distance(this, this.getTarget(plateau))<this.size){
			this.explode(plateau);
		}
	}
	public void explode(Plateau plateau){
		for(Character c : plateau.getCharacters()){
			if(c.getTeam().id!=this.owner){
				boolean isIntersected = ((c.getX()-this.getX())*(c.getX()-this.getX())+
						(c.getY()-this.getY())*(c.getY()-this.getY()))< 
						(0.5f*c.collisionBox.getWidth()+this.areaEffect)*
						(0.5f*c.collisionBox.getWidth()+this.areaEffect);
				if(isIntersected){					
					this.boom(c, plateau);
				}
			}
		}

		new BurningArea(this.owner, new Checkpoint(x,y, plateau), this.areaEffect, plateau);
		this.explosion = true;
	}
	public void boom(Character c, Plateau plateau){
		float damage = this.damage;
		if(c.getAttributString(Attributs.weapon)!= null && c.getAttributString(Attributs.weapon) == "bow")
			damage = damage * this.getTeam().data.bonusBowFoot;
		c.setLifePoints(c.lifePoints-damage, plateau);

	}

	public void boom(Building c){

	}

	



	@Override
	public void collision(Character c, Plateau plateau) {
		// TODO Auto-generated method stub

	}

}
