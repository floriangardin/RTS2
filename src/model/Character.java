package model;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

public class Character extends ActionObjet{
	
	
	protected Circle sightBox;
	protected Weapon weapon;
	protected Armor armor;
	protected RidableObjet horse;
	
	public Character(Plateau p,int team,float x, float y){
		this.team = team;
		this.color = Color.blue;
		this.p = p;
		this.collisionBox = new Circle(x,y,10f);
		this.sightBox = new Circle(x,y,100f);
		this.setXY(x, y);
		this.armor = null;
		this.horse = null;
		this.lifePoints= 10;
		
	}
	private Vector<ActionObjet> getEnnemies(){
		return null;
	}
	private Vector<Objet> getObjets(){
		return null;
	}
	private void chooseTarget(){
		
	}
	
	public Graphics draw(Graphics g){
		g.setColor(this.color);
		g.fill(collisionBox);
		return g;
		}
	
}
