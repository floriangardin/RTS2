package model;

import java.util.Vector;

import org.newdawn.slick.geom.Circle;

public class Character extends ActionObjet{

	protected Circle sightBox;
	protected Weapon weapon;
	protected Armor armor;
	protected RidableObjet horse;
	
	public Character(float x, float y){
		this.x = x;
		this.y = y;
		this.armor = null;
		this.horse = null;
		
	}
	private Vector<ActionObjet> getEnnemies(){
		return null;
	}
	private Vector<Objet> getObjets(){
		return null;
	}
	private void chooseTarget(){
		
	}
	public void setXY(float x,float y){
		this.x= x ;
		this.y = y ;
		this.sightBox.setCenterX(x);
		this.sightBox.setCenterX(y);
	}
	
}
