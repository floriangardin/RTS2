package spells;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import model.Objet;
import model.Plateau;
import units.Character;

public class Firewall extends SpellEffect{


	public float remainingTime;
	public float damage;
	public Image image;
	public int nbFire=15;
	public Character owner;
	public float[] animationState = new float[nbFire];
	public float[] animationX = new float[nbFire];
	public float[] animationY = new float[nbFire];
	public float animationMax=120f;

	public Firewall(Plateau p, Character launcher){
		this.id = p.g.idChar;
		p.g.idChar+=1;
		this.lifePoints = 1f;
		p.addSpell(this);
		image = p.images.explosion;
		owner = launcher;
	}

	public void createAnimation(Objet o1, Objet o2){
		float x1 = o1.getX(),x2=o2.getX(),y1=o1.getY(),y2=o2.getY();
		for(int i=0;i<nbFire;i++){
			animationX[i] = x1+1f*i*(x2-x1)/nbFire;
			animationY[i] = y1+1f*i*(y2-y1)/nbFire;
		}
	}

	public void action(){
		this.remainingTime-=1f;
		int i = (int)(Math.random()*nbFire*5);
		if(i<nbFire){
			if(this.animationState[i]==0f)
				this.animationState[i]=1f;
		}
		for(int k=0; k<nbFire;k++){
			if(this.animationState[k]>0f)
				this.animationState[k]+=1f;
			if(this.animationState[k]>animationMax)
				this.animationState[k] = 0f;
		}
		if(this.remainingTime<=0f)
			this.lifePoints = -1f;
	}

	public Graphics draw(Graphics g){
		float x,y,r;
		for(int i=0;i<nbFire;i++){
			if(this.animationState[i]>0f){
				r = this.image.getWidth()/5f;
				x = this.animationX[i];
				y = this.animationY[i];
				if(this.animationState[i]>=this.animationMax*4f/5f)
					g.drawImage(this.image, x-40f, y-40f, x+40f, y+40f,0f,0f,r,r);
				else if(this.animationState[i]>=this.animationMax*3f/5f)
					g.drawImage(this.image, x-40f, y-40f, x+40f, y+40f,r,0f,2*r,r);
				else if(this.animationState[i]>=this.animationMax*2f/5f)
					g.drawImage(this.image, x-40f, y-40f, x+40f, y+40f,2*r,0f,3*r,r);
				else if(this.animationState[i]>=this.animationMax*1f/5f)
					g.drawImage(this.image, x-40f, y-40f, x+40f, y+40f,3*r,0f,4*r,r);
				else 
					g.drawImage(this.image, x-40f, y-40f, x+40f, y+40f,4*r,0f,5*r,r);
			}
		}
		g.setColor(Color.white);
		g.draw(this.collisionBox);
		return g;
	}

	public void collision(Character c){
		if(c!=owner){
			c.lifePoints-=this.damage;
		}
	}
}
