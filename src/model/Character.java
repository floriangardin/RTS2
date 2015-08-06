package model;


import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Ellipse;
import org.newdawn.slick.geom.Line;

public class Character extends ActionObjet{

	// General attributes
	protected Circle sightBox;
	protected float maxLifePoints;
	protected Vector<Objet> secondaryTargets = new Vector<Objet>();
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
	protected float basicVelocity;
	protected float weight = 0;			//weapon and armor coefficient
	protected float horseVelocity = 0; 	//horse coefficient

	// About drawing
	protected float animationValue=0f;
	protected int orientation=2;
	// value = [2,4,6,8] according to the numeric pad

	public Character(Plateau p,int team,float x, float y){
		// Parameters
		this.basicVelocity = 70f;
		float size = 15f;
		float sight = 100f;
		this.maxLifePoints = 100f;
		 
		//
		
		try{
			Image imagea = new Image("pics/Corps.png");
			Image imageb = new Image("pics/Armure.png");
			if(team==0)
				imageb = new Image("pics/Blue.png");
			if(team==1)
				imageb = new Image("pics/Red.png");
			this.image = Utils.mergeImages(imagea, imageb);

		} catch (SlickException e) {
			e.printStackTrace();
		}
		this.team = team;
		// the maximum number of float by second
		this.maxVelocity = this.basicVelocity;
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
		this.collisionBox = new Circle(x,y,size);
		this.sightBox = new Circle(x,y,sight);
		this.setXY(x, y);
		this.armor = null;
		this.horse = null;
		this.lifePoints= this.maxLifePoints;
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

	protected void setXY(float x, float y){
		this.x = x;
		this.y = y ;
		this.collisionBox.setCenterX(this.x);
		this.collisionBox.setCenterY(this.y);
		this.sightBox.setCenterX(this.getX());
		this.sightBox.setCenterY(this.getY());
	}
	protected void setVXVY(float vx, float vy){
		this.vx = vx;
		this.vy = vy;
		int sector = 0;
		if(vx>0f){
			if(vy>vx){
				sector = 2;
			} else if(vy<-vx){
				sector = 8;
			} else {
				sector = 6;
			}
		} else {
			if(vy>-vx){
				sector = 2;
			} else if(vy<vx){
				sector = 8;
			} else {
				sector = 4;
			}
		}
		this.orientation = sector;
	}

	public float getWeight(){
		return this.weight;
	}


	public void stop(){
		if(this.getTarget() instanceof Checkpoint){
			if(this.secondaryTargets.size()==0){
				this.setTarget(null);
			}else{
				this.setTarget(this.secondaryTargets.firstElement());
				this.secondaryTargets.remove(0);
				return;
			}

		}
		this.vx = 0f;
		this.vy = 0f;
		if(!this.isLeader() && this.leader!=null && this.leader.isMobile()){
			this.leader.stop();
		}
		if(this.isLeader()){
			for(Character c : this.group){
				if(!c.isLeader() && c.isMobile()){
					c.stop();
				}
				c.leader = null;
			}
			this.group = null;
		}
		this.animationValue=0f;
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
		else
			this.weight -= this.weapon.weight;
		this.weapon = weapon;
		this.updateVelocity();
		this.updateImage();
	}
	public void setArmor(Armor armor) {
		if(armor!=null)
			this.weight += armor.weight;
		else 
			this.weight -= this.armor.weight;
		this.armor = armor;
		this.updateVelocity();
		this.updateImage();
	}
	public void setHorse(RidableObjet horse) {
		if(horse!=null)
			this.horseVelocity = horse.velocity;
		else if (this.horse!=null)
			this.horseVelocity = 0f;
		this.horse = horse;
		this.updateVelocity();
		this.updateImage();
	}
	//Update functions
	public void updateVelocity(){
		float v = 1f;
		if(this.horse!=null)
			v = v * this.horse.velocity;
		v = v/(1f+this.weight);

		this.maxVelocity = this.basicVelocity*v;
	}
	public void updateImage(){
		try{
			//Handling the team
			Image imagea = new Image("pics/Corps.png");
			Image imageb = new Image("pics/Armure.png");
			Image imagec = new Image("pics/HorseBlue.png");
			if(team==0){
				imageb = new Image("pics/Blue.png");
				imagec = new Image("pics/HorseBlue.png");
			}
			if(team==1){
				imageb = new Image("pics/Red.png");
				imagec = new Image("pics/HorseRed.png");
			}
			this.image = Utils.mergeImages(imagea, imageb);
			//Handling the armor
			if(this.armor!=null){
				if(this.armor instanceof LightArmor)
					imageb = new Image("pics/LightArmor.png");
				else if(this.armor instanceof MediumArmor)
					imageb = new Image("pics/MediumArmor.png");
				else if(this.armor instanceof HeavyArmor)
					imageb = new Image("pics/HeavyArmor.png");
				this.image = Utils.mergeImages(this.image, imageb);
			}
			//Handling the weapon
			if(this.weapon!=null){
				if(this.weapon instanceof Sword)
					imageb = new Image("pics/Sword.png");
				if(this.weapon instanceof Bow)
					imageb = new Image("pics/Bow.png");
				if(this.weapon instanceof Bible)
					imageb = new Image("pics/Bible.png");
				if(this.weapon instanceof Balista)
					imageb = new Image("pics/Magicwand.png");
				this.image = Utils.mergeImages(this.image, imageb);
			}

			//Handling the horse
			if(this.horse!=null){
				this.image = Utils.mergeHorse(imagec, this.image);
			}
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	//// ACTION METHODS

	// Main method called on every time loop
	// define the behavior of the character according to the attributes
	public void action(){
		if(!this.isAlive()){
			this.setTarget(null);
			return;
		}
		if(this.getTarget()==null){
			Vector<Objet> potential_targets;
			if(this.weapon!=null && this.weapon.damage>0) 
				potential_targets = p.getEnnemiesInSight(this);
			else if (this.weapon!=null && this.weapon.damage<0) 
				potential_targets = p.getWoundedAlliesInSight(this);
			else
				potential_targets = new Vector<Objet>();
			if(potential_targets.size()>0){
				//Take the nearest target :
				this.setTarget(Utils.nearestObject(potential_targets, this));
			}
			if(this.getTarget()==null){
				if(this.checkpointTarget!=null){
					this.setTarget(this.checkpointTarget);
					move();
					this.setTarget(null);
				}
				return;
			}
		}
		if(!this.getTarget().isAlive() ){
			this.setTarget(null);
			// Now require a new target if possible
			Vector<Objet> potential_targets = p.getEnnemiesInSight(this);
			if(potential_targets.size()>0){
				//Take the nearest target :
				this.setTarget(Utils.nearestObject(potential_targets, this));
			}
		}
		else if(this.getTarget() instanceof Character){
			Character c =(Character) this.getTarget();
			if(c.team!=this.team && !this.sightBox.intersects(this.getTarget().collisionBox)){
				this.setTarget(null);
			}
			Vector<Objet> potential_targets = p.getEnnemiesInSight(this);
			if(potential_targets.size()>0){
				//Take the nearest target :
				this.setTarget(Utils.nearestObject(potential_targets, this));
			}
		}
		if(someoneStopped && this.isLeader() && this.group!=null){
			this.stop();
		}
		if(this.getTarget() instanceof Weapon && Utils.distance(this,this.getTarget())<2f*this.collisionBox.getBoundingCircleRadius()){
			this.collectWeapon((Weapon)this.getTarget());
			this.setTarget(new Checkpoint(this.getTarget().getX(),this.getTarget().getY()));
			this.stop();
		}
		if(this.weapon==null){
			// If the character has no weapon it always goes toward the target
			move();
		} else{
			// If the character has a weapon it goes toward the target till it is at range
			if(this.getTarget()!=null && !this.getTarget().collisionBox.intersects(this.weapon.collisionBox)){
				move();
			}
			else{
				this.stop();
				return;
			}
		}

	}
	// Movement method
	// the character move toward its target
	public void move(){
		if(this.getTarget()==null){
			return;
		}
		float accx,accy;
		accx = this.getTarget().getX()-this.getX();
		accy = this.getTarget().getY()-this.getY();
		//Creating the norm of the acceleration and the new velocities among x and y
		float accNorm = (float) Math.sqrt(accx*accx+accy*accy);
		float maxVNorm = this.maxVelocity/((float)this.p.constants.FRAMERATE);
		float ACC = this.p.constants.ACC/((float)this.p.constants.FRAMERATE);
		float newvx, newvy;
		//Checking if the point is not too close of the target
		if(accNorm<1.0f){
			//if so deleting the acceleration (for now)
			//TODO: handle stopping
			newvx = 0f;
			newvy = 0f;
			this.stop();
			return;
		} else {
			accx = accx*ACC/(accNorm);
			accy = accy*ACC/(accNorm);
			newvx = vx + accx;
			newvy = vy + accy;
		}
		float vNorm = (float) Math.sqrt(newvx*newvx+newvy*newvy);
		if(vNorm>maxVNorm*maxVNorm){
			//if the velocity is too large it is reduced to the maxVelocity value
			newvx = newvx*maxVNorm/vNorm;
			newvy = newvy*maxVNorm/vNorm;
		} else if(accNorm<5.0f && vNorm<2.0f || this.collisionBox.intersects(this.getTarget().collisionBox)){
			//if the velocity is small and the acceleration against it
			//the point needs to be stopped
			this.stop();
			return;
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
		this.setVXVY(newvx, newvy);

		this.setXY(newX, newY);
		this.animationValue+=4f/(float)this.p.constants.FRAMERATE;
		if(this.animationValue>=4f){
			this.animationValue = 0f;
		}
	}


	//// GRAPHISMS


	public Graphics draw(Graphics g){
		float r = collisionBox.getBoundingCircleRadius();
		float animation = 1f,direction = 0f;
		if(this.animationValue<1f || (this.animationValue>=2f && this.animationValue<3f))
			animation = 1f;
		else if(this.animationValue>=1f && this.animationValue<2f)
			animation = 0f;
		else
			animation = 2f;
		direction = (float)(orientation/2-1);
		int imageWidth = this.image.getWidth()/3;
		int imageHeight = this.image.getHeight()/4;
		float drawWidth = r*imageWidth/Math.min(imageWidth,imageHeight);
		float drawHeight = r*imageHeight/Math.min(imageWidth,imageHeight);
		float x1 = this.getX() - drawWidth;
		float y1 = this.getY() + drawWidth - 2*drawHeight;
		float x2 = this.getX() + drawWidth;
		float y2 = this.getY() + drawWidth;
		g.drawImage(this.image,x1,y1,x2,y2,imageWidth*animation,imageHeight*direction,imageWidth*animation+imageWidth,imageHeight*direction+imageHeight);
		// Drawing the health bar
		if(this.lifePoints<this.maxLifePoints){
			g.setColor(Color.red);
			g.draw(new Line(this.getX()-r,this.getY()-r,this.getX()+r,this.getY()-r));
			float x = this.lifePoints*2f*r/this.maxLifePoints;
			g.setColor(Color.green);
			g.draw(new Line(this.getX()-r,this.getY()-r,this.getX()-r+x,this.getY()-r));
		}
		return g;
	}
	public void drawIsSelected(Graphics g){
		float r = this.collisionBox.getBoundingCircleRadius();
		g.setColor(Color.green);
		if(this.horse!=null){
			g.draw(new Ellipse(this.getX(),this.getY()+4f*r/6f,r+3f,r-3f));
		} else {
			g.draw(new Ellipse(this.getX(),this.getY()+4f*r/6f,r,r-5f));
		}
	}	


	//// AUXILIARY FUNCTIONS


	// Collision with other Characters
	public void collision(Character o) {
		// If collision test who have the highest velocity
		// The highest velocity continues 
		// The lowest velocity move away ( he is pushed instead of the other ) 

		// set the tolerance for collision:
		//   - 0: collision is totally authorized
		//   - 1: no collision but clipping
		float toleranceCollision = 0.1f;
		// get the mediatrice of both object
		float y_med = this.getX()-o.getX();
		float x_med = o.getY()-this.getY();
		float norm = (x_med*x_med+y_med*y_med);
		float r = this.collisionBox.getBoundingCircleRadius();
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
			this.setXY(this.getX()-0.5f*r*x_med,this.getY()-0.5f*r*y_med);
		}
		else{

			this.setXY(this.getX()+toleranceCollision*(this.getX()-o.getX()),this.getY()+toleranceCollision*(this.getY()-o.getY()));
		}
		//this.move(this.vx+this.x,this.vy+this.y );
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
		// If the point is in the corner the treatment is special
		//		if(Utils.distance(o, this)-this.collisionBox.getBoundingCircleRadius()>0.99f*o.collisionBox.getBoundingCircleRadius()){
		//			float r, r0;
		//			r = this.collisionBox.getBoundingCircleRadius();
		//			r0 = o.collisionBox.getBoundingCircleRadius();
		//			if(oX>x){
		//				if(oY<y){
		//					this.setXY(oX-r0-r, y+r+r0);
		//				} else {
		//					this.setXY(oX-r0-r, y-r-r0);
		//				}
		//			} else {
		//				if(oY<y){
		//					this.setXY(oX+r0+r, y+r+r0);
		//				} else {
		//					this.setXY(oX+r0+r, y-r-r0);
		//				}
		//			}
		//		}
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
			if(Float.isNaN(finalX))
				finalX = this.getX();
			if(Float.isNaN(finalY))
				finalY = this.getY();
			this.setVXVY(finalX-x0, 0f);
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
			if(Float.isNaN(finalX))
				finalX = this.getX();
			if(Float.isNaN(finalY))
				finalY = this.getY();
			this.setVXVY(0f, finalY-y0);
			break;
		default:
		}
		this.setXY(finalX, finalY);

	}


}

