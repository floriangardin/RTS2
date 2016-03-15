package spells;

import main.Main;
import model.Game;
import model.Objet;
import model.Plateau;
import model.Utils;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Circle;
import units.Character;

public class Fence extends SpellEffect{

	public static float radius = 70f;
	public float remainingTime;
	public float damage;
	public Image image;
	public Character owner;
	public boolean active = false;
	public Fence(Plateau p, Character launcher, Objet t,int id){

		if(id==-1){
			this.id = p.g.idChar;
			p.g.idChar+=1;
		}
		else{
			this.id =id;
		}

		this.type = 1;

		this.x = t.getX();
		this.y = t.getY();

		this.lifePoints = 1f;
		this.p = p;
		p.addSpell(this);
		image = p.g.images.explosion.getScaledCopy(Game.ratioSpace);
		owner = launcher;

		this.collisionBox = new Circle(x,y,radius);
		this.p.g.sounds.iceStart.play(1f,this.p.g.options.soundVolume);
	}





	public void action(){

		this.remainingTime-=Main.increment;
		if(remainingTime<1f){
			if(!active){
				this.p.g.sounds.iceActive.play(1f,this.p.g.options.soundVolume);
				this.p.g.sounds.iceStart.stop();
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
		if(!(this.remainingTime>2f)){
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

	public String toString(){
		String s = toStringObjet()+toStringActionObjet()+toStringSpellEffect();
		s+="x:"+this.x+";";
		s+="y:"+this.y+";";
		s+="idLauncher:"+this.owner.id+";";
		return s;
	}
}
