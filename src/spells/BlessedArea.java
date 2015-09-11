package spells;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

import model.*;
import model.Plateau;
import units.Character;

public class BlessedArea extends SpellEffect{

	public float remainingTime;
	public float damage;
	public Image image;
	public int nbFire=4;
	public Character owner;
	public float animationState = 0f;
	public float[] animationX = new float[nbFire];
	public float[] animationY = new float[nbFire];
	public float animationMax=120f;
	public float size;

	public BlessedArea(Plateau p, Character launcher, Checkpoint target){
		this.id = p.g.idChar;
		p.g.idChar+=1;
		this.lifePoints = 1f;
		p.addSpell(this);
		this.image = p.images.blessedArea;
		owner = launcher;
	}

	public void createAnimation(){
		animationX[0] = this.getX()+size/4f;
		animationY[0] = this.getY()+size/4f;
		animationX[1] = this.getX()-size/4f;
		animationY[1] = this.getY()-size/4f;
		animationX[2] = this.getX()-size/4f;
		animationY[2] = this.getY()+size/4f;
		animationX[3] = this.getX()+size/4f;
		animationY[3] = this.getY()-size/4f;
	}

	public void action(){
		this.remainingTime-=1f;
		this.animationState +=1f;
		if(this.animationState>animationMax)
			animationState = 0f;
		if(this.remainingTime<=0f)
			this.lifePoints = -1f;
	}

	public Graphics draw(Graphics g){
		float x,y,r,currentAnimation;
		for(int i=0;i<4;i++){
				r = this.image.getWidth()/4f;
				x = this.animationX[i];
				y = this.animationY[i];
				currentAnimation = this.animationState+1f*i*animationMax/4f;
				if(currentAnimation>animationMax)
					currentAnimation-=animationMax;
				if(currentAnimation>=this.animationMax*3f/4f)
					g.drawImage(this.image, x-40f, y-40f, x+40f, y+40f,0f,0f,r,r);
				else if(currentAnimation>=this.animationMax*2f/4f)
					g.drawImage(this.image, x-40f, y-40f, x+40f, y+40f,r,0f,2*r,r);
				else if(currentAnimation>=this.animationMax*1f/4f)
					g.drawImage(this.image, x-40f, y-40f, x+40f, y+40f,2*r,0f,3*r,r);
				else 
					g.drawImage(this.image, x-40f, y-40f, x+40f, y+40f,3*r,0f,4*r,r);
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
