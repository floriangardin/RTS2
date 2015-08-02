package model;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;


public abstract class ActionObjet extends Objet{

	protected float vx;
	protected float vy;
	protected int team;
	protected Objet target;


	protected void destroy(){
		this.lifePoints = -10;
		this.target = null;
		this.x = -10f;
		this.y = -10f;
	}
	public void action(){


	}
	public void move(){

	}
	



}
