package units;


import java.util.HashMap;
import java.util.Vector;

import model.ActionObjet;
import model.Checkpoint;
import model.Game;
import model.GameTeam;
import model.NaturalObjet;
import model.Objet;
import model.Plateau;
import model.RidableObjet;
import model.Utils;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

import pathfinding.Case;
import spells.Spell;
import IA.IAUnit;
import buildings.Building;

public class Character extends ActionObjet{

	//Time before parsing
	public static int roundBeforeParse = 200;
	public int roundAfterBorn=0;
	// General attributes
	public Circle sightBox;
	
	public float animStep = 4f;
	public float armor = 0f;	
	public float size;
	public float maxVelocity = 100f;
	public float range;
	public float damage;

	//philippe
	public String weapon;
	//Dead since how many rounds
	public int deadSince = 0;
	public IAUnit ia;

	public boolean moveAhead;
	public float state;
	public float chargeTime;

	// Group attributes
	public Character leader;
	public Vector<Character> group;
	public boolean someoneStopped;
	// Equipment attributes
	public RidableObjet horse;

	public int typeWeapon, typeHorse;
	//public Player player;
	// About drawing
	public float animationValue=0f;

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
	// UnitsList associated
	public UnitsList type;
	public Vector<Objet> secondaryTargets = new Vector<Objet>();
	public Vector<Case> waypoints = new Vector<Case>();

	public boolean mouseHover = false;



	// Constructor for data ( not adding in plateau not giving location)
	public Character(Plateau p, GameTeam gameteam){
		this.p = p;
		this.animations = new Image[1][4][4];
		this.setTeam(gameteam);
		this.name = "character";
		this.selection_circle = this.p.g.images.selection_circle;
		Image imagea = this.p.g.images.corps;
		Image imageb = this.p.g.images.corps;
		if(getTeam()==1)
			imageb = this.p.g.images.blue;
		if(getTeam()==2)
			imageb = this.p.g.images.red;
		
		
		this.image = Utils.mergeImages(imagea, imageb);
		this.size = 30f;
		this.isHidden = false;
		this.spells = new Vector<Spell>();

	}
	// Copy constructor , to really create an unit
	public Character(Character c,float x,float y,int id){
		this.p = c.p;
		this.size = c.size;
		p.addCharacterObjets(this);
		if(id==-1){
			this.id = p.g.idChar;
			p.g.idChar+=1;
		}
		else{
			this.id = id;
		}
		this.name = c.name;
		this.setTeam(c.getTeam());
		this.damage = c.damage;
		this.maxLifePoints = c.maxLifePoints;
		this.lifePoints = c.maxLifePoints;
		this.sight = c.sight;
		this.collisionBox = new Circle(c.collisionBox.getCenterX(),c.collisionBox.getCenterY(),c.collisionBox.getBoundingCircleRadius());
		this.sightBox = new Circle(c.sightBox.getCenterX(),c.sightBox.getCenterY(),c.sightBox.getBoundingCircleRadius());
		this.animations = c.animations;
		this.setXY(x, y);
		this.maxVelocity = c.maxVelocity;
		this.armor = c.armor;
		this.range = c.range;
		this.chargeTime = c.chargeTime;
		this.isHidden = c.isHidden;
		this.image = c.image;
		this.selection_circle = c.selection_circle;
		this.horse = c.horse;
		this.weapon = c.weapon;
		this.group = new Vector<Character>();
		this.group.add(this);
		this.animStep = c.animStep;

		for(Spell s:c.spells){
			this.spells.addElement(s);
			this.spellsState.addElement(0f);
		}


	}

