package spells;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

import main.Main;
import model.Game;
import plateau.Character;
import plateau.Objet;
import plateau.Plateau;
import utils.ObjetsList;

public strictfp class Heal extends SpellEffect{

	public static float radius = 70f;
	public float remainingTime;
	public float damage;
	public Character owner;
	public boolean active = false;
	public Heal(Character launcher, Objet t, Plateau plateau){
		super(plateau);

		this.type = 1;

		this.setName(ObjetsList.HealEffect);
		this.setX(t.getX());
		this.setY(t.getY());
		this.team = launcher.getTeam();
		this.setLifePoints(1f);
		plateau.addSpell(this);
		owner = launcher;

		this.setCollisionBox(new Circle(getX(),getY(),radius));
		//this.Game.g.sounds.get("frozen").play(1f,this.Game.g.options.soundVolume);
	}





	public void action(Plateau plateau){

		this.remainingTime-=Main.increment;
		if(remainingTime<0.5f){
			if(!active){
//				Game.g.sounds.get("frozenActive").play(1f,Game.g.options.soundVolume);
//				Game.g.sounds.get("frozen").stop();
			}
			this.active = true;
		}
		if(this.remainingTime<=0f)
			this.setLifePoints(-1f);
	}

	public Graphics draw(Graphics g){
		g.setColor(Color.white);
		g.setAntiAlias(true);
		g.draw(getCollisionBox());
		g.setColor(new Color(99,255,32,0.8f));
		g.fill(getCollisionBox());
		
		g.setAntiAlias(false);

		//g.setColor(Color.white);
		//g.draw(this.collisionBox);
		return g;
	}

	@Override
	public void collision(Character c, Plateau plateau){
		// Si on est suffisamment dedans on reste bloqué
		if(c.getTeam()==this.getTeam()){
			c.setLifePoints(c.getLifePoints()+1f, plateau);
		}
	}

	
}
