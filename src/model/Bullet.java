package model;


import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

public abstract class Bullet extends ActionObjet {
	protected float damage;
	Circle areaEffect;
	Character owner;
	private Vector<Character> getCharactersInAreaEffect(){
		return null;
	}
	
	public Graphics draw(Graphics g){
		g.setColor(Color.black);
		g.fill(this.collisionBox);
		return g;
	}
	
	
	public void action(){
		this.setXY(this.getX()+this.vx, this.getY()+this.vy);
	}

}
