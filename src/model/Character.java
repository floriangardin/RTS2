package model;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

public class Character extends ActionObjet{

	// General attributes
	protected Circle sightBox;

	// Group attributes
	protected Character leader;
	protected Vector<Character> group;
	protected boolean someoneStopped;

	// Equipment attributes
	protected Armor armor;
	protected RidableObjet horse;
	protected Weapon weapon;

	// About velocity
	protected float maxVelocity; 	//current maximum
	protected float weight = 0;			//weapon and armor coefficient
	protected float horseVelocity = 0; 	//horse coefficient


	public Character(Plateau p,int team,float x, float y){
		this.someoneStopped= false;
		this.team = team;
		// the maximum number of float by second
		this.maxVelocity = 250f;
		switch(team){
		case 0:
			this.color = Color.blue;
			break;
		case 1:
			this.color = Color.red;
			break;
		default:
		}
		this.p = p;
		p.addCharacterObjets(this);;
		this.collisionBox = new Circle(x,y,10f);
		this.sightBox = new Circle(x,y,100f);
		this.setXY(x, y);
		this.armor = null;
		this.horse = null;
		this.lifePoints= 10;

	}

	// Getters
	private Vector<ActionObjet> getEnnemies(){
		//TODO:
		return null;
	}
	private Vector<Objet> getObjets(){
		//TODO:
		return null;
	}
	public boolean isLeader(){
		return this.leader==this;
	}
	public boolean isMobile(){
		return vx*vx+vy*vy>0.01f;
	}






	public float getWeight(){
		return this.weight;
	}


	public void stop(){
		if(this.target instanceof Checkpoint){
			this.target = null;
		}
		this.vx = 0f;
		this.vy = 0f;
		if(this.leader!=null){
			this.leader.someoneStopped=true;
		}
	}


	//// WEAPONS


	//Drop functions
	public void dropWeapon(){
		if(this.weapon != null){
			this.weapon.setOwner(null);
			this.setWeapon(null);
		}
	}
	public void dropArmor(){
		if(this.armor!=null){
			this.armor.setOwner(null);
			this.setArmor(null);
		}
	}
	public void dropHorse(){
		if(this.horse!=null){
			this.horse.setOwner(null);
			this.setHorse(null);
		}
	}
	//Collect functions
	public void collectWeapon(Weapon weapon){
		this.dropWeapon();
		this.setWeapon(weapon);
		weapon.setOwner(this);
	}
	public void collectArmor(Armor armor){
		this.dropArmor();
		this.setArmor(armor);
		armor.setOwner(this);
	}
	public void collectHorse(RidableObjet horse){
		this.dropHorse();
		this.setHorse(horse);
		horse.setOwner(this);
	}
	//Get functions
	public Weapon getWeapon() {
		return weapon;
	}
	public Armor getArmor() {
		return armor;
	}
	public RidableObjet getHorse() {
		return horse;
	}
	//Set functions
	public void setWeapon(Weapon weapon) {
		if(weapon!=null)
			this.weight += weapon.weight;
		else if (this.weapon!=null)
			this.weight -= this.weapon.weight;
		this.weapon = weapon;
		this.updateVelocity();
	}
	public void setArmor(Armor armor) {
		if(armor!=null)
			this.weight += armor.weight;
		else if (this.armor!=null)
			this.weight -= this.armor.weight;
		this.armor = armor;
		this.updateVelocity();
	}
	public void setHorse(RidableObjet horse) {
		if(horse!=null)
			this.horseVelocity = horse.velocity;
		else if (this.horse!=null)
			this.horseVelocity = 0f;
		this.horse = horse;
		this.updateVelocity();
	}
	//Update functions
	public void updateVelocity(){
		float v = 1f;
		if(this.horse!=null)
			v = v * this.horseVelocity;
		v = v/(1f+weight);
		this.maxVelocity = v;
	}


	//// ACTION METHODS


