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
		// the maximum number of float by second
		this.maxVelocity = 500f;
		this.color = Color.blue;
		this.p = p;
		p.addCharacterObjets(this);;
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
		if(this.target==null){
			return;
		}
		float accx,accy;
		accx = this.target.getX()-this.getX();
		accy = this.target.getY()-this.getY();
		//Creating the norm of the acceleration and the new velocities among x and y
		float accNorm = (float) Math.sqrt(accx*accx+accy*accy);
		float newvx, newvy;
		//Checking if the point is not too close of the target
		if(accNorm<1.0f){
			//if so deleting the acceleration (for now)
			//TODO: handle stopping
			newvx = 0f;
			newvy = 0f;
		} else {
			accx = accx*this.p.constants.ACC/(accNorm*this.p.constants.FRAMERATE);
			accy = accy*this.p.constants.ACC/(accNorm*this.p.constants.FRAMERATE);
			newvx = vx + accx;
			newvy = vy + accy;
		}
		float maxVNorm = this.maxVelocity/((float)this.p.constants.FRAMERATE);
		float vNorm = newvx*newvx+newvy*newvy;
		if(vNorm>0.1f)
			System.out.println(vNorm+" "+maxVNorm);
		if(vNorm>maxVNorm*maxVNorm){
			//if the velocity is too large it is reduced to the maxVelocity value
			newvx = newvx*maxVNorm/vNorm;
			newvy = newvy*maxVNorm/vNorm;
		} else if(vNorm<1.0f && newvx*accx+newvy*accy<0f){
			//if the velocity is small and the acceleration against it
			//the point needs to be stopped
			newvx = 0f;
			newvy = 0f;
		}
		float newX,newY;
		newX = this.getX()+newvx;
		newY = this.getY()+newvy;
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
		g.fill(collisionBox);
		return g;
	}

	// Collision with other ActionObjets
	public void collision(ActionObjet o) {

		// If collision test who have the highest velocity
		// The highest velocity continues 
		// The lowest velocity move away ( he is pushed at the pace of the other ) 
		if(o instanceof Bullet){

		}
		else{
			// set the tolerance for collision:
			//   - 0: collision is totally authorized
			//   - 1: no collision but clipping
			float toleranceCollision = 0.1f;
			// get the mediatrice of both object
			float y_med = this.getX()-o.getX();
			float x_med = o.getY()-this.getY();
			float norm = (x_med*x_med+y_med*y_med);
			y_med = y_med/norm;
			x_med = x_med/norm;
			if(x_med*vx+y_med*vy<0){
				x_med=-x_med;
				y_med=-y_med;

			}

			if((this.vx*this.vx+this.vy*this.vy)<o.vx*o.vx+o.vy*o.vy){
				if(x_med*o.vx+y_med*o.vy<0){
					x_med=-x_med;
					y_med=-y_med;
				}
				//this.setXY(x-0.3f*o.getVx(), y-0.3f*o.getVy());
				this.setXY(this.getX()-5.5f*x_med,this.getY()-5.5f*y_med);
			}
			else{

				this.setXY(this.getX()+toleranceCollision*(this.getX()-o.getX()),this.getY()+toleranceCollision*(this.getY()-o.getY()));
			}
			//this.move(this.vx+this.x,this.vy+this.y );
		}
	}

	public void drawIsSelected(Graphics g){
		g.setColor(Color.green);
		g.draw(new Circle(this.getX(),this.getY(),((Circle)this.collisionBox).radius+10f));
	}

	// Collision with NaturalObjets
	public void collision(NaturalObjet o) {
		/*On considère pour l'instant que nos natural objets sont carrés
		 * il faut dans un premier temps déterminer de quel côté éjecter l'objet
		 * pour cela on délimite 4 secteurs:
		 * 		1: à droite
		 * 		2: en haut
		 * 		3: à gauche
		 * 		4: en bas
		 *	puis on éjecte le point au bord du côté correspondant via projection
		 */
		float oX, oY;
		oX = o.collisionBox.getCenterX();
		oY = o.collisionBox.getCenterY();
		float x, y;
		x = this.getX();
		y = this.getY();
		// Choosing the sector to eject the point
		int sector = 0;
		if(x-oX>0f){
			if(y-oY>Math.abs(x-oX)){
				sector = 2;
			} else if(y-oY<-Math.abs(x-oX)){
				sector = 4;
			} else {
				sector = 1;
			}
		} else {
			if(y-oY>Math.abs(x-oX)){
				sector = 2;
			} else if(y-oY<-Math.abs(x-oX)){
				sector = 4;
			} else {
				sector = 3;
			}
		}
		// Ejecting the point
		float newX=this.getX(),newY=this.getY();
		switch(sector){
		case 1: newX = o.collisionBox.getMaxX()+this.collisionBox.getBoundingCircleRadius();
		break;
		case 2:	newY = o.collisionBox.getMinY()-this.collisionBox.getBoundingCircleRadius();
		break;
		case 3: newX = o.collisionBox.getMinX()-this.collisionBox.getBoundingCircleRadius();
		break;
		case 4: newY = o.collisionBox.getMaxY()+this.collisionBox.getBoundingCircleRadius();
		break;
		default:
		}

		this.setXY(newX, newY);

	}


}

