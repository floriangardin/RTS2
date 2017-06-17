package spells;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;

import data.Attributs;
import data.AttributsChange;
import events.EventAttackDamage;
import main.Main;
import model.Game;
import plateau.Character;
import plateau.Checkpoint;
import plateau.Objet;
import plateau.Plateau;
import ressources.Images;
import utils.ObjetsList;

public class BurningArea extends SpellEffect{

	public float remainingTime = 200f;
	public float totalTime = 200f;
	public float startTime = 0.03f;
	public float endTime = 0.3f;
	public float size;

	public BurningArea(int launcher, Checkpoint t, float size, Plateau plateau){

		this.name = ObjetsList.BlessedAreaEffect;
		this.type = 2;
		this.size = size;
		this.id = plateau.id;
		this.toDrawOnGround = true;
		this.lifePoints = 1f;
		plateau.addSpell(this);
		this.image = "magma";
		this.team = plateau.getById(launcher).getTeam();
		this.collisionBox = createShape(t, size);
		this.x = t.getX();
		this.y = t.getY();
	}
	
	public static Shape createShape(Objet t, float size){
		return new Circle(t.getX(),t.getY(),size);
	}



	public void action(Plateau plateau){
		this.remainingTime-=10f*Main.increment;
		if(this.remainingTime<=0f){
			this.lifePoints = -1f;
		}
	}

	public Graphics draw(Graphics g){
		Image im = Images.get(this.image).getScaledCopy((int)(2*size), (int)(2*size));
		float alpha = (totalTime-remainingTime)/(startTime*remainingTime);
		alpha = (float)Math.min(alpha, remainingTime/(endTime*totalTime));
		alpha = (float)Math.min(alpha, 1f);
		alpha = (float)Math.max(alpha, 0f);
		im.setAlpha(alpha);
		g.drawImage(im, x-im.getWidth()/2f, y-im.getHeight()/2f);
		return g;
	}

	public void collision(Character c){
		if(this.lifePoints>0 && c.getTeam()!=this.team){
			c.setLifePoints(c.lifePoints-0.05f);
		}
	}

}
