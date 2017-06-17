package spells;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import data.Attributs;
import main.Main;
import model.Game;
import plateau.Character;
import plateau.Objet;
import plateau.Plateau;
import ressources.Images;
import utils.ObjetsList;
import utils.Utils;

public class Immolation extends SpellEffect{

	public float remainingTime;
	public float damage;
	public float step;
	public float lifepointStart;
	
	public boolean active = false;
	public Immolation(Character launcher, Objet t, Plateau plateau){
		launcher.canMove = false;
		launcher.etats.add(Etats.Immolated);
		this.type = 1;
		this.name = ObjetsList.Immolation;
		this.x = launcher.getX();
		this.y = launcher.getY();
		remainingTime = this.getAttribut(Attributs.totalTime);	
		// Calculate step of lifepoints
		lifepointStart = launcher.lifePoints;
		System.out.println("lp start : "+lifepointStart);
		System.out.println("Remaining time "+remainingTime);
		step = lifepointStart/(Main.framerate*remainingTime);
		System.out.println("step "+step);
		this.name = ObjetsList.Immolation;
		this.lifePoints = 1f;
		Game.gameSystem.plateau.addSpell(this);
		owner = launcher.id;
		this.team = launcher.getTeam();
	}

	public void action(Plateau plateau){
		
		this.remainingTime-=1f/Main.framerate;
		Objet owner = this.getOwner();
		if(owner!=null){			
			owner.setLifePoints(owner.lifePoints-step);
		}
		if(owner!=null && this.remainingTime-1f/Main.framerate<=0f){
			// Test if explosion
			if(getOwner().getAttribut(Attributs.explosionWhenImmolate)==1){
				for(Character c : Game.gameSystem.plateau.characters){
					if(Utils.distance(c, this.getOwner())<100f && c!=this.getOwner()){
						c.setLifePoints(c.lifePoints-20f);
					}
				}
			}
			getOwner().lifePoints = -1f;
			this.lifePoints=-1f;
		}
		if(this.getOwner()==null){
			this.lifePoints=-1f;
		}
	}

	public Graphics draw(Graphics g){
		float r = 60f*Main.ratioSpace;
		Image fire = Images.get("explosion").getScaledCopy(Main.ratioSpace);
		r = fire.getWidth()/5f;
		x = this.getX();
		y = this.getY();
		if(this.remainingTime>=65f){
			g.drawImage(fire, x-40f*Main.ratioSpace, y-40f*Main.ratioSpace, x+40f*Main.ratioSpace, y+40f*Main.ratioSpace,0f,0f,r,r);
		}
		else if(this.remainingTime>=55f)
			g.drawImage(fire, x-40f*Main.ratioSpace, y-40f*Main.ratioSpace, x+40f*Main.ratioSpace, y+40f*Main.ratioSpace,r,0f,2*r,r);
		else if(this.remainingTime>=45f)
			g.drawImage(fire, x-40f*Main.ratioSpace, y-40f*Main.ratioSpace, x+40f*Main.ratioSpace, y+40f*Main.ratioSpace,2*r,0f,3*r,r);
		else if(this.remainingTime>=40f*Main.ratioSpace)
			g.drawImage(fire, x-40f*Main.ratioSpace, y-40f*Main.ratioSpace, x+40f*Main.ratioSpace, y+40f*Main.ratioSpace,3*r,0f,4*r,r);
		else if(this.remainingTime>=35f)
			g.drawImage(fire, x-40f*Main.ratioSpace, y-40f*Main.ratioSpace, x+40f*Main.ratioSpace, y+40f*Main.ratioSpace,4*r,0f,5*r,r);
		else if(this.remainingTime>=40f*Main.ratioSpace)
			g.drawImage(fire, x-40f*Main.ratioSpace, y-40f*Main.ratioSpace, x+40f*Main.ratioSpace, y+40f*Main.ratioSpace,3*r,0f,4*r,r);
		else if(this.remainingTime>=35f)
			g.drawImage(fire, x-40f*Main.ratioSpace, y-40f*Main.ratioSpace, x+40f*Main.ratioSpace, y+40f*Main.ratioSpace,4*r,0f,3*r,r);
		else if(this.remainingTime>=30f)
			g.drawImage(fire, x-40f*Main.ratioSpace, y-40f*Main.ratioSpace, x+40f*Main.ratioSpace, y+40f*Main.ratioSpace,3*r,0f,4*r,r);
		else if(this.remainingTime>=25f)
			g.drawImage(fire, x-40f*Main.ratioSpace, y-40f*Main.ratioSpace, x+40f*Main.ratioSpace, y+40f*Main.ratioSpace,4*r,0f,5*r,r);
		else if(this.remainingTime>=20f)
			g.drawImage(fire, x-40f*Main.ratioSpace, y-40f*Main.ratioSpace, x+40f*Main.ratioSpace, y+40f*Main.ratioSpace,3*r,0f,4*r,r);
		else if(this.remainingTime>=15f)
			g.drawImage(fire, x-40f*Main.ratioSpace, y-40f*Main.ratioSpace, x+40f*Main.ratioSpace, y+40f*Main.ratioSpace,4*r,0f,3*r,r);
		else if(this.remainingTime>=10f)
			g.drawImage(fire, x-40f*Main.ratioSpace, y-40f*Main.ratioSpace, x+40f*Main.ratioSpace, y+40f*Main.ratioSpace,3*r,0f,4*r,r);
		else if(this.remainingTime>=5f)
			g.drawImage(fire, x-40f*Main.ratioSpace, y-40f*Main.ratioSpace, x+40f*Main.ratioSpace, y+40f*Main.ratioSpace,4*r,0f,5*r,r);
		else 
			g.drawImage(fire, x-40f*Main.ratioSpace, y-40f*Main.ratioSpace, x+40f*Main.ratioSpace, y+40f*Main.ratioSpace,3*r,0f,4*r,r);

		return g;
	}



	
}
