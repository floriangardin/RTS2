package spells;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Polygon;

import model.Checkpoint;
import model.Objet;
import model.Plateau;
import model.Utils;
import multiplaying.OutputModel.OutputSpell;
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
	public float x,y,x2,y2;

	public Firewall(Plateau p, Character launcher, Objet t){
		this.id = p.g.idChar;
		this.x = launcher.getX();
		this.y = launcher.getY();
		this.x2 = t.getX();
		this.y2 = t.getY();
		float width = 15f;
		p.g.idChar+=1;
		this.lifePoints = 1f;
		p.addSpell(this);
		image = p.images.explosion;
		owner = launcher;
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
		this.collisionBox = new Polygon(arg);
		this.createAnimation(t, launcher);
	}

	public Firewall(Plateau p, OutputSpell s){
		float x = s.x1, y = s.y1, x2 = s.x2, y2 = s.y2;
		this.id = s.id;
		float width = 15f;
		this.lifePoints = 1f;
		p.addSpell(this);
		image = p.images.explosion;
		float vx = y2-y;
		float vy = x-x2;
		float norm = (float)Math.sqrt(vx*vx+vy*vy);
		vx = vx/norm;
		vy = vy/norm;
		float ax,ay,bx,by,cx,cy,dx,dy;
		ax = x+vx*width/2f;
		ay = y+vy*width/2f;
		bx = x-vx*width/2f;
		by = y-vy*width/2f;
		dx = x2+vx*width/2f;
		dy = y2+vy*width/2f;
		cx = x2-vx*width/2f;
		cy = y2-vy*width/2f;
		float[] arg = {ax,ay,bx,by,cx,cy,dx,dy};
		this.collisionBox = new Polygon(arg);
		this.createAnimation(new Checkpoint(x,y), new Checkpoint(x2,y2));
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
		if(this.remainingTime<=0f)
			this.lifePoints = -1f;
	}

	public Graphics draw(Graphics g){
		int j = (int)(Math.random()*nbFire*5);
		if(j<nbFire){
			if(this.animationState[j]==0f)
				this.animationState[j]=1f;
		}
		for(int k=0; k<nbFire;k++){
			if(this.animationState[k]>0f)
				this.animationState[k]+=1f;
			if(this.animationState[k]>animationMax)
				this.animationState[k] = 0f;
		}
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