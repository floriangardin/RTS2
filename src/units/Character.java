package units;


import java.util.HashMap;
import java.util.Vector;


import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

import IA.IAUnit;
import battleIA.Mission;
import buildings.Building;

import main.Main;
import model.ActionObjet;
import model.Checkpoint;
import model.Colors;
import model.Game;
import model.GameTeam;
import model.NaturalObjet;
import model.Objet;
import model.Plateau;
import model.RidableObjet;
import model.Utils;
import nature.Tree;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Transform;

import pathfinding.Case;
import spells.Spell;
import IA.IAUnit;
import buildings.Building;

public class Character extends ActionObjet{


	public boolean explosionWhenImmolate = false;
	//Isattackec
	public boolean isAttacked;
	public float timerAttacked = 0f;
	public float timerMaxValueAttacked = 10f;

	//UNITS TYPE
	public static int SPEARMAN = 0;
	public static int CROSSBOWMAN = 1;
	public static int KNIGHT = 2;
	public static int INQUISITOR = 3;
	public static int PRIEST = 4;
	public static int ARCHANGE = 5;
	public int unitType;

	//MODE
	public Mission mission;
	public static int MOVE=0;
	public static int AGGRESSIVE=1;
	public static int TAKE_BUILDING=2;
	public static int NORMAL  = 3;
	public static int HOLD_POSITION = 4;
	public static int DESTROY_BUILDING = 5;

	// General attributes
	public Circle sightBox;

	public float animStep = 4f;
	public float armor = 0f;	

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
	public float attackDuration = 10f;
	public boolean isAttacking  = false;
	public float attackState = 0f;

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



	public Image animationAttack;




	// Constructor for data ( not adding in plateau not giving location)
	public Character(Plateau p, GameTeam gameteam){
		this.p = p;
		this.animations = new Image[1][4][4];
		this.setTeam(gameteam);
		this.name = "character";
		this.selection_circle = this.p.g.images.selection_circle;
		Image imagea = this.p.g.images.corps.getScaledCopy(Game.ratioSpace);
		Image imageb = this.p.g.images.corps.getScaledCopy(Game.ratioSpace);;
		if(getTeam()==1)
			imageb = this.p.g.images.blue.getScaledCopy(Game.ratioSpace);;
			if(getTeam()==2)
				imageb = this.p.g.images.red.getScaledCopy(Game.ratioSpace);;


				this.image = Utils.mergeImages(imagea, imageb);
				this.size = 30f*Game.ratioSpace;
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
		this.selectionBox = new Rectangle(c.selectionBox.getX(),c.selectionBox.getY(),c.selectionBox.getWidth(),c.selectionBox.getHeight());
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
		this.attackDuration = c.attackDuration;
		this.animationAttack = c.animationAttack;
		this.soundSetTarget = c.soundSetTarget;
		this.soundAttack = c.soundAttack;
		this.soundDeath = c.soundDeath;
		this.soundSelection = c.soundSelection;
		this.getGameTeam().pop++;
		this.mode = NORMAL;
		this.explosionWhenImmolate = c.explosionWhenImmolate;

		for(Spell s:c.spells){
			this.spells.addElement(s);
			this.spellsState.addElement(0f);
		}


	}


