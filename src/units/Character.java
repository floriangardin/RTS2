package units;


import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;

import buildings.*;
import model.ActionObjet;
import model.Checkpoint;
import model.Horse;
import model.NaturalObjet;
import model.Objet;
import model.Plateau;
import model.Player;
import model.RidableObjet;
import model.Utils;
import multiplaying.OutputModel.OutputChar;
import spells.Spell;
import weapon.*;

public class Character extends ActionObjet{


	// General attributes
	public Circle sightBox;

	public float armor = 0f;	
	public float size = 20f;
	public float maxVelocity = 100f;
	public float range;
	public float damage;
	public float chargeTime;

	// Group attributes
	public Character leader;
	public Vector<Character> group;
	public boolean someoneStopped;
	// Equipment attributes
	public RidableObjet horse;
	public Weapon weapon;
	public int typeWeapon, typeHorse;
	public Player player;
	// About drawing
	public float animationValue=0f;
	public int animation = 0;
	public int orientation=2;
	// value = [2,4,6,8] according to the numeric pad
	// Spells ( what should appear in the bottom bar
	public Vector<Spell> spells = new Vector<Spell>();
	public Vector<Float> spellsState = new Vector<Float>();
	// Invisibility 
	boolean isHidden;
	protected int civ ;
	// Special Abilities
	public boolean isImmolating = false;
	public float remainingTime;



	public Vector<Objet> secondaryTargets = new Vector<Objet>();

	// Constructor for data ( not adding in plateau not giving location)
	public Character(Plateau p,Player player){
		this.p = p;
		this.player = player;
		this.team = player.team;
		this.name = "character";
		this.selection_circle = this.p.images.selection_circle;
		Image imagea = this.p.images.corps;
		Image imageb = this.p.images.corps;
		if(team==1)
			imageb = this.p.images.blue;
		if(team==2)
			imageb = this.p.images.red;
		this.image = Utils.mergeImages(imagea, imageb);
		this.size = 20f;
		this.isHidden = false;
		this.spells = new Vector<Spell>();

	}
	// Copy constructor , to really create an unit
	public Character(Character c,float x,float y){
		this.p = c.p;
		this.size = c.size;
		p.addCharacterObjets(this);
		this.id = p.g.idChar;
		p.g.idChar+=1;
		this.name = c.name;
		this.team = c.team;
		this.damage = c.damage;
		this.maxLifePoints = c.maxLifePoints;
		this.lifePoints = c.maxLifePoints;
		this.sight = c.sight;
		this.collisionBox = new Circle(c.collisionBox.getCenterX(),c.collisionBox.getCenterY(),c.collisionBox.getBoundingCircleRadius());
		this.sightBox = new Circle(c.sightBox.getCenterX(),c.sightBox.getCenterY(),c.sightBox.getBoundingCircleRadius());

		this.setXY(x, y);
		this.maxVelocity = c.maxVelocity;
		this.armor = c.armor;
		this.range = c.range;
		this.chargeTime = c.chargeTime;
		this.isHidden = c.isHidden;
		this.image = c.image;
		this.selection_circle = c.selection_circle;
		this.horse = c.horse;
		
		this.group = new Vector<Character>();
		this.group.add(this);
		
		for(Spell s:c.spells){
			this.spells.addElement(s);
			this.spellsState.addElement(0f);
		}
		
		// COpy weapon
		switch(c.weapon.name){
		case "bow":
			this.weapon = new Bow(this.p,this);
			break;
		case "sword":
			this.weapon = new Sword(this.p,this);
			break;
		case "spear":
			this.weapon = new Spear(this.p,this);
			break;
		case "wand":
			this.weapon = new Wand(this.p,this);
			break;
		case "bible":
			this.weapon = new Bible(this.p,this);
			break;
		default:

		}

	}
	public Character(OutputChar occ, Plateau p){
		// Only used to display on client screen
		// Parameters
		this.name = occ.name;
		this.p = p;
		this.player = this.p.g.players.get(team);
		this.player.create(UnitsList.switchName(occ.name),occ.x,occ.y);
		this.id = occ.id;
	}