	public void castSpell(int number,int player){
		if(-1!=number && number<this.spells.size() && this.spellsState.get(number)>=this.spells.get(number).chargeTime){
			if(this.spells.get(number).needToClick){
				this.p.isCastingSpell.set(player,true);
				this.p.castingSpell.set(player,number);
			} 
		}
	}
	public boolean isLeader(){
		return this.leader==this;
	}
	public boolean isMobile(){
		return vx*vx+vy*vy>0.01f;
	}
	public void setXY(float x, float y){
		this.x = x;
		this.y = y ;
		this.collisionBox.setCenterX(this.x);
		this.collisionBox.setCenterY(this.y);
		this.sightBox.setCenterX(this.getX());
		this.sightBox.setCenterY(this.getY());
		Case oldc = this.c;
		this.c = this.p.mapGrid.getCase(x, y);
		//Updating the case
		if(c==null){
			return;
		}
		if(oldc==null || c.id!=oldc.id){
			if(oldc!=null && oldc.characters.contains(this))
				oldc.characters.remove(this);
			this.c.characters.addElement(this);
		}
		this.changes.x=true;
		this.changes.y = true;
	}
	public void setVXVY(float vx, float vy){
		this.vx = vx;
		this.vy = vy;
		int sector = 0;
		if(vx==0 && vy==0){
			return;
		}
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
		this.changes.orientation = true;

	}


	//Set functions

	@Deprecated
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
		Image imagea = this.p.g.images.corps;
		if(imagea==null)
			return;
		Image imageb = this.p.g.images.corps;
		Image imagec = this.p.g.images.corps;
		Image imaged = null;
		if(getTeam()==1){
			imageb = this.p.g.images.blue;
			imagec = this.p.g.images.horseBlue;
		}
		if(getTeam()==2){
			imageb = this.p.g.images.red;
			imagec = this.p.g.images.horseRed;
		}
		this.image = Utils.mergeImages(imagea, imageb);
		//Handling the weapon
		if(this.weapon!=null){
			if(this.weapon == "sword"){
				imageb = this.p.g.images.sword;
				imaged = this.p.g.images.mediumArmor;
			}
			if(this.weapon == "spear"){
				imageb = this.p.g.images.sword;
				imaged = this.p.g.images.heavyArmor;
			}
			if(this.weapon == "bow"){
				imageb = this.p.g.images.bow;
				imaged = this.p.g.images.lightArmor;
			}
			if(this.weapon == "bible")
				imageb = this.p.g.images.bible;
			if(this.weapon == "wand")
				imageb = this.p.g.images.magicwand;
			this.image = Utils.mergeImages(this.image, imageb);
			this.image = Utils.mergeImages(this.image, imaged);
		}

