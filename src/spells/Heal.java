package spells;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

import main.Main;
import model.Character;
import model.Game;
import model.Objet;
import utils.ObjetsList;

public class Heal extends SpellEffect{

	public static float radius = 70f;
	public float remainingTime;
	public float damage;
	public Character owner;
	public boolean active = false;
	public Heal(Character launcher, Objet t){


		this.type = 1;

		this.name = ObjetsList.HealEffect;
		this.x = t.getX();
		this.y = t.getY();
		this.team = launcher.getTeam();
		this.lifePoints = 1f;
		Game.g.plateau.addSpell(this);
		owner = launcher;

		this.collisionBox = new Circle(x,y,radius);
		//this.Game.g.sounds.get("frozen").play(1f,this.Game.g.options.soundVolume);
	}





	public void action(){

		this.remainingTime-=Main.increment;
		if(remainingTime<0.5f){
			if(!active){
				Game.g.sounds.get("frozenActive").play(1f,Game.g.options.soundVolume);
				Game.g.sounds.get("frozen").stop();
			}
			this.active = true;
		}
		if(this.remainingTime<=0f)
			this.lifePoints = -1f;
	}

	public Graphics draw(Graphics g){
		g.setColor(Color.white);
		g.setAntiAlias(true);
		g.draw(collisionBox);
		g.setColor(new Color(99,255,32,0.8f));
		g.fill(collisionBox);
		
		g.setAntiAlias(false);

		//g.setColor(Color.white);
		//g.draw(this.collisionBox);
		return g;
	}

	public void collision(Character c){
		// Si on est suffisamment dedans on reste bloqué
		if(c.getTeam()==this.getTeam()){
			c.setLifePoints(c.lifePoints+1f);
		}
	}

	
}
