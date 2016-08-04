package spells;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;

import main.Main;
import model.Game;
import model.GameTeam;
import model.Objet;
import model.Plateau;
import units.Character;

public class Heal extends SpellEffect{

	public static float radius = 70f;
	public float remainingTime;
	public float damage;
	public Image image;
	public Character owner;
	public boolean active = false;
	public Heal(Character launcher, Objet t,int id,GameTeam gameTeam){

		if(id==-1){
			this.id = Game.g.idChar;
			Game.g.idChar+=1;
		}
		else{
			this.id =id;
		}

		this.type = 1;

		this.x = t.getX();
		this.y = t.getY();
		this.gameteam = gameTeam;
		this.lifePoints = 1f;
		Game.g.plateau.addSpell(this);
		image = Game.g.images.get("explosion").getScaledCopy(Main.ratioSpace);
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

	public String toString(){
		String s = toStringObjet()+toStringActionObjet()+toStringSpellEffect();
		s+="x:"+this.x+";";
		s+="y:"+this.y+";";
		s+="idLauncher:"+this.owner.id+";";
		return s;
	}
}
