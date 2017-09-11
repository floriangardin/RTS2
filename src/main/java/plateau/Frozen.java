package plateau;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;

import main.Main;
import model.Game;
import utils.ObjetsList;

public strictfp class Frozen extends SpellEffect{

	public float remainingTime;
	public float damage;
	public Character owner;
	public boolean active = false;
	public Frozen(Character launcher, Objet t, float radius, Plateau plateau){
		super(plateau);
		launcher.getEtats().add(Etats.Frozen);
		this.type = 1;

		this.setX(t.getX());
		this.setY(t.getY());

		this.setName(ObjetsList.FrozenEffect);
		this.setLifePoints(1f);
		plateau.addSpell(this);
		owner = launcher;
		this.team = launcher.getTeam();

		this.setCollisionBox(createShape(launcher, t, radius));
//		Game.g.sounds.get("frozen").play(1f,Game.g.options.soundVolume);
	}

	public static Shape createShape(Character launcher, Objet t, float radius){
		return new Circle(t.getX(),t.getY(),radius);
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
		if(!(this.remainingTime>0.5f)){
			g.setColor(new Color(60,100,250,0.2f));
			g.fill(getCollisionBox());
		}

		g.setAntiAlias(false);

		//g.setColor(Color.white);
		//g.draw(this.collisionBox);
		return g;
	}

	public void collision(Character c){
		// Si on est suffisamment dedans on reste bloqué
		if(active){
			c.setFrozen(10f);
		}
	}

	
}
