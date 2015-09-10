package spells;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;

import model.ActionObjet;
import model.Plateau;

public class Firewall extends ActionObjet{
	
	public float x1,x2,y1,y2;
	public float remainingTime;
	public float damage;
	public Shape collisionBox;
	public float animation;
	public float animationMax = 120f;
	
	public Firewall(Plateau p){
		this.id = p.g.idChar;
		p.g.idChar+=1;
		this.lifePoints = 1f;
		this.p.addSpell(this);
	}
	
	public void action(){
		this.remainingTime-=1f;
		this.animation+=1f;
		if(animation>animationMax)
			animation = 0f;
		if(this.remainingTime<=0f)
			this.lifePoints = -1f;
	}
	
	public Graphics draw(Graphics g){
		g.setColor(Color.white);
		g.draw(this.collisionBox);
		return g;
	}

}
