package spells;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import data.Attributs;
import data.AttributsChange;
import data.AttributsChange.Change;
import main.Main;
import model.Checkpoint;
import model.Game;
import model.Objet;
import units.Character;
import utils.ObjetsList;

public class BlessedArea extends SpellEffect{

	public float remainingTime;
	public Vector<AttributsChange> ac;
	public int nbFire=4;
	public Character owner;
	public float animationState = 0f;
	public float[] animationX = new float[nbFire];
	public float[] animationY = new float[nbFire];
	public float animationMax=120f;
	public float size;
	public Vector<Character> targeted = new Vector<Character>();

	public BlessedArea(Character launcher, Checkpoint t,int id, float size){
		if(id==-1){
			this.id = Game.g.idChar;
			Game.g.idChar+=1;
		}
		else{
			this.id =id;
		}
		this.name = ObjetsList.BlessedAreaEffect;
		this.type = 2;
		this.size = size;
		this.id = Game.g.idChar;
		Game.g.idChar+=1;
		this.lifePoints = 1f;
		Game.g.plateau.addSpell(this);
		this.image = "blessedArea";
		this.ac = new Vector<AttributsChange>();
		ac.add(new AttributsChange(Attributs.chargeTime,Change.MUL,0.5f,this.remainingTime));
		owner = launcher;
		this.setTeam(launcher.getTeam());
		this.collisionBox = createShape(launcher, t, size);
		this.x = t.getX();
		this.y = t.getY();
		this.createAnimation();
	}
	
	public static Shape createShape(Character launcher, Objet t, float size){
		return new Rectangle(t.getX()-size/2f,t.getY()-size/2f,size,size);
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
		this.remainingTime-=10f*Main.increment;
		for(AttributsChange ac : this.ac){
			ac.remainingTime = this.remainingTime;
		}
		Vector<Character> toDelete = new Vector<Character>();
		if(this.remainingTime<=0f){
			this.lifePoints = -1f;
		}
		for(Character c:this.targeted){
			if(this.lifePoints == -1f || !c.collisionBox.intersects(this.collisionBox)){
				toDelete.add(c);
			}
		}
		for(Character c:toDelete){
			this.targeted.remove(c);
		}
	}

	public Graphics draw(Graphics g){
		this.animationState +=30f*Main.increment;
		if(this.animationState>animationMax)
			animationState = 0f;
		float x,y,r,currentAnimation;
		Image im = Game.g.images.get(this.image);
		for(int i=0;i<4;i++){
			r = im.getWidth()/4f;
			x = this.animationX[i];
			y = this.animationY[i];
			currentAnimation = this.animationState+1f*i*animationMax/4f;
			if(currentAnimation>animationMax)
				currentAnimation-=animationMax;
			if(currentAnimation>=this.animationMax*3f/4f)
				g.drawImage(im, x-40f, y-40f, x+40f, y+40f,0f,0f,r,r);
			else if(currentAnimation>=this.animationMax*2f/4f)
				g.drawImage(im, x-40f, y-40f, x+40f, y+40f,r,0f,2*r,r);
			else if(currentAnimation>=this.animationMax*1f/4f)
				g.drawImage(im, x-40f, y-40f, x+40f, y+40f,2*r,0f,3*r,r);
			else 
				g.drawImage(im, x-40f, y-40f, x+40f, y+40f,3*r,0f,4*r,r);
		}
		g.setColor(Color.white);
		g.draw(this.collisionBox);
		return g;
	}

	public void collision(Character c){
		if(this.lifePoints>0 && c.getTeam()==owner.getTeam() && !this.targeted.contains(c)){
			for(AttributsChange ac : this.ac){
				c.attributsChanges.add(ac);
			}
			this.targeted.addElement(c);
		}
	}



}
