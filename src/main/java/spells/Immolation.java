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

public strictfp class Immolation extends SpellEffect{

	public float remainingTime;
	public float damage;
	public float step;
	public float lifepointStart;
	
	public boolean active = false;
	public Immolation(Character launcher, Objet t, Plateau plateau){
		super(plateau);
		launcher.canMove = false;
		launcher.getEtats().add(Etats.Immolated);
		this.type = 1;
		this.setName(ObjetsList.Immolation);
		this.setX(launcher.getX());
		this.setY(launcher.getY());
		remainingTime = this.getAttribut(Attributs.totalTime);	
		// Calculate step of lifepoints
		lifepointStart = launcher.getLifePoints();
		System.out.println("lp start : "+lifepointStart);
		System.out.println("Remaining time "+remainingTime);
		step = lifepointStart/(Main.framerate*remainingTime);
		System.out.println("step "+step);
		this.setName(ObjetsList.Immolation);
		this.setLifePoints(1f);
		plateau.addSpell(this);
		owner = launcher.getId();
		this.team = launcher.getTeam();
	}

	public void action(Plateau plateau){
		
		this.remainingTime-=1f/Main.framerate;
		Objet owner = this.getOwner(plateau);
		if(owner!=null){			
			owner.setLifePoints(owner.getLifePoints()-step, plateau);
		}
		if(owner!=null && this.remainingTime-1f/Main.framerate<=0f){
			// Test if explosion
			if(getOwner(plateau).getAttribut(Attributs.explosionWhenImmolate)==1){
				for(Character c : plateau.getCharacters()){
					if(Utils.distance(c, this.getOwner(plateau))<100f && c!=this.getOwner(plateau)){
						c.setLifePoints(c.getLifePoints()-20f, plateau);
					}
				}
			}
			getOwner(plateau).setLifePoints(-1f, plateau);
			this.setLifePoints(-1f);
		}
		if(this.getOwner(plateau)==null){
			this.setLifePoints(-1f);
		}
	}

	public Graphics draw(Graphics g){
		float r = 60f*Main.ratioSpace;
		Image fire = Images.get("explosion").getScaledCopy(Main.ratioSpace);
		r = fire.getWidth()/5f;
		setX(this.getX());
		setY(this.getY());
		if(this.remainingTime>=65f){
			g.drawImage(fire, getX()-40f*Main.ratioSpace, getY()-40f*Main.ratioSpace, getX()+40f*Main.ratioSpace, getY()+40f*Main.ratioSpace,0f,0f,r,r);
		}
		else if(this.remainingTime>=55f)
			g.drawImage(fire, getX()-40f*Main.ratioSpace, getY()-40f*Main.ratioSpace, getX()+40f*Main.ratioSpace, getY()+40f*Main.ratioSpace,r,0f,2*r,r);
		else if(this.remainingTime>=45f)
			g.drawImage(fire, getX()-40f*Main.ratioSpace, getY()-40f*Main.ratioSpace, getX()+40f*Main.ratioSpace, getY()+40f*Main.ratioSpace,2*r,0f,3*r,r);
		else if(this.remainingTime>=40f*Main.ratioSpace)
			g.drawImage(fire, getX()-40f*Main.ratioSpace, getY()-40f*Main.ratioSpace, getX()+40f*Main.ratioSpace, getY()+40f*Main.ratioSpace,3*r,0f,4*r,r);
		else if(this.remainingTime>=35f)
			g.drawImage(fire, getX()-40f*Main.ratioSpace, getY()-40f*Main.ratioSpace, getX()+40f*Main.ratioSpace, getY()+40f*Main.ratioSpace,4*r,0f,5*r,r);
		else if(this.remainingTime>=40f*Main.ratioSpace)
			g.drawImage(fire, getX()-40f*Main.ratioSpace, getY()-40f*Main.ratioSpace, getX()+40f*Main.ratioSpace, getY()+40f*Main.ratioSpace,3*r,0f,4*r,r);
		else if(this.remainingTime>=35f)
			g.drawImage(fire, getX()-40f*Main.ratioSpace, getY()-40f*Main.ratioSpace, getX()+40f*Main.ratioSpace, getY()+40f*Main.ratioSpace,4*r,0f,3*r,r);
		else if(this.remainingTime>=30f)
			g.drawImage(fire, getX()-40f*Main.ratioSpace, getY()-40f*Main.ratioSpace, getX()+40f*Main.ratioSpace, getY()+40f*Main.ratioSpace,3*r,0f,4*r,r);
		else if(this.remainingTime>=25f)
			g.drawImage(fire, getX()-40f*Main.ratioSpace, getY()-40f*Main.ratioSpace, getX()+40f*Main.ratioSpace, getY()+40f*Main.ratioSpace,4*r,0f,5*r,r);
		else if(this.remainingTime>=20f)
			g.drawImage(fire, getX()-40f*Main.ratioSpace, getY()-40f*Main.ratioSpace, getX()+40f*Main.ratioSpace, getY()+40f*Main.ratioSpace,3*r,0f,4*r,r);
		else if(this.remainingTime>=15f)
			g.drawImage(fire, getX()-40f*Main.ratioSpace, getY()-40f*Main.ratioSpace, getX()+40f*Main.ratioSpace, getY()+40f*Main.ratioSpace,4*r,0f,3*r,r);
		else if(this.remainingTime>=10f)
			g.drawImage(fire, getX()-40f*Main.ratioSpace, getY()-40f*Main.ratioSpace, getX()+40f*Main.ratioSpace, getY()+40f*Main.ratioSpace,3*r,0f,4*r,r);
		else if(this.remainingTime>=5f)
			g.drawImage(fire, getX()-40f*Main.ratioSpace, getY()-40f*Main.ratioSpace, getX()+40f*Main.ratioSpace, getY()+40f*Main.ratioSpace,4*r,0f,5*r,r);
		else 
			g.drawImage(fire, getX()-40f*Main.ratioSpace, getY()-40f*Main.ratioSpace, getX()+40f*Main.ratioSpace, getY()+40f*Main.ratioSpace,3*r,0f,4*r,r);

		return g;
	}



	
}
