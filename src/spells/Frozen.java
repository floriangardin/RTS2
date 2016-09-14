package spells;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;

import main.Main;
import model.Character;
import model.Game;
import model.Objet;
import utils.ObjetsList;

public class Frozen extends SpellEffect{

	public float remainingTime;
	public float damage;
	public Character owner;
	public boolean active = false;
	public Frozen(Character launcher, Objet t, float radius){

		launcher.etats.add(Etats.Frozen);
		this.type = 1;

		this.x = t.getX();
		this.y = t.getY();

		this.name = ObjetsList.FrozenEffect;
		this.lifePoints = 1f;
		Game.g.plateau.addSpell(this);
		owner = launcher;
		this.setTeam(launcher.getTeam());

		this.collisionBox = createShape(launcher, t, radius);
		Game.g.sounds.get("frozen").play(1f,Game.g.options.soundVolume);
	}

	public static Shape createShape(Character launcher, Objet t, float radius){
		return new Circle(t.getX(),t.getY(),radius);
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
		if(!(this.remainingTime>0.5f)){
			g.setColor(new Color(60,100,250,0.2f));
			g.fill(collisionBox);
		}

		g.setAntiAlias(false);

		//g.setColor(Color.white);
		//g.draw(this.collisionBox);
		return g;
	}

	public void collision(Character c){
		// Si on est suffisamment dedans on reste bloqué
		if(active){
			c.frozen = 10f;
		}
	}

	
}
