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
	public int nbFire=15;
	public Character owner;
	public float[] animationState = new float[nbFire];
	public float[] animationX = new float[nbFire];
	public float[] animationY = new float[nbFire];
	public float animationMax=1000f;
	public float x2,y2;

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
		float width = 5f*Game.ratioSpace;
		
		this.lifePoints = 1f;
		this.p = p;
		p.addSpell(this);
		image = p.g.images.explosion.getScaledCopy(Game.ratioSpace);
		owner = launcher;

		this.collisionBox = new Circle(x,y,radius);
		this.createAnimation(t, launcher);
	}

	
	
	public void createAnimation(Objet o1, Objet o2){
		float x1 = o1.getX(),x2=o2.getX(),y1=o1.getY(),y2=o2.getY();
		for(int i=0;i<nbFire;i++){
			animationX[i] = x1+1f*i*(x2-x1)/nbFire;
			animationY[i] = y1+1f*i*(y2-y1)/nbFire;
		}
	}

	public void action(){
		
		this.remainingTime-=Main.increment;
		if(this.remainingTime<=0f)
			this.lifePoints = -1f;
	}

	public Graphics draw(Graphics g){
		g.setColor(Color.white);
		g.setAntiAlias(true);
		
		g.draw(collisionBox);
		
		g.setAntiAlias(false);
		
		//g.setColor(Color.white);
		//g.draw(this.collisionBox);
		return g;
	}

	public void collision(Character c){
		// Si on est suffisamment dedans on reste bloqué
		if(Utils.distance(c, this)>=this.radius*1f/4f){
			c.stop();
			c.target =null;
		}
	}
	
	public String toString(){
		String s = toStringObjet()+toStringActionObjet()+toStringSpellEffect();
		s+="x2:"+this.x2+";";
		s+="y2:"+this.y2+";";
		s+="idLauncher:"+this.owner.id+";";
		return s;
	}
}