	public boolean isLeader(){
		return this.leader==this;
	}
	public boolean isMobile(){
		return vx*vx+vy*vy>0.01f;
	}
	public void setXY(float x, float y){
		float xt = Math.min(this.p.maxX-1f, Math.max(1f, x));
		float yt = Math.min(this.p.maxY-1f, Math.max(1f, y));
		this.selectionBox = this.selectionBox.transform(Transform.createTranslateTransform(xt-this.x, yt-this.y));
		this.x = xt;
		this.y = yt;
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

	}
	public void setVXVY(float vx, float vy){
		this.vx = vx;
		this.vy = vy;
		int sector = 0;
		if(vx==0 && vy==0){
			//Orientation toward target

			if(this.target!=null){
				vx =this.target.x-this.x;
				vy = this.target.y-this.y;
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
				this.image = this.p.g.images.spearman_move;
				return;
				//				imageb = this.p.g.images.sword;
				//				imaged = this.p.g.images.heavyArmor;
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

		mainAction();
	}

	public void mainAction(){
		this.toKeep = false;

		this.updateChargeTime();

		if(this.isImmolating){
			this.updateImmolation();
			return;
		}

		this.actionIAScript();
		this.updateAnimation();
	}

	// Movement method
	// the character move toward its target
	public void move(){
		if(mode == AGGRESSIVE){
			Vector<Character> targets  = this.p.getEnnemiesInSight(this);
			if(targets.size()>0){
				this.setTarget(Utils.nearestObject(targets, this),null,NORMAL);
			}
		}
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
	public void moveToward(Case c){
		moveToward(new Checkpoint(this.p,c.x+c.sizeX/2f,c.y+c.sizeY/2f));
	}
	public void moveToward(Objet o){
		if(o==null && this.checkpointTarget==null){
			return;
		}
		float newvx, newvy;
		newvx = o.getX()-this.getX();
		newvy = o.getY()-this.getY();
		//Creating the norm of the acceleration and the new velocities among x and y
		float maxVNorm = this.maxVelocity/(Main.framerate);
		//System.out.println(Game.deplacementGroupIntelligent+ " "+this.group);
		if(Game.deplacementGroupIntelligent && this.group!=null){
			//System.out.println("héhé");
			for(Character c : this.group){
				maxVNorm = Math.min(maxVNorm, c.maxVelocity/(Main.framerate));
			}
		}
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

			}	
			else if(this.animationValue>=1f && this.animationValue<2f){
				animation = 0;

			}	
			else{
				animation = 2;

			}	
		}

	}
	public void stop(){
		this.checkpointTarget = null;
		if(this.mode!=TAKE_BUILDING){
			this.mode = NORMAL;
		}
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
	public void drawLifePoints(Graphics g,float r){
		//Draw lifepoints

		g.setColor(Color.black);
		g.fill(new Rectangle(this.getX()-r/2-1f,-47f+this.getY()-r,r+2f,8f));
		float x = this.lifePoints/this.maxLifePoints;
		g.setColor(new Color((int)(255*(1f-x)),(int)(255*x),0));
		g.fill(new Rectangle(this.getX()-r/2,-46f+this.getY()-r,x*r,6f));
	}

	public Graphics draw(Graphics g){

		float r = collisionBox.getBoundingCircleRadius();
		float direction = 0f;
		direction = (float)(orientation/2-1);
		int imageWidth = this.image.getWidth()/3;
		int imageHeight = this.image.getHeight()/4;
		float factor = 1f;
		if(this.weapon=="spear")
			factor = 2;
		float drawWidth = factor*r*imageWidth/Math.min(imageWidth,imageHeight);
		float drawHeight = factor*r*imageHeight/Math.min(imageWidth,imageHeight);
		float x1 = this.getX() - drawWidth;
		float y1 = this.getY() + drawWidth - 2*drawHeight;
		float x2 = this.getX() + drawWidth;
		float y2 = this.getY() + drawWidth;
		y1-=15f;
		y2-=15f;
		if(mouseOver){
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
			g.setColor(new Color(0,0,0));
			g.fill(new Rectangle(this.getX()-r/2,-34f+this.getY()-r,r,4f));
			float x = this.lifePoints*r/this.maxLifePoints;
			g.setColor(new Color(255*(1-x),255*x,0));
			g.fill(new Rectangle(this.getX()-r/2,-34f+this.getY()-r,x,4f));

		}
		//Draw state
		if(!isImmolating && this.state<this.chargeTime){
			g.setColor(new Color(255,255,255,0.8f));
			g.fill(new Rectangle(this.getX()-r/2,-30f+this.getY()-r,r,4f));
			float x = this.state*r/this.chargeTime;
			g.setColor(new Color(0,0,0,0.8f));
			g.fill(new Rectangle(this.getX()-r/2,-30f+this.getY()-r,x,4f));
		}

		//Draw the immolation
		if(isImmolating){
			drawImmolation(g, r);
		}
		return g;
	}

	protected void drawImmolation(Graphics g,float r) {
		Image fire = this.p.g.images.explosion.getScaledCopy(Game.ratioSpace);
		r = fire.getWidth()/5f;
		x = this.getX();
		y = this.getY();
		if(this.remainingTime>=65f){
			g.drawImage(fire, x-40f*Game.ratioSpace, y-40f*Game.ratioSpace, x+40f*Game.ratioSpace, y+40f*Game.ratioSpace,0f,0f,r,r);
		}
		else if(this.remainingTime>=55f)
			g.drawImage(fire, x-40f*Game.ratioSpace, y-40f*Game.ratioSpace, x+40f*Game.ratioSpace, y+40f*Game.ratioSpace,r,0f,2*r,r);
		else if(this.remainingTime>=45f)
			g.drawImage(fire, x-40f*Game.ratioSpace, y-40f*Game.ratioSpace, x+40f*Game.ratioSpace, y+40f*Game.ratioSpace,2*r,0f,3*r,r);
		else if(this.remainingTime>=40f*Game.ratioSpace)
			g.drawImage(fire, x-40f*Game.ratioSpace, y-40f*Game.ratioSpace, x+40f*Game.ratioSpace, y+40f*Game.ratioSpace,3*r,0f,4*r,r);
		else if(this.remainingTime>=35f)
			g.drawImage(fire, x-40f*Game.ratioSpace, y-40f*Game.ratioSpace, x+40f*Game.ratioSpace, y+40f*Game.ratioSpace,4*r,0f,5*r,r);
		else if(this.remainingTime>=40f*Game.ratioSpace)
			g.drawImage(fire, x-40f*Game.ratioSpace, y-40f*Game.ratioSpace, x+40f*Game.ratioSpace, y+40f*Game.ratioSpace,3*r,0f,4*r,r);
		else if(this.remainingTime>=35f)
			g.drawImage(fire, x-40f*Game.ratioSpace, y-40f*Game.ratioSpace, x+40f*Game.ratioSpace, y+40f*Game.ratioSpace,4*r,0f,3*r,r);
		else if(this.remainingTime>=30f)
			g.drawImage(fire, x-40f*Game.ratioSpace, y-40f*Game.ratioSpace, x+40f*Game.ratioSpace, y+40f*Game.ratioSpace,3*r,0f,4*r,r);
		else if(this.remainingTime>=25f)
			g.drawImage(fire, x-40f*Game.ratioSpace, y-40f*Game.ratioSpace, x+40f*Game.ratioSpace, y+40f*Game.ratioSpace,4*r,0f,5*r,r);
		else if(this.remainingTime>=20f)
			g.drawImage(fire, x-40f*Game.ratioSpace, y-40f*Game.ratioSpace, x+40f*Game.ratioSpace, y+40f*Game.ratioSpace,3*r,0f,4*r,r);
		else if(this.remainingTime>=15f)
			g.drawImage(fire, x-40f*Game.ratioSpace, y-40f*Game.ratioSpace, x+40f*Game.ratioSpace, y+40f*Game.ratioSpace,4*r,0f,3*r,r);
		else if(this.remainingTime>=10f)
			g.drawImage(fire, x-40f*Game.ratioSpace, y-40f*Game.ratioSpace, x+40f*Game.ratioSpace, y+40f*Game.ratioSpace,3*r,0f,4*r,r);
		else if(this.remainingTime>=5f)
			g.drawImage(fire, x-40f*Game.ratioSpace, y-40f*Game.ratioSpace, x+40f*Game.ratioSpace, y+40f*Game.ratioSpace,4*r,0f,5*r,r);
		else 
			g.drawImage(fire, x-40f*Game.ratioSpace, y-40f*Game.ratioSpace, x+40f*Game.ratioSpace, y+40f*Game.ratioSpace,3*r,0f,4*r,r);

	}

	public void drawIsSelected(Graphics g){

		g.setColor(Colors.selection);
		g.setLineWidth(2f*Game.ratioSpace);
		g.setAntiAlias(true);
		if(this.horse!=null){
			g.draw(this.collisionBox);

		} else {
			g.draw(this.collisionBox);
			//g.draw(new Ellipse(this.getX(),this.getY()+4f*r/6f,r,r-5f));
		}
		if(mode==MOVE || mode==NORMAL){
			g.setColor(Colors.team0);
		}
		else if(mode==AGGRESSIVE){
			g.setColor(Colors.aggressive);
		}
		else if(mode==TAKE_BUILDING){
			g.setColor(Colors.buildingTaking);
		}
		g.setLineWidth(2f);
		if(this.target instanceof Character){
			g.setColor(Colors.aggressive);
			g.draw(this.target.collisionBox);
		}
		if(this.target instanceof Checkpoint){

			this.target.draw(g);
		}
		//Draw the building which is being conquered
		if(this.target !=null && this.target instanceof Building && this.mode==Character.TAKE_BUILDING){
			g.setLineWidth(2f*Game.ratioSpace);
			g.setColor(Colors.buildingTaking);
			Building target = (Building) this.target;
			g.draw(target.collisionBox);
		}

		g.setLineWidth(1f*Game.ratioSpace);
		g.setAntiAlias(false);
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
	// Collision with other round object inamovible
	public void collision(Circle c) {
		float cx = c.getCenterX();
		float cy = c.getCenterY();
		float x = this.x - this.vx;
		float y = this.y - this.vy;
		float v = (float)Math.sqrt(vx*vx+vy*vy);
		float vx = y - cy;
		float vy = cx - x;
		float n = (float)Math.sqrt(vx*vx+vy*vy);
		// get the mediatrice of both object
		float newx = x+vx*v/n;
		float newy = y+vy*v/n;
		this.setXY(newx,newy);
		//this.move(this.vx+this.x,this.vy+this.y );
	}
	// Collision with NaturalObjets
	public void collision(NaturalObjet o) {
		if(o instanceof Tree){
			this.collision((Circle)o.collisionBox);
		} else {
			this.collisionRect((Rectangle)o.collisionBox);
		}
	}
	// Collision with EnemyGenerator
	public void collision(Building o) {
		this.collisionRect((Rectangle)o.collisionBox);
	}

	public void collisionRect(Rectangle o) {

		/*On considï¿½re pour l'instant que nos natural objets sont carrï¿½s
		 * il faut dans un premier temps dï¿½terminer de quel cï¿½tï¿½ ï¿½jecter l'objet
		 * pour cela on dï¿½limite 4 secteurs:
		 * 		1: ï¿½ droite
		 * 		2: en haut
		 * 		3: ï¿½ gauche
		 * 		4: en bas
		 *	puis on ï¿½jecte le point au bord du cï¿½tï¿½ correspondant via projection
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
		//		if((o.getMaxX()-this.getX()<cornerThreshold || this.getX()-o.getMinX()<cornerThreshold)&&(o.getMaxY()-this.getY()<cornerThreshold || this.getY()-o.getMinY()<cornerThreshold)){
		//			//System.out.println("dans un coin");
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
		//		}
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
				// ï¿½ droite ou ï¿½ gauche
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
		if(this.mode == TAKE_BUILDING){
			this.mode = NORMAL;
		}

		if(t!=null){
			this.checkpointTarget = new Checkpoint(this.p,t.getX(),t.getY());
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

	public void setTarget(Objet t, Vector<Case> waypoints,int mode){
		this.mode = mode;
		this.target = t;
		if(t!=null){
			this.checkpointTarget = new Checkpoint(this.p,t.getX(),t.getY());
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
		if(!isAttacking && this.getTarget()!=null && (this.getTarget() instanceof Checkpoint || !range.intersects(this.target.collisionBox))){
			if(this.mode!=Character.HOLD_POSITION)
				this.move();
			if(!this.isMobile())
				return;
			if(this.group!=null){
				// Handling the group movement
				boolean nextToStop = false;
				boolean oneHasArrived = false;
				if(Utils.distance(this, this.getTarget())<(float)(2*Math.log(this.group.size())+1)*1*this.size){
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
				// avoiding problem if two members of the group are close to the target
				if(Utils.distance(this, this.getTarget())<2*this.size){
					for(Character c:this.group){
						if(Utils.distance(c, c.getTarget())<2*c.size){
							this.stop();
							c.stop();
						}
					}
				}
			}
		}else{
			if(this.getTarget()==null){
				return;
			}else{
				//System.out.println("stop2 " +(this.getTarget() instanceof Checkpoint)+" "+(!range.intersects(this.target.collisionBox)));
			}
			if(!isAttacking)
				this.stop();
			if(state>=chargeTime && this.target!=null && this.target.getTeam()!=this.getTeam() && this.target instanceof Character){
				if(!this.isAttacking){
					this.stop();
					this.attackState = 0f;
					this.isAttacking = true;
					this.animation= 0;
				}
			}
			if(this.target!=null && this.isAttacking && this.attackState>this.attackDuration-2*Main.increment && this.mode!=TAKE_BUILDING){
				this.useWeapon();
				this.attackState = 0f;
				this.isAttacking= false;
			}

		}


	}



	public void updateChargeTime(){
		// INCREASE CHARGE TIME AND TEST IF CAN ATTACK
		if(!isAttacking && this.state<=this.chargeTime)
			this.state+= Main.increment;
		if(isAttacking && this.attackState<=this.attackDuration)
			this.attackState+= Main.increment;
		if(this.attackState>=this.attackDuration){
			this.attackState-=2*Main.increment;
		}

		for(int i=0; i<this.spells.size(); i++){
			this.spellsState.set(i,Math.min(this.spells.get(i).chargeTime, this.spellsState.get(i)+1f));
		}
		this.timerAttacked-=Main.increment;
		if(this.timerAttacked<0f){
			this.timerAttacked=0f;
			this.isAttacked = false;
		}

	}
	public void updateImmolation(){
		this.lifePoints=this.maxLifePoints;
		this.remainingTime-=1f;
		if(this.remainingTime<=0f){
			//Test if explosion
			if(this.explosionWhenImmolate){
				for(Character c : p.characters){
					if(Utils.distance(c, this)<100f && c!=this){
						c.setLifePoints(c.lifePoints-20f);
					}
				}
			}
			if(p.g.sounds.fire.playing())
				p.g.sounds.fire.stop();
			this.lifePoints=-1f;
			this.getGameTeam().special+=this.getGameTeam().data.gainedFaithByImmolation;
		}

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
			Vector<Character> potential_targets;
			if(this.damage>0f) 
				potential_targets = p.getEnnemiesInSight(this);
			else if (this.damage<0f) 
				potential_targets = p.getWoundedAlliesInSight(this);
			else
				potential_targets = new Vector<Character>();
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
				this.setTarget(new Checkpoint(this.p,this.getTarget().x,this.getTarget().y),null);
			}
		}
	}
	public void updateAnimation(){
		if(this.vx>0 ||this.vy>0 || this.isAttacking){
			this.incrementf+=4f/(float)this.getGameTeam().data.FRAMERATE;
		}


	}

	public String toString(){
		String s="";
		s+="id:"+id+";";
		s+="name:"+name+";";
		s+="tm:"+this.getTeam()+";";
		s+="x:"+(int)x+";";
		s+="y:"+(int)y+";";
		s+="lp:"+lifePoints+";";
		s+="st:"+this.state+";";
		s+="as:"+this.attackState+";";
		s+="vx:"+this.vx+";";
		s+="vy:"+this.vy+";";
		s+="mode:"+this.mode+";";
		if(this.isAttacking){
			s+="ia: ;";
		}

		if(this.target!=null){
			if(this.target instanceof Checkpoint){
				s+="tx:"+this.target.x+";";
				s+="ty:"+this.target.y+";";
			}
			if(this.target instanceof Character || this.target instanceof Building){
				s+="tid:"+this.target.id+";";
			}
		}

		return s;
	}

	public void parseCharacter(HashMap<String,String> hs){

		if(hs.containsKey("x") && hs.containsKey("y")){
			this.setXY(Float.parseFloat(hs.get("x")), Float.parseFloat(hs.get("y")));
		}
		if(hs.containsKey("as")){
			this.attackState=Float.parseFloat(hs.get("as"));
		}

		if(hs.containsKey("vx")){
			this.setVXVY(Float.parseFloat(hs.get("vx")),Float.parseFloat(hs.get("vy")));
		}

		if(hs.containsKey("ia")){
			this.isAttacking = true;
		}

		if(hs.containsKey("tx")){
			this.setTarget(new Checkpoint(this.p,Float.parseFloat(hs.get("tx")),Float.parseFloat(hs.get("ty"))),null);
		}
		if(hs.containsKey("tid")){
			ActionObjet target = this.p.getCharacterById(Integer.parseInt(hs.get("tid")));
			if(target==null){
				target = p.getBuildingById(Integer.parseInt(hs.get("tid")));
			}
			this.setTarget(target,null);
		}

		if(hs.containsKey("mode")){	
			this.mode = Integer.parseInt(hs.get("mode"));
		}

		if(hs.containsKey("lp")){
			this.lifePoints=Float.parseFloat(hs.get("lp"));
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

		s+="weapon:"+weapon+";";


		s+="chargeTime:"+chargeTime+";";

		s+="state:"+state+";";


		s+="spellState:";
		for(float i : this.spellsState){
			s+=i+",";			
		}
		if(this.spellsState.size()>0){
			s=s.substring(0, s.length()-1);
		}

		s+=";";


	
			s+="animation:"+animation+";";

		
			s+="isImmolating:"+(isImmolating?1:0)+";";

		
			s+="remainingTime:"+remainingTime+";";

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
			c =  new UnitSpearman(g.teams.get(team).data.spearman,x,y,id);	
			break;
		case "knight":
			c = new UnitKnight(g.teams.get(team).data.knight,x,y,id);	

			break;
		case "priest":
			c =  new UnitPriest(g.teams.get(team).data.priest,x,y,id);
			break;	
		case "crossbowman":
			c =  new UnitCrossbowman(g.teams.get(team).data.crossbowman,x,y,id);
			break;	
		case "inquisitor":
			c =  new UnitInquisitor(g.teams.get(team).data.inquisitor,x,y,id);
			break;
		case "archange":
			c = new UnitArchange(g.teams.get(team).data.archange,x,y,id);
			break;
		default:
			c = null;
		}

		return c;
	}
	public void isAttacked() {
		this.isAttacked=true;
		this.timerAttacked = this.timerMaxValueAttacked;
	}

}