	// Main method called on every time loop
	// define the behavior of the character according to the attributes
	public void action(){
		if(someoneStopped && this.isLeader() && this.group!=null){
			for(Character c : this.group){
				c.stop();
				c.leader = null;
			}
			someoneStopped = false;
			this.group = null;
		}
		if(target instanceof Weapon && Utils.distance(this,target)<2f*this.collisionBox.getBoundingCircleRadius()){
			this.collectWeapon((Weapon)target);
			this.target = new Checkpoint(target.getX(),target.getY());
			this.stop();
		}
		if(this.weapon==null){
			// If the character has no weapon it always goes toward the target
			move();
		} else{
			// If the character has a weapon it goes toward the target till it is at range
			if(!this.target.collisionBox.intersects(this.weapon.collisionBox)){
				move();
			}
		}

	}
	// Movement method
	// the character move toward its target
	public void move(){
		if(this.target==null){
			return;
		}
		float accx,accy;
		accx = this.target.getX()-this.getX();
		accy = this.target.getY()-this.getY();
		//Creating the norm of the acceleration and the new velocities among x and y
		float accNorm = (float) Math.sqrt(accx*accx+accy*accy);
		float maxVNorm = this.maxVelocity/((float)this.p.constants.FRAMERATE);
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
		float vNorm = (float) Math.sqrt(newvx*newvx+newvy*newvy);
		if(vNorm>maxVNorm*maxVNorm){
			//if the velocity is too large it is reduced to the maxVelocity value
			newvx = newvx*maxVNorm/vNorm;
			newvy = newvy*maxVNorm/vNorm;
		} else if(accNorm<5.0f && vNorm<2.0f || this.collisionBox.intersects(target.collisionBox)){
			//if the velocity is small and the acceleration against it
			//the point needs to be stopped
			this.stop();
		}
		vNorm = (float) Math.sqrt(newvx*newvx+newvy*newvy);
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


	//// GRAPHISMS


	public Graphics draw(Graphics g){
		g.setColor(this.color);
		g.fill(collisionBox);
		return g;
	}
	public void drawIsSelected(Graphics g){
		g.setColor(Color.green);
		g.draw(new Circle(this.getX(),this.getY(),((Circle)this.collisionBox).radius+10f));
	}	


	//// AUXILIARY FUNCTIONS


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
		case 2:	newY = o.collisionBox.getMaxY()+this.collisionBox.getBoundingCircleRadius();
		break;
		case 3: newX = o.collisionBox.getMinX()-this.collisionBox.getBoundingCircleRadius();
		break;
		case 4: newY = o.collisionBox.getMinY()-this.collisionBox.getBoundingCircleRadius();
		break;
		default:
		}
		float x0,y0,x1,y1,x2,y2;
		x1 = this.getX();
		y1 = this.getY();
		x0 = x1 - this.vx;
		y0 = y1 - this.vy;
		x2 = newX;
		y2 = newY;
		float finalX=newX, finalY=newY;
		switch(Math.floorMod(sector, 2)){
		case 0: 
			float ya,yb;
			ya = y0+(float)Math.sqrt(vx*vx+vy*vy-(x2-x0)*(x2-x0));
			yb = y0-(float)Math.sqrt(vx*vx+vy*vy-(x2-x0)*(x2-x0));
			if ( Math.abs(ya-y2)<Math.abs(yb-y2)){
				finalY = ya;
				finalX = x2;
			} else {
				finalY = yb;
				finalX = x2;
			}
			this.vx = finalX-x0;
			this.vy = 0f;
			break;
		case 1:
			float xa,xb;
			xa = x0+(float)Math.sqrt(vx*vx+vy*vy-(y2-y0)*(y2-y0));
			xb = x0-(float)Math.sqrt(vx*vx+vy*vy-(y2-y0)*(y2-y0));
			if ( Math.abs(xa-x2)<Math.abs(xb-x2)){
				finalX = xa;
				finalY = y2;
			} else {
				finalX = xb;
				finalY = y2;
			}
			this.vy = finalY-y0;
			this.vx = 0f;
			break;
		default:
		}

		this.setXY(finalX, finalY);

	}


}

