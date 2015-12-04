package spells;

import java.util.Vector;

import main.Main;
import model.Checkpoint;
import model.Plateau;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

import units.Character;

public class BlessedArea extends SpellEffect{

	public float remainingTime;
	public float effect;
	public Image image;
	public int nbFire=4;
	public Character owner;
	public float animationState = 0f;
	public float[] animationX = new float[nbFire];
	public float[] animationY = new float[nbFire];
	public float animationMax=120f;
	public float size = 200f;;
	public Vector<Character> targeted = new Vector<Character>();

	public BlessedArea(Plateau p, Character launcher, Checkpoint t,int id){
		if(id==-1){
			this.id = p.g.idChar;
			p.g.idChar+=1;
		}
		else{
			this.id =id;
		}
		this.type = 2;
		this.id = p.g.idChar;
		p.g.idChar+=1;
		this.lifePoints = 1f;
		this.p = p;
		p.addSpell(this);
		this.image = p.g.images.blessedArea;
		owner = launcher;
		this.collisionBox = new Rectangle(t.getX()-size/2f,t.getY()-size/2f,size,size);
		this.x = t.getX();
		this.y = t.getY();
		this.createAnimation();
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
		//MULTI
		this.changes.x = true;
		this.changes.y = true;
		
		this.remainingTime-=10f*Main.increment;
		Vector<Character> toDelete = new Vector<Character>();
		if(this.remainingTime<=0f){
			this.lifePoints = -1f;
			for(Character c:this.targeted){
				c.chargeTime/=this.effect;
				toDelete.add(c);
			}
		}
		for(Character c:toDelete){
			this.targeted.remove(c);
		}
		toDelete = new Vector<Character>();
		for(Character c:this.targeted){
			if(!c.collisionBox.intersects(this.collisionBox)){
				c.chargeTime/=this.effect;
				toDelete.add(c);
			}
		}
		for(Character c:toDelete){
			this.targeted.remove(c);
		}
	}

	public Graphics draw(Graphics g){
		this.animationState +=1f*Main.increment;
		if(this.animationState>animationMax)
			animationState = 0f;
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
		if(this.lifePoints>0 && c.getTeam()==owner.getTeam() && !this.targeted.contains(c)){
			c.chargeTime*=this.effect;
			this.targeted.addElement(c);
		}
	}
	
	
	public String toString(){
		String s = toStringObjet()+toStringActionObjet()+toStringSpellEffect();
		s+="idLauncher:"+this.owner.id+";";
		return s;
	}
}
