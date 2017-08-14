package spells;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;

import main.Main;
import model.Game;
import plateau.Character;
import plateau.Objet;
import plateau.Plateau;
import ressources.Images;
import utils.ObjetsList;

public class Firewall extends SpellEffect{


	public float remainingTime;
	public float damage;
	public int nbFire=8;
	public Character owner;
	public float[] animationState = new float[nbFire];
	public float[] animationX = new float[nbFire];
	public float[] animationY = new float[nbFire];
	public float animationMax=4f;
	public float x2,y2;

	public Firewall( Character launcher, Objet t,float width, Plateau plateau){
		super(plateau);
		this.type = 1;
		this.x = launcher.getX();
		this.y = launcher.getY();
		this.x2 = t.getX();
		this.y2 = t.getY();

		this.name = ObjetsList.FirewallEffect;
		this.lifePoints = 1f;
		plateau.addSpell(this);
		image = "explosion";
		this.team = launcher.getTeam();
		owner = launcher;
		this.collisionBox = createShape(launcher, t, width) ;
		this.createAnimation(t, launcher);
	}

	public static Shape createShape(Character launcher, Objet t, float width){
		float vx = t.getY()-launcher.getY();
		float vy = launcher.getX()-t.getX();
		float norm = (float)Math.sqrt(vx*vx+vy*vy);
		vx = vx/norm;
		vy = vy/norm;
		float ax,ay,bx,by,cx,cy,dx,dy;
		ax = launcher.getX()+vx*width/2f;
		ay = launcher.getY()+vy*width/2f;
		bx = launcher.getX()-vx*width/2f;
		by = launcher.getY()-vy*width/2f;
		dx = t.getX()+vx*width/2f;
		dy = t.getY()+vy*width/2f;
		cx = t.getX()-vx*width/2f;
		cy = t.getY()-vy*width/2f;
		float[] arg = {ax,ay,bx,by,cx,cy,dx,dy};
		return new Polygon(arg);
	}



	public void createAnimation(Objet o1, Objet o2){
		float x1 = o1.getX(),x2=o2.getX(),y1=o1.getY(),y2=o2.getY();
		for(int i=0;i<nbFire;i++){
			animationX[i] = x1+1f*i*(x2-x1)/nbFire;
			animationY[i] = y1+1f*i*(y2-y1)/nbFire;
			animationState[i] = 0f;
		}
	}

	public void action(Plateau plateau){

		this.remainingTime-=10f*Main.increment;
		if(this.remainingTime<=0f)
			this.lifePoints = -1f;
	}

	public Graphics draw(Graphics g){
		int j = (int)(Math.random()*nbFire*70*Main.increment);
		if(j<nbFire){
			if(this.animationState[j]==0f)
				this.animationState[j]=1f;
		}
		for(int k=0; k<nbFire;k++){
			if(this.animationState[k]>0f)
				this.animationState[k]+=1f*Main.increment;
			if(this.animationState[k]>=animationMax)
				this.animationState[k]=0f;
		}
		float x,y,r;
		Image im = Images.get(this.image).getScaledCopy(Main.ratioSpace);
		for(int i=0;i<nbFire;i++){
			if(this.animationState[i]>0f){
				r = im.getWidth()/5f;
				x = this.animationX[i];
				y = this.animationY[i];
				if(this.animationState[i]>=this.animationMax*5f/6f){
					g.drawImage(im, x-40f*Main.ratioSpace, y-40f*Main.ratioSpace, x+40f*Main.ratioSpace, y+40f*Main.ratioSpace,4*r,0f,5*r,r);
				}else if(this.animationState[i]>=this.animationMax*4f/6f){
					g.drawImage(im, x-40f*Main.ratioSpace, y-40f*Main.ratioSpace, x+40f*Main.ratioSpace, y+40f*Main.ratioSpace,3*r,0f,4*r,r);
				}else if(this.animationState[i]>=this.animationMax*3f/6f){
					g.drawImage(im, x-40f*Main.ratioSpace, y-40f*Main.ratioSpace, x+40f*Main.ratioSpace, y+40f*Main.ratioSpace,4*r,0f,5*r,r);
				}else if(this.animationState[i]>=this.animationMax*2f/6f){
					g.drawImage(im, x-40f*Main.ratioSpace, y-40f*Main.ratioSpace, x+40f*Main.ratioSpace, y+40f*Main.ratioSpace,3*r,0f,4*r,r);
				}else if(this.animationState[i]>=this.animationMax*1f/6f){
					g.drawImage(im, x-40f*Main.ratioSpace, y-40f*Main.ratioSpace, x+40f*Main.ratioSpace, y+40f*Main.ratioSpace,r,0f,2*r,r);
				}else {
					g.drawImage(im, x-40f*Main.ratioSpace, y-40f*Main.ratioSpace, x+40f*Main.ratioSpace, y+40f*Main.ratioSpace,0f,0f,r,r);
				}
			}
		}
		//g.setColor(Color.white);
		//g.draw(this.collisionBox);
		return g;
	}

	public void collision(Character c, Plateau plateau){
		if(c!=owner){
			c.setLifePoints(c.lifePoints-this.damage, plateau);
		}
	}


}
