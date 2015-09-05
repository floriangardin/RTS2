package model;


import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Shape;

public abstract class Objet {
	Image selection_circle;
	protected Sound sound;
	protected float x;
	protected float y;
	public float sight;
	protected Shape collisionBox;
	protected Color color;
	protected Plateau p;
	public float lifePoints;
	protected String name;
	public int team;
	
	public Image image;
	
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
	public float getX(){
		return x;
	}
	public float getY(){
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

