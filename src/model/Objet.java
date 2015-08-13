package model;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public abstract class Objet {
	Image selection_circle;
	protected Sound sound;
	protected float x;
	protected float y;
	protected Shape collisionBox;
	protected Color color;
	protected Plateau p;
	protected float lifePoints;
	protected String name;
	protected int team;
	protected Image image;
	
	public void setName(String s){
		this.name = s;
	}
	public String getName(){
		return this.name;
	}
	protected void destroy(){
		this.lifePoints = -10;
		
		this.x = -10f;
		this.y = -10f;
	}
	public Graphics draw(Graphics g){
		return g;}
	protected void collision(Objet o){}
	public void collision(Character c){}
	protected float getX(){
		return x;
	}
	protected float getY(){
		return y;
	}
	protected void setXY(float x, float y){
		this.x = x;
		this.y = y;
		this.collisionBox.setCenterX(x);
		this.collisionBox.setCenterY(y);
	}
	
	protected boolean isAlive(){
		return this.lifePoints>0f;
	}

}

