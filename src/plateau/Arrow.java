package plateau;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;

import data.Attributs;
import events.EventAttackDamage;
import main.Main;
import model.Game;
import ressources.Images;
import utils.ObjetsList;

public class Arrow extends Bullet{

	protected float angle= 0f;
	public float life = 6f;
	public Arrow(Character owner,float vx,float vy,float damage, Plateau plateau){
		//MULTI 
		super(plateau);
		// Parameters
		this.size = 2f*Main.ratioSpace;
		this.name = ObjetsList.Arrow;
		this.damage = damage;
		plateau.addBulletObjets(this);
		this.lifePoints = 1f;
		this.owner = owner.id;
		this.team = plateau.getById(owner.id).getTeam();
		float Vmax = getAttribut(Attributs.maxVelocity)*Main.ratioSpace;
		this.collisionBox = new Circle(owner.getX(),owner.getY(),size);
		this.setXY(owner.getX(),owner.getY(), plateau);
		this.vx = vx;
		this.vy = vy;
		//Normalize speed : 
		float norm = this.vx*this.vx+this.vy*this.vy;
		norm  = (float)Math.sqrt(norm)*Main.framerate;
		this.vx = Vmax*this.vx/norm;
		this.vy = Vmax*this.vy/norm;
		this.angle = (float) (Math.atan(this.vy/(this.vx+0.00001f))*180/Math.PI);
		if(this.vx<0)
			this.angle+=180;
		if(this.angle<0)
			this.angle+=360;
		
		this.soundLaunch = "arrow";
//		Game.g.triggerEvent(EventNames.ArrowLaunched, this);
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
				c.setLifePoints(c.lifePoints+c.getAttribut(Attributs.armor)-damage);
			}
			c.isAttacked();
			this.setLifePoints(-1f);
		}

	}

	public void collision(Building c, Plateau plateau){
		this.lifePoints = -1f;
	}
	
	public Graphics draw(Graphics g){
		Images.get("arrow").rotate(angle);
		g.drawImage(Images.get("arrow"),this.getX()-5f*Main.ratioSpace,this.getY()-75f*Main.ratioSpace);
		Images.get("arrow").rotate(-angle);
		Image shadow = Images.get("arrow").getScaledCopy(Main.ratioSpace);
		shadow.rotate(this.angle);
		shadow.drawFlash(this.getX()-5f*Main.ratioSpace,this.getY()-5f*Main.ratioSpace,shadow.getWidth(),shadow.getHeight(),new Color(0,0,0,0.3f));
		//g.drawImage(i ,this.getX()-5f,this.getY()-5f);
		//g.setColor(Color.white);
		//		g.setColor(new Color(5,5,5,0.2f));
		//		g.draw(this.collisionBox);
		return g;
	}
	public void action(Plateau plateau){
		//MULTI 
		this.life  -= Main.increment;
		if(life<0f){
			this.lifePoints = -1f;
		}
		
		this.setXY(this.getX()+this.vx, this.getY()+this.vy, plateau);
		if(this.x>plateau.maxX || this.x<0 || this.y>plateau.maxY||this.y<0){
			this.setLifePoints(-1f);
		}
	}


	// For MenuArrow
	public Arrow(Plateau plateau){
		super(plateau);
	}

}