		//Handling the horse
		if(this.horse!=null){
			this.image = Utils.mergeHorse(imagec, this.image);
		}
	}


	//// ACTION METHODS

	// Main method called on every time loop
	// define the behavior of the character according to the attributes

	// ATTACK METHOD IF AT RANGE AND CHARGE TIME OK
	public void useWeapon(){
		this.state = 0f;		
	}

	public void action(){

		this.roundAfterBorn++;


		this.updateChargeTime();

		if(this.isImmolating){
			this.updateImmolation();
			return;
		}
		if(this.ia==null){
			// IA script�e
			this.actionIAScript();
		} else {
			// IA sp�cifique
			Vector<Character> enemies = new Vector<Character>();
			for(Character c: this.p.characters)
				if(c.getTeam()!=this.getTeam())
					enemies.add(c);
			this.moveToward(this.ia.moveInBattle(enemies));
			this.updateSetTarget();
			Circle range = new Circle(this.getX(), this.getY(), this.range);
			if(!(this.getTarget()!=null && (this.getTarget() instanceof Checkpoint || !range.intersects(this.target.collisionBox)))){
				if(state>=chargeTime && this.target!=null && this.target.getTeam()!=this.getTeam() && this.target instanceof Character){
					this.useWeapon();
				}
			}
		}

		this.updateAnimation();
	}
	// Movement method
	// the character move toward its target
	public void move(){

		if(this.getTarget()==null && this.checkpointTarget==null){
			return;
		}
		if(this.moveAhead){
			this.moveToward(this.getTarget());
			return;
		}
		if(this.c == this.getTarget().c){
			this.moveToward(this.getTarget());
		} else if(this.waypoints.size()>0){
			if(this.c==this.waypoints.get(0)){
				this.waypoints.remove(0);
				this.move();
			} else if(this.waypoints.size()>1 && this.c==this.waypoints.get(1)){
				this.waypoints.remove(1);
				this.waypoints.remove(0);
				this.move();
			} else if(this.waypoints.size()>2 && this.c==this.waypoints.get(2)){
				this.waypoints.remove(2);
				this.waypoints.remove(1);
				this.waypoints.remove(0);
				this.move();
			} else {
				this.moveToward(this.waypoints.get(0));
			}
		} else {
			this.waypoints = this.p.mapGrid.pathfinding(this.getX(), this.getY(), this.getTarget().getX(),this.getTarget().getY());
		}

	}
	// Moving toward method method
	// 
	public void moveToward(Case c){
		moveToward(new Checkpoint(c.x+c.sizeX/2f,c.y+c.sizeY/2f));
	}
	public void moveToward(Objet o){
		if(o==null && this.checkpointTarget==null){
			return;
		}
		float newvx, newvy;
		newvx = o.getX()-this.getX();
		newvy = o.getY()-this.getY();
		//Creating the norm of the acceleration and the new velocities among x and y
		float maxVNorm = this.maxVelocity/((float)this.getGameTeam().data.FRAMERATE);
		float vNorm = (float) Math.sqrt(newvx*newvx+newvy*newvy);

		//Checking if the point is not too close of the target
		if((this.group.size()>1 && vNorm<maxVNorm) || vNorm<maxVNorm){
			// 1st possible call of stop: the target is near
			this.stop();
			return;
		}
		vNorm = (float) Math.sqrt(newvx*newvx+newvy*newvy);
		if(vNorm>maxVNorm){
			//if the velocity is too large it is reduced to the maxVelocity value
			newvx = newvx*maxVNorm/vNorm;
			newvy = newvy*maxVNorm/vNorm;
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
		if(newX>this.p.maxX-this.collisionBox.getBoundingCircleRadius()){
			newX = this.p.maxX-this.collisionBox.getBoundingCircleRadius();
			newvx = Math.min(0f, newvx);
		}
		if(newY>this.p.maxY-this.collisionBox.getBoundingCircleRadius()){
			newY = this.p.maxY-this.collisionBox.getBoundingCircleRadius();
			newvy = Math.min(0f, newvy);
		}

		//eventually we reassign the position and velocity variables
		this.setVXVY(newvx, newvy);

		this.setXY(newX, newY);
		this.animationValue+=this.animStep/(float)this.getGameTeam().data.FRAMERATE;
		if(this.animationValue>=4f){
			this.animationValue = 0f;
		}

		if(animationValue!=0f){
			if(this.animationValue<1f || (this.animationValue>=2f && this.animationValue<3f)){
				animation = 1;
				this.changes.animation=true;
			}	
			else if(this.animationValue>=1f && this.animationValue<2f){
				animation = 0;
				this.changes.animation=true;
			}	
			else{
				animation = 2;
				this.changes.animation=true;
			}	
		}

	}
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
		this.setVXVY(0, 0);
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
		
		y1-=15f;
		y2-=15f;
		if(mouseHover){
			Color color = Color.darkGray;
			if(this.getGameTeam().id==1){
				color = new Color(0,0,205,0.4f);
			}
			else{
				color = new Color(250,0,0,0.4f);
			}

			Image i = this.image.getSubImage(imageWidth*animation,imageHeight*(int)direction,imageWidth,imageHeight);
			i = i.getScaledCopy((int)(x2-x1), (int)(y2-y1));
			
			g.drawImage(i,x1,y1);
			i.drawFlash(x1, y1,i.getWidth(),i.getHeight(),color);
			//g.drawImage(this.image,x1,y1,x2,y2,imageWidth*animation,imageHeight*direction,imageWidth*animation+imageWidth,imageHeight*direction+imageHeight);
		}
		else{
			g.drawImage(this.image,x1,y1,x2,y2,imageWidth*animation,imageHeight*direction,imageWidth*animation+imageWidth,imageHeight*direction+imageHeight);
		}

		// Drawing the health bar
		if(!isImmolating && this.lifePoints<this.maxLifePoints){
			//Draw lifepoints
			g.setColor(new Color(250,0,0,0.8f));
			g.fill(new Rectangle(this.getX()-r,-34f+this.getY()-r,2*r,4f));
			float x = this.lifePoints*2f*r/this.maxLifePoints;
			g.setColor(new Color(0,250,0,0.8f));
			g.fill(new Rectangle(this.getX()-r,-34f+this.getY()-r,x,4f));

		}
		//Draw state
		if(!isImmolating && this.state<this.chargeTime){
			g.setColor(new Color(255,255,255,0.8f));
			g.fill(new Rectangle(this.getX()-r,-30f+this.getY()-r,2*r,4f));
			float x = this.state*2f*r/this.chargeTime;
			g.setColor(new Color(0,0,0,0.8f));
			g.fill(new Rectangle(this.getX()-r,-30f+this.getY()-r,x,4f));
		}

		//DEBUG
		g.setColor(Color.black);
		g.drawString(String.valueOf(this.id), this.getX()-r,this.getY()-r);
		//Draw the immolation
		if(isImmolating){
			Image fire = this.p.g.images.explosion;
			r = fire.getWidth()/5f;
			x = this.getX();
			y = this.getY();
			if(this.remainingTime>=65f){
				g.drawImage(fire, x-40f, y-40f, x+40f, y+40f,0f,0f,r,r);
			}
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
		g.setLineWidth(3f);
		g.setAntiAlias(true);
		if(this.horse!=null){
			g.draw(this.collisionBox);

		} else {
			g.draw(this.collisionBox);
			//g.draw(new Ellipse(this.getX(),this.getY()+4f*r/6f,r,r-5f));
		}
		//DRAW TARGET
		g.setColor(Color.darkGray);
		if(this.target instanceof Character){
			g.draw(this.target.collisionBox);
		}
		if(this.target instanceof Checkpoint){
			g.draw(this.target.collisionBox);
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
		float toleranceCollision = 0.03f;
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
			int sign = (o.vy*(o.x-x)-o.vx*(o.y-y))<0 ? 1: -1;
			float newx = this.getX()+1.5f*sign*(o.vy)/2;
			float newy = this.getY()+1.5f*sign*(-o.vx)/2;
			//			this.setVXVY(newx-x, newy-y);
			this.setXY(newx,newy);
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


		//		if(x-oX>0f){
		//			if(y-oY>Math.abs(x-oX)*o.getHeight()/o.getWidth()){
		//				sector = 2;
		//			} else if(y-oY<-Math.abs(x-oX)*o.getHeight()/o.getWidth()){
		//				sector = 4;
		//			} else {
		//				sector = 1;
		//			}
		//		} else {
		//			if(y-oY>Math.abs(x-oX)*o.getHeight()/o.getWidth()){
		//				sector = 2;
		//			} else if(y-oY<-Math.abs(x-oX)*o.getHeight()/o.getWidth()){
		//				sector = 4;
		//			} else {
		//				sector = 3;
		//			}
		//		}

		//		x = x-vx;
		//		y = y-vy;
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
		float cornerThreshold = 5f;
		if((o.getMaxX()-this.getX()<cornerThreshold || this.getX()-o.getMinX()<cornerThreshold)&&(o.getMaxY()-this.getY()<cornerThreshold || this.getY()-o.getMinY()<cornerThreshold)){
			//System.out.println("dans un coin");
			//			if(this.getTarget()==null)
			//				return;
			//			if( ((sector==1||sector==3) && this.getTarget().getY()<o.getMaxY() && this.getTarget().getY()>o.getMinY()) || 
			//					((sector==2||sector==4) && this.getTarget().getX()<o.getMaxX() && this.getTarget().getX()>o.getMinX())){
			//				switch(sector){
			//				case 1: 
			//				case 3:
			//					if(this.getY()>o.getCenterY())
			//
			//						this.setXY(this.getX(), this.getY()+30f);
			//					else
			//						this.setXY(this.getX(), this.getY()-30f);
			//
			//					break;
			//				case 2:
			//				case 4:
			//					if(this.getX()>o.getCenterX())
			//
			//						this.setXY(this.getX()+30f, this.getY());
			//					else
			//						this.setXY(this.getX()-30f, this.getY());
			//
			//					break;
			//				}
			//				return;
			//			}
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
			boolean b;
			switch(Math.floorMod(sector, 2)){
			case 1: 
				// � droite ou � gauche
				b = this.getTarget()!=null && (this.getTarget().getY()<o.getMaxY() && this.getTarget().getY()>o.getMinY());
				float ya,yb;
				ya = y0+(float)Math.sqrt(vx*vx+vy*vy-(x2-x0)*(x2-x0));
				yb = y0-(float)Math.sqrt(vx*vx+vy*vy-(x2-x0)*(x2-x0));
				if ( (b && Math.abs(ya-oY)>Math.abs(yb-oY)) || (!b && Math.abs(ya-y2)<Math.abs(yb-y2))){
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
			case 0:
				// en haut ou en bas
				b = this.getTarget()!=null && (this.getTarget().getX()<o.getMaxX() && this.getTarget().getX()>o.getMinX());
				float xa,xb;
				xa = x0+(float)Math.sqrt(vx*vx+vy*vy-(y2-y0)*(y2-y0));
				xb = x0-(float)Math.sqrt(vx*vx+vy*vy-(y2-y0)*(y2-y0));
				if ( (b && Math.abs(xa-oX)>Math.abs(xb-oX)) || (!b && Math.abs(xa-x2)<Math.abs(xb-x2))){
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
		this.setTeam( newTeam);
		this.updateImage();
	}

	//// UPDATE FUNCTIONS


	public void setTarget(Objet t, Vector<Case> waypoints){
		this.target = t;
		if(t!=null){
			this.checkpointTarget = new Checkpoint(t.getX(),t.getY());
			if(waypoints==null){
				this.moveAhead = (this.p.mapGrid.isLineOk(x, y, t.getX(), t.getY()).size()>0);
				if(!this.moveAhead)	
					this.waypoints = this.computeWay();
				else
					this.waypoints = new Vector<Case>();
			}else{
				this.waypoints = new Vector<Case>();
				for(Case cas:waypoints)
					this.waypoints.addElement(cas);
			}
		}
	}

	public boolean encounters(Character c){
		boolean b = false;
		if(c.horse!=null && this.name=="Spearman"){
			return true;
		}
		if(c.horse==null && this.weapon == "bow"){
			return true;
		}
		if(c.weapon == "bow" && this.weapon =="wand"){
			return true;
		}
		return b;
	}


	public void actionIAScript(){
		this.updateSetTarget();
		Circle range = new Circle(this.getX(), this.getY(), this.range);
		if(this.getTarget()!=null && (this.getTarget() instanceof Checkpoint || !range.intersects(this.target.collisionBox))){
			this.move();
			if(!this.isMobile())
				return;
			if(this.group!=null){
				// Handling the group movement
				boolean nextToStop = false;
				boolean oneHasArrived = false;
				if(Utils.distance(this, this.getTarget())<(float)(2*Math.log(this.group.size())+1)*this.size){
					for(Character c: this.group){
						if(c!=this && !c.isMobile() && Utils.distance(c, this)<this.collisionBox.getBoundingCircleRadius()+c.collisionBox.getBoundingCircleRadius()+2f)
							nextToStop = true;
						if(Utils.distance(c, this.getTarget())< c.collisionBox.getBoundingCircleRadius()+2f)
							oneHasArrived = true;
					}
					//				if(nextToStop && Utils.distance(this, this.getTarget())>200f)
					//					nextToStop = false;
					if(nextToStop && oneHasArrived){
						this.stop();
						return;
					}
				}
			}
		}else{
			if(this.getTarget()==null){
				//System.out.println("stop2 " +(this.getTarget()!=null));
			}else{
				//System.out.println("stop2 " +(this.getTarget() instanceof Checkpoint)+" "+(!range.intersects(this.target.collisionBox)));
			}
			this.stop();
			if(state>=chargeTime && this.target!=null && this.target.getTeam()!=this.getTeam() && this.target instanceof Character){
				this.useWeapon();
			}
		}
	}

	public void updateChargeTime(){
		// INCREASE CHARGE TIME AND TEST IF CAN ATTACK
		if(this.state<=this.chargeTime)
			this.state+= 0.1f*Game.ratio;

		for(int i=0; i<this.spells.size(); i++){
			this.spellsState.set(i,Math.min(this.spells.get(i).chargeTime, this.spellsState.get(i)+1f));
		}

		//MULTI
		this.changes.state = true;
		this.changes.chargeTime=true;
	}
	public void updateImmolation(){
		this.lifePoints=this.maxLifePoints;
		this.remainingTime-=1f;
		if(this.remainingTime<=0f){
			this.lifePoints=-1f;
			this.getGameTeam().special+=this.getGameTeam().data.gainedFaithByImmolation;
		}
		this.changes.isImmolating=true;
		this.changes.remainingTime = true;
	}
	public void updateSetTarget(){

		if(this.getTarget()!=null && !this.getTarget().isAlive() ){
			this.setTarget(null);
		}
		if(this.getTarget()==null && this.secondaryTargets.size()>0){
			this.setTarget(this.secondaryTargets.get(0));
		}
		if(this.getTarget()==null){

			// The character has no target, we look for a new one
			Vector<Objet> potential_targets;
			if(this.damage>0f) 
				potential_targets = p.getEnnemiesInSight(this);
			else if (this.damage<0f) 
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
			if(c.getTeam()!=this.getTeam() && !c.collisionBox.intersects(this.sightBox)){
				this.setTarget(new Checkpoint(this.getTarget().x,this.getTarget().y),null);
			}
		}
	}
	public void updateAnimation(){
		if(this.vx>0 ||this.vy>0){
			this.incrementf+=4f/(float)this.getGameTeam().data.FRAMERATE;
		}


	}

	public String toString(boolean isDead){
		String s="";
		s+="id:"+id+";";
		s+="name:"+name+";";
		s+="tm:"+this.getTeam()+";";
		s+="x:"+(int)x+";";
		s+="y:"+(int)y+";";
		s+="lp:"+lifePoints+";";
		s+="st:"+this.state+";";
		if(this.target!=null){
			if(this.target instanceof Checkpoint){
				s+="tx:"+this.target.x+";";
				s+="ty:"+this.target.y+";";
			}
			if(this.target instanceof Character){
				s+="tid:"+this.target.id+";";
			}
		}
		if(isDead){
			s+="dead: ;";
		}
		return s;
	}

	public void parseCharacter(HashMap<String,String> hs){

		if(hs.containsKey("state")){
			this.state=Float.parseFloat(hs.get("state"));
		}

		if(hs.containsKey("tx")){
			this.setTarget(new Checkpoint(this.p,Float.parseFloat(hs.get("tx")),Float.parseFloat(hs.get("ty"))),null);
		}
		if(hs.containsKey("tid")){
			Character target = this.p.getCharacterById(Integer.parseInt(hs.get("tid")));
			this.setTarget(target,null);
		}

		if(hs.containsKey("x") && hs.containsKey("y")){
			this.setXY(Float.parseFloat(hs.get("x")), Float.parseFloat(hs.get("y")));
		}
		if(hs.containsKey("lp")){
			this.lifePoints=Float.parseFloat(hs.get("lp"));
			if(this.lifePoints>0 && !this.p.characters.contains(this)){
				this.p.characters.addElement(this);
				this.p.population.remove(this);
			}
			if(this.lifePoints<0 && this.p.characters.contains(this)){
				this.p.population.add(this);
				this.p.characters.remove(this);
			}
		}
		if(hs.containsKey("st")){
			this.state=Float.parseFloat(hs.get("st"));
		}
		if(hs.containsKey("tm")){
			this.setTeam(Integer.parseInt(hs.get("tm")));
		}
	}

	@Deprecated
	public String toStringEx(){
		String s ="" ;
		s+=toStringObjet();
		s+= toStringActionObjet();
		if(changes.weapon){
			s+="weapon:"+weapon+";";
			changes.weapon = false;
		}
		if(changes.chargeTime){
			s+="chargeTime:"+chargeTime+";";
			changes.chargeTime = false;
		}
		if(changes.state){
			s+="state:"+state+";";
			changes.state = false;
		}

		if(this.changes.spellState){
			s+="spellState:";
			for(float i : this.spellsState){
				s+=i+",";			
			}
			if(this.spellsState.size()>0){
				s=s.substring(0, s.length()-1);
			}
			this.changes.spellState=true;
			s+=";";
		}

		if(changes.animation){
			s+="animation:"+animation+";";
			changes.animation=false;
		}
		if(changes.isImmolating){
			s+="isImmolating:"+(isImmolating?1:0)+";";
			changes.isImmolating = false;
		}
		if(changes.remainingTime){
			s+="remainingTime:"+remainingTime+";";
			changes.remainingTime = false;
		}
		return s;
	}

	@Deprecated
	public void parseCharacterEx(HashMap<String,String> hs){
		if(hs.containsKey("weapon")){
			this.weapon=hs.get("weapon");
		}
		if(hs.containsKey("chargeTime")){
			this.chargeTime=Float.parseFloat(hs.get("chargeTime"));
		}
		if(hs.containsKey("state")){
			this.state=Float.parseFloat(hs.get("state"));
		}
		if(hs.containsKey("spellState")){
			this.spellsState.clear();
			String[] r = hs.get("spellState").split(",");
			if(!r[0].equals("")){
				for(int i = 0;i<r.length;i++){
					this.spellsState.addElement(Float.parseFloat(r[i]));
				}
			}
		}
		if(hs.containsKey("animation")){
			this.animation=Integer.parseInt(hs.get("animation"));
		}
		if(hs.containsKey("isImmolating")){
			this.isImmolating=hs.get("isImmolating").equals("1");
		}
		if(hs.containsKey("remainingTime")){
			this.remainingTime=Float.parseFloat(hs.get("remainingTime"));
		}


	}

	public void parse(HashMap<String,String> hs){
		//SEPARATION BETWEEN KEYS

		this.parseCharacter(hs);

	}

	public static Character createNewCharacter(HashMap<String,String> hs,Game g){
		Character c;
		int id = Integer.parseInt(hs.get("id"));
		float x = Float.parseFloat(hs.get("x"));
		float y = Float.parseFloat(hs.get("y"));
		int team = Integer.parseInt(hs.get("tm"));
		switch(hs.get("name")){
		case "spearman":
			c =  new UnitSpearman(g.plateau.getTeamById(team).data.spearman,x,y,id);	
			break;
		case "knight":
			c = new UnitKnight(g.plateau.getTeamById(team).data.knight,x,y,id);	

			break;
		case "priest":
			c =  new UnitPriest(g.plateau.getTeamById(team).data.priest,x,y,id);
			break;	
		case "crossbowman":
			c =  new UnitCrossbowman(g.plateau.getTeamById(team).data.crossbowman,x,y,id);
			break;	
		case "inquisitor":
			c =  new UnitInquisitor(g.plateau.getTeamById(team).data.inquisitor,x,y,id);
			break;
		case "archange":
			c = new UnitArchange(g.plateau.getTeamById(team).data.archange,x,y,id);
			break;
		default:
			c = null;
		}

		return c;

	}

}



