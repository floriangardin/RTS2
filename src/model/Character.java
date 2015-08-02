package model;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Point;

public class Character extends ActionObjet{


	protected Circle sightBox;
	protected Weapon weapon;
	protected Armor armor;
	protected RidableObjet horse;
	protected float maxVelocity;

	public Character(Plateau p,int team,float x, float y){
		this.team = team;
		this.maxVelocity = 1.0f;
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
	
	public void action(){
		move();
	}
	
	public void move(){
		float accx,accy;
		accx = this.target.getX()-this.getX();
		accy = this.target.getY()-this.getY();
		//Creating the norm of the acceleration and the new velocities among x and y
		float accNorm = accx*accx+accy+accy;
		float newvx, newvy;
		//Checking if the point is not too close of the target
		if(accNorm<10.0f){
			//if so deleting the acceleration (for now)
			//TODO: handle stopping
			accx = -accx;
			accy = -accy;
		}
		accx = accx*this.p.constantes.acc/accNorm;
		accy = accy*this.p.constantes.acc/accNorm;
		newvx = vx + accx*this.p.constantes.tr;
		newvy = vy + accy*this.p.constantes.tr;
		float vNorm = newvx*newvx+newvy*newvy;
		if(vNorm>maxVelocity*maxVelocity){
			//if the velocity is too large it is reduced to the maxVelocity value
			newvx = newvx*maxVelocity/vNorm;
			newvy = newvy*maxVelocity/vNorm;
		} else if(vNorm<1.0f && newvx*accx+newvy*accy<0f){
			//if the velocity is small and the acceleration against it
			//the point needs to be stopped
			newvx = 0f;
			newvy = 0f;
		}
		float newX,newY;
		newX = this.getX()+newvx*this.p.constantes.tr;
		newY = this.getY()+newvy*this.p.constantes.tr;
		//if the new coordinates are beyond the map's limits, it must be reassigned
		if(newX<this.collisionBox.getBoundingCircleRadius()){
			newX = this.collisionBox.getBoundingCircleRadius();
			newvx = Math.max(newvx,0f);
		}
		if(newY<this.collisionBox.getBoundingCircleRadius()){
			newY = this.collisionBox.getBoundingCircleRadius();
			newvy = Math.max(newvy, 0f);
		}
		if(newX>this.p.getMaxX()-this.collisionBox.getBoundingCircleRadius()){
			newX = this.p.getMaxX()-this.collisionBox.getBoundingCircleRadius();
			newvx = Math.min(0f, newvx);
		}
		if(newY>this.p.getMaxY()-this.collisionBox.getBoundingCircleRadius()){
			newY = this.p.getMaxY()-this.collisionBox.getBoundingCircleRadius();
			newvy = Math.min(0f, newvy);
		}
		
		//eventually we reassign the position and velocity variables
		this.vx = newvx;
		this.vy = newvy;
		this.setXY(newX, newY);
	}

	public boolean isMobile(){
		return vx*vx+vy*vy>0.01f;
	}
	
	public Graphics draw(Graphics g){
		g.setColor(this.color);
		g.draw(collisionBox);
		return g;
	}

}
