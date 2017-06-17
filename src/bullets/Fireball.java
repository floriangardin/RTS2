package bullets;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;

import data.Attributs;
import events.EventNames;
import main.Main;
import model.Game;
import plateau.Building;
import plateau.Character;
import plateau.Checkpoint;
import plateau.Objet;
import plateau.Plateau;
import ressources.Images;
import spells.BurningArea;
import utils.ObjetsList;
import utils.Utils;

public class Fireball extends Bullet {

	protected float altitude;
	protected int animation;
	protected float angle;
	protected boolean explosion= false;

	public Fireball( Objet owner,float targetX,float targetY,float vx,float vy,float damage, Plateau plateau){
		//MULTI 
		// Parameters
		this.altitude = 0f;
		this.name = ObjetsList.Fireball;
		//
		this.size = 10f*Main.ratioSpace;
		Game.gameSystem.plateau.addBulletObjets(this);
		this.damage = damage;
		this.animation = 0;
		this.lifePoints = 30f;
		this.owner = owner.id;
		this.team = owner.getTeam();
		this.areaEffect = getAttribut(Attributs.size)*Main.ratioSpace;
		float Vmax = getAttribut(Attributs.maxVelocity)*Main.ratioSpace;
		float size = 10f*Main.ratioSpace;
		this.setTarget(new Checkpoint(targetX,targetY, plateau));
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
//		Game.g.triggerEvent(EventNames.FireBallLaunched, this);
	}

	public Fireball(){}
	public void action(Plateau plateau){

		if(explosion){
			this.setLifePoints(lifePoints-10f*Main.increment);
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
		Circle area = new Circle(this.getX(),this.getY(),this.areaEffect);

//		for(Character c : Game.gameSystem.plateau.characters){
//			if(c.collisionBox.intersects(area) && c.getTeam()!=this.owner.getTeam()){
//				this.boom(c);
//
//			}
//		}

		new BurningArea(this.owner, new Checkpoint(x,y, plateau), this.areaEffect, plateau);
		this.explosion = true;
	}
	public void boom(Character c){
		float damage = this.damage;
		if(c.getAttributString(Attributs.weapon)!= null && c.getAttributString(Attributs.weapon) == "bow")
			damage = damage * this.getTeam().data.bonusBowFoot;
		c.setLifePoints(c.lifePoints-damage);
		c.isAttacked();

	}

	public void boom(Building c){

	}

	public Graphics draw(Graphics g){

		if(this.explosion){
			Image boom = Images.get("explosion").getScaledCopy(Main.ratioSpace);
			float r = boom.getWidth()/5f;
			if(lifePoints>=24f)
				g.drawImage(boom, this.getX()-40f*Main.ratioSpace, this.getY()-40f*Main.ratioSpace, this.getX()+40f*Main.ratioSpace, this.getY()+40f*Main.ratioSpace,0f,0f,r,r);
			else if(lifePoints>=18f)
				g.drawImage(boom, this.getX()-40f*Main.ratioSpace, this.getY()-40f*Main.ratioSpace, this.getX()+40f*Main.ratioSpace, this.getY()+40f*Main.ratioSpace,r,0f,2*r,r);
			else if(lifePoints>=12f)
				g.drawImage(boom, this.getX()-40f*Main.ratioSpace, this.getY()-40f*Main.ratioSpace, this.getX()+40f*Main.ratioSpace, this.getY()+40f*Main.ratioSpace,2*r,0f,3*r,r);
			else if(lifePoints>=6f)
				g.drawImage(boom, this.getX()-40f*Main.ratioSpace, this.getY()-40f*Main.ratioSpace, this.getX()+40f*Main.ratioSpace, this.getY()+40f*Main.ratioSpace,3*r,0f,4*r,r);
			else 
				g.drawImage(boom, this.getX()-40f*Main.ratioSpace, this.getY()-40f*Main.ratioSpace, this.getX()+40f*Main.ratioSpace, this.getY()+40f*Main.ratioSpace,4*r,0f,5*r,r);

		} else {
			if(animation<3)	{
				Image image = (Images.get("fireball")).getSubImage(0, 150, 75, 75).getScaledCopy(Main.ratioSpace);
				image.rotate(this.angle);
				g.drawImage(image, this.getX()-28*Main.ratioSpace, this.getY()-28*Main.ratioSpace, this.getX()+28*Main.ratioSpace, this.getY()+28*Main.ratioSpace,0f,0f,image.getWidth(),image.getHeight());
			}else if(animation<6)	{
				Image image1 = (Images.get("fireball")).getSubImage(75, 150, 75, 75).getScaledCopy(Main.ratioSpace);
				image1.rotate(this.angle);
				g.drawImage(image1, this.getX()-28*Main.ratioSpace, this.getY()-28*Main.ratioSpace, this.getX()+28*Main.ratioSpace, this.getY()+28*Main.ratioSpace,0f,0f,image1.getWidth(),image1.getHeight());
			}else	{
				Image image2 = (Images.get("fireball")).getSubImage(150, 150, 75, 75).getScaledCopy(Main.ratioSpace);
				image2.rotate(this.angle);
				g.drawImage(image2, this.getX()-28*Main.ratioSpace, this.getY()-28*Main.ratioSpace, this.getX()+28*Main.ratioSpace, this.getY()+28*Main.ratioSpace,0f,0f,image2.getWidth(),image2.getHeight());
			}
		}
		//g.setColor(Color.black);
		//g.fill(new Circle(this.collisionBox.getCenterX(),this.collisionBox.getCenterY(),this.collisionBox.getBoundingCircleRadius()));
		return g;
	}



	@Override
	public void collision(Character c, Plateau plateau) {
		// TODO Auto-generated method stub

	}

}
