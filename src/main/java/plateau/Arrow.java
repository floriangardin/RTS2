package plateau;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;

import data.Attributs;
import events.EventAttackDamage;
import events.EventHandler;
import events.EventNames;
import main.Main;
import model.Game;
import ressources.Images;
import utils.ObjetsList;

public strictfp class Arrow extends Bullet{

	public float angle= 0f;
	public float life = 6f;
	
	public Arrow(Character owner,float vx,float vy,float damage, Plateau plateau){
		//MULTI 
		super(plateau);
		// Parameters
		this.size = 2f*Main.ratioSpace;
		this.setName(ObjetsList.Arrow);
		this.damage = damage;
		plateau.addBulletObjets(this);
		this.setLifePoints(1f);
		this.owner = owner.getId();
		this.team = plateau.getById(owner.getId()).getTeam();
		float Vmax = getAttribut(Attributs.maxVelocity)*Main.ratioSpace;
		this.setCollisionBox(new Circle(owner.getX(),owner.getY(),size));
		this.setXY(owner.getX(),owner.getY(), plateau);
		this.vx = vx;
		this.vy = vy;
		//Normalize speed : 
		float norm = this.vx*this.vx+this.vy*this.vy;
		norm  = (float)StrictMath.sqrt(norm)*Main.framerate;
		this.vx = Vmax*this.vx/norm;
		this.vy = Vmax*this.vy/norm;
		this.angle = (float) (StrictMath.atan(this.vy/(this.vx+0.00001f))*180/StrictMath.PI);
		if(this.vx<0)
			this.angle+=180;
		if(this.angle<0)
			this.angle+=360;
		
		this.soundLaunch = "arrow";
		EventHandler.addEvent(EventNames.ArrowLaunched, this, plateau);
	}

	public void collision(Character c, Plateau plateau){
		if(c.getTeam()!=this.team){
			// Attack if armor<damage and collision
			float damage = this.damage;
			if(!c.horse){
				damage = damage * this.getTeam().data.bonusBowFoot;
			}
			if(c.getAttribut(Attributs.armor)<=damage){
//				Game.g.getEvents().addEvent(new EventAttackDamage(c, (int)(damage-c.getAttribut(Attributs.armor))));
				c.setLifePoints(c.getLifePoints()+c.getAttribut(Attributs.armor)-damage, plateau);
			}
			
			this.setLifePoints(-1f, plateau);
		}

	}

	@Override
	public void collision(Objet c, Plateau plateau){
		this.setLifePoints(-1f);
	}
	
	
	public void action(Plateau plateau){
		//MULTI 
		this.life  -= Main.increment;
		if(life<0f){
			this.setLifePoints(-1f);
		}
		
		this.setXY(this.getX()+this.vx, this.getY()+this.vy, plateau);
		if(this.getX()>plateau.maxX || this.getX()<0 || this.getY()>plateau.maxY||this.getY()<0){
			this.setLifePoints(-1f, plateau);
		}
	}


	// For MenuArrow
	public Arrow(Plateau plateau){
		super(plateau);
	}

}