	public boolean isLeader(){
		return this.leader==this;
	}
	public boolean isMobile(){
		return vx*vx+vy*vy>0.01f;
	}
	public int getId(){
		return id;
	}
	public float getArmor(){
		return this.armor;
	}
	public void setXY(float x, float y){
		this.x = x;
		this.y = y ;
		this.collisionBox.setCenterX(this.x);
		this.collisionBox.setCenterY(this.y);
		this.sightBox.setCenterX(this.getX());
		this.sightBox.setCenterY(this.getY());
	}
	public void setVXVY(float vx, float vy){
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





	//// WEAPONS


	//Drop functions
	public void dropWeapon(){
		if(this.weapon != null){
			this.weapon.setOwner(null);
			this.setWeapon(null);
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
	public void collectHorse(RidableObjet horse){
		this.dropHorse();
		this.setHorse(horse);
		horse.setOwner(this);
	}
	//Get functions
	public Weapon getWeapon() {
		return weapon;
	}
	public RidableObjet getHorse() {
		return horse;
	}
	//Set functions
	public void setWeapon(Weapon weapon) {
		if(weapon!=null){
			if(weapon instanceof Sword)
				this.typeWeapon = 1;
			if(weapon instanceof Bow)
				this.typeWeapon = 2;
			if(weapon instanceof Bible)
				this.typeWeapon = 3;
			if(weapon instanceof Wand)
				this.typeWeapon = 4;
		}else{
			this.typeWeapon = 0;
		}
		this.weapon = weapon;
		this.updateImage();
	}
	public void setHorse(RidableObjet horse) {
		if(horse!=null){
			this.typeHorse = 1;
		}else if (this.horse!=null){
			this.typeHorse = 0;
		}
		this.horse = horse;
		this.updateImage();
	}
	//Update functions
	public void updateImage(){
		//Handling the team
		Image imagea = this.p.images.corps;
		Image imageb = this.p.images.corps;
		Image imagec = this.p.images.corps;
		if(team==1){
			imageb = this.p.images.blue;
			imagec = this.p.images.horseBlue;
		}
		if(team==2){
			imageb = this.p.images.red;
			imagec = this.p.images.horseRed;
		}
		this.image = Utils.mergeImages(imagea, imageb);
		//Handling the weapon
		if(this.weapon!=null){
			if(this.weapon instanceof Sword)
				imageb = this.p.images.sword;
			if(this.weapon instanceof Bow)
				imageb = this.p.images.bow;
			if(this.weapon instanceof Bible)
				imageb = this.p.images.bible;
			if(this.weapon instanceof Wand)
				imageb = this.p.images.magicwand;
			this.image = Utils.mergeImages(this.image, imageb);
		}

		//Handling the horse
		if(this.horse!=null){
			this.image = Utils.mergeHorse(imagec, this.image);
		}
	}


	//// ACTION METHODS

	// Main method called on every time loop
	// define the behavior of the character according to the attributes
	public void action(){
		// Handling the death of the unit
		if(!this.isAlive()){
			this.setTarget(null);
			return;
		}
		// Handling the immolation
		if(this.isImmolating){
			this.lifePoints=this.maxLifePoints;
			this.remainingTime-=1f;
			if(this.remainingTime<=0f){
				this.lifePoints=-1f;
				this.player.special+=this.player.data.gainedFaithByImmolation;
			}
			return;
		}
		for(int i=0; i<this.spells.size(); i++){
			this.spellsState.set(i,Math.min(this.spells.get(i).chargeTime, this.spellsState.get(i)+1f));
		}
		if(this.getTarget()!=null && !this.getTarget().isAlive() ){
			this.setTarget(null);
		}
		if(this.getTarget()==null && this.secondaryTargets.size()>0){
			this.setTarget(this.secondaryTargets.get(0));
		}
		if(this.getTarget()==null){
			// The character has no target, we look for a new one
			Vector<Objet> potential_targets;
			if(this.weapon!=null && this.weapon.damage>0f) 
				potential_targets = p.getEnnemiesInSight(this);
			else if (this.weapon!=null && this.weapon.damage<0f) 
				potential_targets = p.getWoundedAlliesInSight(this);
			else
				potential_targets = new Vector<Objet>();
			if(potential_targets.size()>0){
				this.setTarget(Utils.nearestObject(potential_targets, this));
			} else {
				this.setTarget(this.checkpointTarget);
			}
			//			move();
			//			this.setTarget(null);
			//			return;
		}
		if(this.getTarget() instanceof Character){
			Character c =(Character) this.getTarget();
			if(c.team!=this.team && !this.sightBox.intersects(this.getTarget().collisionBox)){
				//TODO : create a boolean isVisible and update it
				this.setTarget(new Checkpoint(this.getTarget().x,this.getTarget().y));
			}
		}
		if(this.getTarget()!=null && (this.getTarget() instanceof Checkpoint || !this.getTarget().collisionBox.intersects(this.weapon.collisionBox))){
			this.move();
		}else{
			this.stop();
			// The target is at range, the character don't move
		}


	}
	// Movement method
	// the character move toward its target
	public void move(){
		if(this.getTarget()==null && this.checkpointTarget==null){
			return;
		}
		float accx,accy;
		accx = this.getTarget().getX()-this.getX();
		accy = this.getTarget().getY()-this.getY();
		//Creating the norm of the acceleration and the new velocities among x and y
		float accNorm = (float) Math.sqrt(accx*accx+accy*accy);
		float maxVNorm = this.maxVelocity/((float)this.p.g.players.get(team).data.FRAMERATE);
		float ACC = this.p.g.players.get(team).data.ACC/((float)this.p.g.players.get(team).data.FRAMERATE);
		float newvx, newvy;
		//Checking if the point is not too close of the target
		if(accNorm<1.0f){
			// 1st possible call of stop: the target is near
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
			// 2nd possible call: the target is near and the character is going backward
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
		this.animationValue+=4f/(float)this.p.g.players.get(team).data.FRAMERATE;
		if(this.animationValue>=4f){
			this.animationValue = 0f;
		}

		if(animationValue!=0f){
			if(this.animationValue<1f || (this.animationValue>=2f && this.animationValue<3f))
				animation = 1;
			else if(this.animationValue>=1f && this.animationValue<2f)
				animation = 0;
			else
				animation = 2;
		}
		if(this.group!=null){
			// Handling the group deplacement
			boolean nextToStop = false;
			boolean oneHasArrived = false;
			for(Character c: this.group){
				if(c!=this && !c.isMobile() && Utils.distance(c, this)<this.collisionBox.getBoundingCircleRadius()+c.collisionBox.getBoundingCircleRadius()+2f)
					nextToStop = true;
				if(Utils.distance(c, this.getTarget())< c.collisionBox.getBoundingCircleRadius()+2f)
					oneHasArrived = true;
			}
			if(nextToStop && oneHasArrived){
				this.stop();
				return;
			}
		}
	}
	// Stopping method
	// 
	public void stop(){
		this.checkpointTarget = null;
		if(this.getTarget() instanceof Checkpoint){
			if(this.secondaryTargets.size()==0){
				this.setTarget(null);
				this.animationValue=0f;
			}else{
				this.setTarget(this.secondaryTargets.firstElement());
				this.secondaryTargets.remove(0);
				return;
			}

		}
		this.vx = 0f;
		this.vy = 0f;
	}

	//// GRAPHISMS

	public Graphics draw(Graphics g){
		float r = collisionBox.getBoundingCircleRadius();
		float direction = 0f;
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
		if(!isImmolating && this.lifePoints<this.maxLifePoints){
			g.setColor(Color.red);
			g.draw(new Line(this.getX()-r,this.getY()-r,this.getX()+r,this.getY()-r));
			float x = this.lifePoints*2f*r/this.maxLifePoints;
			g.setColor(Color.green);
			g.draw(new Line(this.getX()-r,this.getY()-r,this.getX()-r+x,this.getY()-r));
		}
		//Draw the immolation
		if(isImmolating){
			Image fire = this.p.images.explosion;
			r = fire.getWidth()/5f;
			x = this.getX();
			y = this.getY();
			if(this.remainingTime>=65f)
				g.drawImage(fire, x-40f, y-40f, x+40f, y+40f,0f,0f,r,r);
			else if(this.remainingTime>=55f)
				g.drawImage(fire, x-40f, y-40f, x+40f, y+40f,r,0f,2*r,r);
			else if(this.remainingTime>=45f)
				g.drawImage(fire, x-40f, y-40f, x+40f, y+40f,2*r,0f,3*r,r);
			else if(this.remainingTime>=40f)
				g.drawImage(fire, x-40f, y-40f, x+40f, y+40f,3*r,0f,4*r,r);
			else if(this.remainingTime>=35f)
				g.drawImage(fire, x-40f, y-40f, x+40f, y+40f,4*r,0f,5*r,r);
			else if(this.remainingTime>=40f)
				g.drawImage(fire, x-40f, y-40f, x+40f, y+40f,3*r,0f,4*r,r);
			else if(this.remainingTime>=35f)
				g.drawImage(fire, x-40f, y-40f, x+40f, y+40f,4*r,0f,3*r,r);
			else if(this.remainingTime>=30f)
				g.drawImage(fire, x-40f, y-40f, x+40f, y+40f,3*r,0f,4*r,r);
			else if(this.remainingTime>=25f)
				g.drawImage(fire, x-40f, y-40f, x+40f, y+40f,4*r,0f,5*r,r);
			else if(this.remainingTime>=20f)
				g.drawImage(fire, x-40f, y-40f, x+40f, y+40f,3*r,0f,4*r,r);
			else if(this.remainingTime>=15f)
				g.drawImage(fire, x-40f, y-40f, x+40f, y+40f,4*r,0f,3*r,r);
			else if(this.remainingTime>=10f)
				g.drawImage(fire, x-40f, y-40f, x+40f, y+40f,3*r,0f,4*r,r);
			else if(this.remainingTime>=5f)
				g.drawImage(fire, x-40f, y-40f, x+40f, y+40f,4*r,0f,5*r,r);
			else 
				g.drawImage(fire, x-40f, y-40f, x+40f, y+40f,3*r,0f,4*r,r);
		}
		return g;
	}
	public void drawIsSelected(Graphics g){
		g.setColor(Color.green);
		if(this.horse!=null){
			g.drawImage(this.selection_circle,-14f+this.getX()-this.collisionBox.getBoundingCircleRadius()/2f,-8f+this.getY()-this.collisionBox.getBoundingCircleRadius()/2f);

		} else {
			g.drawImage(this.selection_circle,-14f+this.getX()-this.collisionBox.getBoundingCircleRadius()/2f,-8f+this.getY()-this.collisionBox.getBoundingCircleRadius()/2f);
			//g.draw(new Ellipse(this.getX(),this.getY()+4f*r/6f,r,r-5f));
		}
	}	


	//// COLLISIONS

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
		this.collisionRect((Rectangle)o.collisionBox);
	}
	// Collision with EnemyGenerator
	public void collision(Building o) {
		this.collisionRect((Rectangle)o.collisionBox);
	}

	public void collisionRect(Rectangle o) {
		/*On consid�re pour l'instant que nos natural objets sont carr�s
		 * il faut dans un premier temps d�terminer de quel c�t� �jecter l'objet
		 * pour cela on d�limite 4 secteurs:
		 * 		1: � droite
		 * 		2: en haut
		 * 		3: � gauche
		 * 		4: en bas
		 *	puis on �jecte le point au bord du c�t� correspondant via projection
		 */
		float oX, oY;
		oX = o.getCenterX();
		oY = o.getCenterY();
		float x, y;
		x = this.getX();
		y = this.getY();
		int sector = 0;
		if(x-oX>0f){
			if(y-oY>Math.abs(x-oX)*o.getHeight()/o.getWidth()){
				sector = 2;
			} else if(y-oY<-Math.abs(x-oX)*o.getHeight()/o.getWidth()){
				sector = 4;
			} else {
				sector = 1;
			}
		} else {
			if(y-oY>Math.abs(x-oX)*o.getHeight()/o.getWidth()){
				sector = 2;
			} else if(y-oY<-Math.abs(x-oX)*o.getHeight()/o.getWidth()){
				sector = 4;
			} else {
				sector = 3;
			}
		}
		// Ejecting the point
		float newX=this.getX(),newY=this.getY();
		switch(sector){
		case 1: newX = o.getMaxX()+this.collisionBox.getBoundingCircleRadius();
		break;
		case 2:	newY = o.getMaxY()+this.collisionBox.getBoundingCircleRadius();
		break;
		case 3: newX = o.getMinX()-this.collisionBox.getBoundingCircleRadius();
		break;
		case 4: newY = o.getMinY()-this.collisionBox.getBoundingCircleRadius();
		break;
		default:
		}
		float finalX=newX, finalY=newY;
		if(Math.abs(vx)+Math.abs(vy)>0.1f){
			float x0,y0,x1,y1,x2,y2;
			x1 = this.getX();
			y1 = this.getY();
			x0 = x1 - this.vx;
			y0 = y1 - this.vy;
			x2 = newX;
			y2 = newY;
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
		}
		this.setXY(finalX, finalY);

	}
	
	//// Changing the team
	public void changeTeam(int newTeam){
		this.team = newTeam;
		this.player = this.p.g.players.get(newTeam);
		this.updateImage();
	}


	//// UPDATE FUNCTIONS

	// update from an outputchar
	public void change(OutputChar occ){
		if(occ.team!=this.team)
			this.changeTeam(occ.team);
		this.setXY(occ.x, occ.y);
		this.lifePoints = occ.lifePoints;
		this.animation = occ.animation;
		this.orientation = occ.direction;
		this.sight = occ.sight;
		this.isImmolating = (occ.isImmolating==1);
		for(int i=0; i<this.spells.size();i++)
			this.spellsState.set(i,occ.spellState[i]);
		if(occ.weaponType==this.typeWeapon && occ.horseType == this.typeHorse)
			return;
		this.changeEquipment(occ.weaponType, occ.horseType);
	}
	// update the equiments
	public void changeEquipment(int typeWeapon, int typeHorse){
		this.typeWeapon = typeWeapon;
		this.typeHorse = typeHorse;
		switch(typeWeapon){
		case 1: this.weapon = new Sword(p,this);break;
		case 2: this.weapon = new Bow(p,this);break;
		case 3: this.weapon = new Bible(p,this);break;
		case 4: this.weapon = new Wand(p,this);break;
		default:
		}
		switch(typeHorse){
		case 1: this.horse = new Horse(p,this);break;
		default:
		}
		this.updateImage();
	}




}

