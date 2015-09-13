package model;


import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Shape;

import bullets.Arrow;
import units.Character;

public abstract class Objet {
	public Image selection_circle;
	public Sound sound;
	public float x;
	public float y;
	public float sight;
	public Shape collisionBox;
	public Color color;
	public Plateau p;
	public float lifePoints;
	public String name;
	public int team;
	public boolean visibleByCurrentPlayer;
	
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
	public void collision(Arrow b){}
	public float getX(){
		return x;
	}
	public float getY(){
		return y;
	}
	public void setXY(float x, float y){
		this.x = x;
		this.y = y;
		this.collisionBox.setCenterX(x);
		this.collisionBox.setCenterY(y);
	}
	
	public boolean isAlive(){
		return this.lifePoints>0f;
	}

}

