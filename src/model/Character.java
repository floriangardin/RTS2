package model;


import java.util.Vector;


import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;


import bullets.Arrow;
import bullets.Fireball;
import data.Attributs;
import display.DisplayRessources;
import main.Main;
import nature.Tree;
import pathfinding.Case;
import utils.ObjetsList;
import utils.Utils;

public class Character extends Objet{


	/**
	 * 
	 */
	private static final long serialVersionUID = -6156105360565271761L;

	//Isattackec
	public boolean isAttacked;
	public float timerAttacked = 0f;
	public float timerMaxValueAttacked = 10f;


	public static int MOVE=0;
	public static int AGGRESSIVE=1;
	public static int TAKE_BUILDING=2;
	public static int NORMAL  = 3;
	public static int HOLD_POSITION = 4;
	public static int DESTROY_BUILDING = 5;

	// General attributes
	public Circle sightBox;

	public boolean moveAhead;
	public float state;
	public boolean isAttacking  = false;
	public float attackState = 0f;

	private Vector<Integer> group = new Vector<Integer>();
	
	// Equipment attributes
	public boolean horse;

	// About drawing
	public float animationValue=0f;

	// Special Abilities or subisse
	public boolean isImmolating = false;
	public boolean isBolted = false;
	public float frozen = 0f;
	
	public float remainingTime;

	// UnitsList associated
	public Vector<Integer> secondaryTargets = new Vector<Integer>();
	public Vector<Integer> waypoints = new Vector<Integer>();

	// Copy constructor , to really create an unit
	public Character(float x,float y,ObjetsList name, int team){
		Game.g.plateau.addCharacterObjets(this);
		this.name= name;
		this.setTarget(null);
		this.setTeam(team);
		this.lifePoints = this.getAttribut(Attributs.maxLifepoints);
		this.collisionBox = new Circle(1f,1f,this.getAttribut(Attributs.size));
		this.selectionBox = new Rectangle(1f,1f,2*this.getAttribut(Attributs.size),3*this.getAttribut(Attributs.size));
		this.sightBox = new Circle(1f,1f,this.getAttribut(Attributs.sight));
		this.setXY(x, y);
		this.setGroup(new Vector<Character>());
		this.getGroup().add(this);
		this.getGameTeam().pop++;
		this.mode = NORMAL;
		// TODO : ajouter les sorts
		for(String s: this.getAttributList(Attributs.spells)){
			this.addSpell(ObjetsList.valueOf(s));
			this.spellsState.addElement(0f);
		}


	}



	public boolean isMobile(){
		return vx*vx+vy*vy>0.01f;
	}
	public void setXY(float x, float y){
		float xt = Math.min(Game.g.plateau.maxX-1f, Math.max(1f, x));
		float yt = Math.min(Game.g.plateau.maxY-1f, Math.max(1f, y));
		//		this.selectionBox = (Rectangle) this.selectionBox.transform(Transform.createTranslateTransform(xt-this.x, yt-this.y));
		this.selectionBox.setCenterX(xt);
		this.selectionBox.setCenterY(yt);
		this.x = xt;
		this.y = yt;
		this.collisionBox.setCenterX(this.x);
		this.collisionBox.setCenterY(this.y);
		this.sightBox.setCenterX(this.getX());
		this.sightBox.setCenterY(this.getY()-this.getAttribut(Attributs.size)/2f);
		int oldc = this.idCase;
		this.idCase = Game.g.plateau.mapGrid.getCase(x, y).id;
		//Updating the case
		if(idCase==-1){
			return;
		}
		if(oldc==-1 || idCase!=oldc){
			Case c = Game.g.plateau.mapGrid.getCase(oldc);
			if(c!=null && c.characters.contains(this))
				c.characters.remove(this);
			Game.g.plateau.mapGrid.getCase(this.idCase).characters.addElement(this);
		}

	}
	public void setVXVY(float vx, float vy){
		this.vx = vx;
		this.vy = vy;
		int sector = 0;
		if(vx==0 && vy==0){
			//Orientation toward target

			if(this.getTarget()!=null){
				vx =this.getTarget().x-this.x;
				vy = this.getTarget().y-this.y;
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



	//Update functions

	public void updateImage(){
		//		//Handling the team
		//		Image imagea = this.p.g.images.get("corps");
		//		if(imagea==null)
		//			return;
		//		Image imageb = this.p.g.images.get("corps");
		//		Image imagec = this.p.g.images.get("corps");
		//		Image imaged = null;
		//		if(getTeam()==1){
		//			imageb = this.p.g.images.get("blue");
		//			imagec = this.p.g.images.get("horseBlue");
		//		}
		//		if(getTeam()==2){
		//			imageb = this.p.g.images.get("red");
		//			imagec = this.p.g.images.get("horseRed");
		//		}
		//		this.image = Utils.mergeImages(imagea, imageb);
		//		//Handling the weapon
		//
		//		if(this.weapon!=null){
		//			if(this.weapon == "sword"){
		//				imageb = this.p.g.images.get("sword");
		//				imaged = this.p.g.images.get("mediumArmor");
		//			}
		//			if(this.weapon == "spear"){
		//				this.image = this.p.g.images.get("spearman_move");
		//				return;
		//				//				imageb = this.p.g.images.get("sword;
		//				//				imaged = this.p.g.images.get("heavyArmor;
		//			}
		//			if(this.weapon == "bow"){
		//				imageb = this.p.g.images.get("bow");
		//				imaged = this.p.g.images.get("lightArmor");
		//			}
		//			if(this.weapon == "bible")
		//				imageb = this.p.g.images.get("bible");
		//			if(this.weapon == "wand")
		//				imageb = this.p.g.images.get("magicwand");
		//			this.image = Utils.mergeImages(this.image, imageb);
		//			this.image = Utils.mergeImages(this.image, imaged);
		//		}
		//
		//		//Handling the horse
		//		if(this.horse!=null){
		//			this.image = Utils.mergeHorse(imagec, this.image);
		//		}
	}


	//// ACTION METHODS

	// Main method called on every time loop
	// define the behavior of the character according to the attributes

	// ATTACK METHOD IF AT RANGE AND CHARGE TIME OK
	public void useWeapon(){
		if(!(this.getTarget() instanceof Character)){
			return ;
		}
		String weapon = this.getAttributString(Attributs.weapon);

		//arme de corps à corps
		if(Game.g.data.getAttributList(ObjetsList.ContactWeapon, Attributs.list).contains(weapon)){
			Character c = (Character) this.getTarget();
			c.isAttacked();
			// Attack sound
			if(Game.g.sounds!=null)
				Game.g.sounds.get(this.getAttributString(Attributs.weapon)).play(1f,Game.g.options.soundVolume);

			// compute damages
			float damage = computeDamage(c);

			if(damage<0 || c.getAttribut(Attributs.armor)<damage){
				c.setLifePoints(c.lifePoints+c.getAttribut(Attributs.armor)-damage);
			}			
		} else {
			// autres armes
			switch(weapon){
			case "bow" :
				new Arrow(this,this.getTarget().getX()-this.getX(),this.getTarget().getY()-this.getY(),this.getAttribut(Attributs.damage));
				break;
			case "wand" :
				new Fireball(this,this.getTarget().getX(),this.getTarget().getY(),this.getTarget().getX()-this.getX(),this.getTarget().getY()-this.getY(),this.getAttribut(Attributs.damage));
				break;
			default:
			}
		}
		// Reset the state
		this.state = 0f;
		this.isAttacking = false;
	}

	public float computeDamage(Character target){
		float damage = this.getAttributAndRemoveUsageUnique(Attributs.damage);
		if(this.getAttributString(Attributs.weapon)=="spear" && target.horse)
			damage = damage*this.getGameTeam().data.bonusSpearHorse;
		if(this.getAttributString(Attributs.weapon)=="bow" && !target.horse)
			damage = damage*this.getGameTeam().data.bonusBowFoot;
		return damage;
	}

	public void action(){

		mainAction();
	}

	public void mainAction(){



		if(this.frozen>0f){
			this.frozen-=Main.increment;
			return;
		}
		if(isBolted){
			this.lifePoints-=20*Main.increment;
		}
		this.updateChargeTime();

		if(this.isImmolating){
			this.updateImmolation();
			return;
		}

		this.actionIAScript();
		this.updateAnimation();
		this.updateAttributsChange();
	}

	// Movement method
	// the character move toward its target
	public void move(){
		Objet target = this.getTarget();
		// rustine debug mode : collision with trees
		if(target!=null){
			if( this.getTarget() instanceof Tree){
				if(Utils.distance(this, target)<(target.collisionBox.getBoundingCircleRadius()+this.collisionBox.getBoundingCircleRadius()+2f)){
					this.stop();
					return;
				}
			}
		}
		if(mode == AGGRESSIVE){
			Vector<Character> targets  = Game.g.plateau.getEnnemiesInSight(this);
			if(targets.size()>0){
				this.setTarget(Utils.nearestObject(targets, this),null,NORMAL);
			}
		}
		if(this.getTarget()==null ){
			return;
		}
		if(this.moveAhead){
			this.moveToward(target.x,target.y);
			return;
		}
		if(this.idCase == target.idCase){
			this.moveToward(target.x,target.y);
		} else if(this.waypoints.size()>0){
			if(this.idCase==this.waypoints.get(0)){
				this.waypoints.remove(0);
				this.move();
			} else if(this.waypoints.size()>1 && this.idCase==this.waypoints.get(1)){
				this.waypoints.remove(1);
				this.waypoints.remove(0);
				this.move();
			} else if(this.waypoints.size()>2 && this.idCase==this.waypoints.get(2)){
				this.waypoints.remove(2);
				this.waypoints.remove(1);
				this.waypoints.remove(0);
				this.move();
			} else {
				this.moveToward(this.waypoints.get(0));
			}
		} else {
			this.waypoints = Game.g.plateau.mapGrid.pathfinding(this.getX(), this.getY(), this.getTarget().getX(),this.getTarget().getY());
		}
	}
	// Moving toward method method
	public void moveToward(int idCase){
		Case c0 = Game.g.plateau.mapGrid.getCase(this.idCase);
		Case c1 = Game.g.plateau.mapGrid.getCase(idCase);
		// il faut vérifier que l'intersection ne se fait pas trop près du bord de la case
		float a, b, c, d;
		float newX, newY;
		float coeff;
		if(c0.x==c1.x){
			b = c1.y+c1.sizeY/2f-y;;
			c = x;
			d = c1.x+c1.sizeX/2f-x;;
			//déplacement vertical
			if(c0.y<c1.y){
				// on va vers le bas
				a = c1.y-y;
				newY = c1.y+getAttribut(Attributs.maxVelocity);
			} else {
				// on va vers le haut
				a = c0.y-y;
				newY = c0.y-getAttribut(Attributs.maxVelocity);
			}
			newX = (float)(Math.min(Math.max(c+d*a/b, c1.x+getAttribut(Attributs.size)+getAttribut(Attributs.maxVelocity)/2),c1.x+c1.sizeX-getAttribut(Attributs.size)-getAttribut(Attributs.maxVelocity)/2));
		} else {
			// déplacement horizontal
			b = c1.x+c1.sizeX/2f-x;
			c = y;
			d = c1.y+c1.sizeY/2f-y;
			if(c0.x<c1.x){
				// on va vers la droite
				a = c1.x-x;
				newX = c1.x+getAttribut(Attributs.maxVelocity);
			} else {
				// on va vers la gauche
				a = c0.x-x;
				newX = c0.x-getAttribut(Attributs.maxVelocity);
			} 
			newY = (float)(Math.min(Math.max(c+d*a/b, c1.y+getAttribut(Attributs.size)+getAttribut(Attributs.maxVelocity)/2),c1.y+c1.sizeY-getAttribut(Attributs.size)-getAttribut(Attributs.maxVelocity)/2));
		}

		moveToward(newX,newY);
	}
	private void moveToward(float x , float y){

		float newvx, newvy;
		newvx = x-this.getX();
		newvy = y-this.getY();
		//Creating the norm of the acceleration and the new velocities among x and y
		float maxVNorm = this.getAttribut(Attributs.maxVelocity)/(Main.framerate);
		//System.out.println(Game.deplacementGroupIntelligent+ " "+this.group);
		if(Game.deplacementGroupIntelligent && this.getGroup()!=null){
			//System.out.println("héhé");
			for(Character c : this.getGroup()){
				maxVNorm = Math.min(maxVNorm, c.getAttribut(Attributs.maxVelocity)/(Main.framerate));
			}
		}
		float vNorm = (float) Math.sqrt(newvx*newvx+newvy*newvy);

		//Checking if the point is not too close of the target
		if((this.getGroup().size()>1 && vNorm<maxVNorm) || vNorm<maxVNorm){
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
		if(newX>Game.g.plateau.maxX-this.collisionBox.getBoundingCircleRadius()){
			newX = Game.g.plateau.maxX-this.collisionBox.getBoundingCircleRadius();
			newvx = Math.min(0f, newvx);
		}
		if(newY>Game.g.plateau.maxY-this.collisionBox.getBoundingCircleRadius()){
			newY = Game.g.plateau.maxY-this.collisionBox.getBoundingCircleRadius();
			newvy = Math.min(0f, newvy);
		}

		//eventually we reassign the position and velocity variables
		this.setVXVY(newvx, newvy);

		this.setXY(newX, newY);


		this.animationValue+=this.getAttribut(Attributs.animStep)/(float)this.getGameTeam().data.FRAMERATE;
		if(this.animationValue>=4f){
			this.animationValue = 0f;
			this.animation = (this.animation+1)%5;
			if(this.animation == 0){
				this.animation = 1;
			}

		}


	}
	public void stop(){
		this.animation = 0;
		if(this.mode!=TAKE_BUILDING ){
			this.mode = NORMAL;
		}
		if(this.getTarget() instanceof Checkpoint){
			if(this.secondaryTargets.size()==0){
				this.getTarget().lifePoints = -1f;
				this.setTarget(null);
				this.animationValue=0f;
			}else{
				this.setTarget(Game.g.plateau.getById(this.secondaryTargets.firstElement()));
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
		g.fillRect(this.getX()-r/2-1f,-47f+this.getY()-r,r+2f,8f);
		float x = this.lifePoints/this.getAttribut(Attributs.maxLifepoints);
		g.setColor(new Color((int)(255*(1f-x)),(int)(255*x),0));
		g.fillRect(this.getX()-r/2,-46f+this.getY()-r,x*r,6f);
	}


	public Graphics draw(Graphics g){

		float r = 60f*Main.ratioSpace;
		int direction = (orientation/2-1);
		// inverser gauche et droite
		if(direction==1 || direction==2){
			direction = ((direction-1)*(-1)+2);
		}
		Image im;
		im = Game.g.images.getUnit(name, direction, animation, getGameTeam().id, isAttacking);
		if(mouseOver && frozen<=0f && Game.g.round>Game.nbRoundInit){
			Color color = Color.darkGray;
			if(this.getGameTeam().id==1){
				color = new Color(0,0,205,0.4f);
			}
			else{
				color = new Color(250,0,0,0.4f);
			}
			g.drawImage(im,x-im.getWidth()/2,y-3*im.getHeight()/4);
			im.drawFlash(x-im.getWidth()/2,y-3*im.getHeight()/4,im.getWidth(),im.getHeight(),color);
		}
		else{
			g.drawImage(im,x-im.getWidth()/2,y-3*im.getHeight()/4);
		}
		if(frozen>0f){
			Color color = Color.darkGray;
			color = new Color(100,150,255,0.4f);
			g.drawImage(im,x-im.getWidth()/2,y-3*im.getHeight()/4);
			im.drawFlash(x-im.getWidth()/2,y-3*im.getHeight()/4,im.getWidth(),im.getHeight(),color);
		}
		if(isBolted){
			Color color = new Color(44,117,255,0.8f);
			g.drawImage(im,x-im.getWidth()/2,y-3*im.getHeight()/4);
			im.drawFlash(x-im.getWidth()/2,y-3*im.getHeight()/4,im.getWidth(),im.getHeight(),color);
		}

		// Drawing the health bar
		if(!isImmolating && this.lifePoints<this.getAttribut(Attributs.maxLifepoints)){
			drawLifePoints(g,r);
		}

		//Draw the immolation
		if(isImmolating){
			drawImmolation(g, r);
		}
		return g;
	}

	protected void drawImmolation(Graphics g,float r) {
		Image fire = Game.g.images.get("explosion").getScaledCopy(Main.ratioSpace);
		r = fire.getWidth()/5f;
		x = this.getX();
		y = this.getY();
		if(this.remainingTime>=65f){
			g.drawImage(fire, x-40f*Main.ratioSpace, y-40f*Main.ratioSpace, x+40f*Main.ratioSpace, y+40f*Main.ratioSpace,0f,0f,r,r);
		}
		else if(this.remainingTime>=55f)
			g.drawImage(fire, x-40f*Main.ratioSpace, y-40f*Main.ratioSpace, x+40f*Main.ratioSpace, y+40f*Main.ratioSpace,r,0f,2*r,r);
		else if(this.remainingTime>=45f)
			g.drawImage(fire, x-40f*Main.ratioSpace, y-40f*Main.ratioSpace, x+40f*Main.ratioSpace, y+40f*Main.ratioSpace,2*r,0f,3*r,r);
		else if(this.remainingTime>=40f*Main.ratioSpace)
			g.drawImage(fire, x-40f*Main.ratioSpace, y-40f*Main.ratioSpace, x+40f*Main.ratioSpace, y+40f*Main.ratioSpace,3*r,0f,4*r,r);
		else if(this.remainingTime>=35f)
			g.drawImage(fire, x-40f*Main.ratioSpace, y-40f*Main.ratioSpace, x+40f*Main.ratioSpace, y+40f*Main.ratioSpace,4*r,0f,5*r,r);
		else if(this.remainingTime>=40f*Main.ratioSpace)
			g.drawImage(fire, x-40f*Main.ratioSpace, y-40f*Main.ratioSpace, x+40f*Main.ratioSpace, y+40f*Main.ratioSpace,3*r,0f,4*r,r);
		else if(this.remainingTime>=35f)
			g.drawImage(fire, x-40f*Main.ratioSpace, y-40f*Main.ratioSpace, x+40f*Main.ratioSpace, y+40f*Main.ratioSpace,4*r,0f,3*r,r);
		else if(this.remainingTime>=30f)
			g.drawImage(fire, x-40f*Main.ratioSpace, y-40f*Main.ratioSpace, x+40f*Main.ratioSpace, y+40f*Main.ratioSpace,3*r,0f,4*r,r);
		else if(this.remainingTime>=25f)
			g.drawImage(fire, x-40f*Main.ratioSpace, y-40f*Main.ratioSpace, x+40f*Main.ratioSpace, y+40f*Main.ratioSpace,4*r,0f,5*r,r);
		else if(this.remainingTime>=20f)
			g.drawImage(fire, x-40f*Main.ratioSpace, y-40f*Main.ratioSpace, x+40f*Main.ratioSpace, y+40f*Main.ratioSpace,3*r,0f,4*r,r);
		else if(this.remainingTime>=15f)
			g.drawImage(fire, x-40f*Main.ratioSpace, y-40f*Main.ratioSpace, x+40f*Main.ratioSpace, y+40f*Main.ratioSpace,4*r,0f,3*r,r);
		else if(this.remainingTime>=10f)
			g.drawImage(fire, x-40f*Main.ratioSpace, y-40f*Main.ratioSpace, x+40f*Main.ratioSpace, y+40f*Main.ratioSpace,3*r,0f,4*r,r);
		else if(this.remainingTime>=5f)
			g.drawImage(fire, x-40f*Main.ratioSpace, y-40f*Main.ratioSpace, x+40f*Main.ratioSpace, y+40f*Main.ratioSpace,4*r,0f,5*r,r);
		else 
			g.drawImage(fire, x-40f*Main.ratioSpace, y-40f*Main.ratioSpace, x+40f*Main.ratioSpace, y+40f*Main.ratioSpace,3*r,0f,4*r,r);

	}

	public void drawIsSelected(Graphics g){

		g.setColor(Colors.selection);
		g.setLineWidth(2f*Main.ratioSpace);
		g.setAntiAlias(true);
		g.draw(this.collisionBox);
		Objet target = this.getTarget();
		if(target !=null && target instanceof Checkpoint){
			target.draw(g);
		}
		
		if(target !=null && target instanceof Building){
			((Building)target).marker.draw(g);
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
		if(this.getTarget() instanceof Character){
			g.setLineWidth(2f*Main.ratioSpace);
			g.setColor(Colors.aggressive);
			g.draw(this.getTarget().collisionBox);
		}
		if(this.getTarget() instanceof Checkpoint){
			((Checkpoint) getTarget()).toDraw = true;
			//this.target.draw(g);
		}
		if(this.getTarget() instanceof Building){
			((Building) getTarget()).marker.toDraw = true;
			//this.target.draw(g);
		}
		//		//Draw the building which is being conquered
		//		if(this.target !=null && this.target instanceof Building && this.mode==Character.TAKE_BUILDING){
		//			g.setLineWidth(2f*Main.ratioSpace);
		//			g.setColor(Colors.buildingTaking);
		//			Building target = (Building) this.target;
		//			g.draw(target.collisionBox);
		//		}

		g.setLineWidth(1f*Main.ratioSpace);
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


	public void setTarget(Objet t, Vector<Integer> waypoints,int mode){

		this.mode = mode;
		if(this.getTarget()!=null && this.getTarget() instanceof Checkpoint ){
			((Checkpoint)this.getTarget()).lifePoints =-1f;
		}
		if(this.getTarget()!=null && this.getTarget() instanceof Building){
			((Building) this.getTarget()).marker.state = ((Building) this.getTarget()).marker.maxDuration+1f;
		}
		if(t!=null && t instanceof Building){
			((Building) t).marker.state = 0;
		}
		this.setTarget(t);
		if(t!=null){
			if(waypoints==null){
				this.moveAhead = (Game.g.plateau.mapGrid.isLineOk(x, y, t.getX(), t.getY()).size()>0);
				if(!this.moveAhead)	
					this.waypoints = this.computeWay();
				else
					this.waypoints = new Vector<Integer>();
			}else{
				this.waypoints = new Vector<Integer>();
				for(Integer cas:waypoints)
					this.waypoints.addElement(cas);
			}
		}
	}

	public boolean encounters(Character c){
		boolean b = false;
		if(c.horse && this.getAttributString(Attributs.weapon)=="spear"){
			return true;
		}
		if(!c.horse && this.getAttributString(Attributs.weapon) == "bow"){
			return true;
		}
		if(c.getAttributString(Attributs.weapon) == "bow" && this.getAttributString(Attributs.weapon) =="wand"){
			return true;
		}
		return b;
	}


	public void actionIAScript(){
		this.updateSetTarget();
		Circle range = new Circle(this.getX(), this.getY(), this.getAttribut(Attributs.range));
		if(!isAttacking && this.getTarget()!=null && (this.getTarget() instanceof Checkpoint || !range.intersects(this.getTarget().collisionBox))){
			if(this.mode!=Character.HOLD_POSITION)
				this.move();
			if(!this.isMobile())
				return;
			if(this.getGroup()!=null){
				// Handling the group movement
				boolean nextToStop = false;
				boolean oneHasArrived = false;
				if(Utils.distance(this, this.getTarget())<(float)(2*Math.log(this.getGroup().size())+1)*1*this.getAttribut(Attributs.size)){
					for(Character c: this.getGroup()){
						if(c!=null && c!=this && !c.isMobile() && Utils.distance(c, this)<this.collisionBox.getBoundingCircleRadius()+c.collisionBox.getBoundingCircleRadius()+2f)
							nextToStop = true;
						if(c!=null && Utils.distance(c, this.getTarget())< c.collisionBox.getBoundingCircleRadius()+2f)
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
				if(Utils.distance(this, this.getTarget())<2*this.getAttribut(Attributs.size)){
					for(Character c:this.getGroup()){
						if(Utils.distance(c, c.getTarget())<2*c.getAttribut(Attributs.size)){
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
			if(state>=getAttribut(Attributs.chargeTime) && this.getTarget()!=null && this.getTarget() instanceof Character && canAttack() ){
				if(!this.isAttacking){
					this.stop();
					this.attackState = 0f;
					this.isAttacking = true;
				}
			}
			if(this.getTarget()!=null && this.isAttacking && this.attackState>this.getAttribut(Attributs.attackDuration)-2*Main.increment && this.mode!=TAKE_BUILDING &&  canAttack()){
				this.useWeapon();
				this.attackState = 0f;
				this.isAttacking= false;
			}

		}


	}
	
	public boolean canAttack(){
		boolean b= (getTarget()!=null && getTarget().getGameTeam().id!=this.getGameTeam().id);
		return   ( b || (!b && this.getAttribut(Attributs.damage)<0)) ;
	}




	public void updateChargeTime(){
		// INCREASE CHARGE TIME AND TEST IF CAN ATTACK
		if(!isAttacking && this.state<=this.getAttribut(Attributs.chargeTime))
			this.state+= Main.increment;
		if(isAttacking && this.attackState==0){
			this.animation = 0;
		}
		if(isAttacking && this.attackState<=this.getAttribut(Attributs.attackDuration))
			this.attackState+= Main.increment;
		if(this.attackState>=this.getAttribut(Attributs.attackDuration)){
			this.attackState-=2*Main.increment;
		}

		for(int i=0; i<this.getSpells().size(); i++){
			this.spellsState.set(i,Math.min(this.getSpell(i).getAttribut(Attributs.chargeTime), this.spellsState.get(i)+1f));
		}
		this.timerAttacked-=Main.increment;
		if(this.timerAttacked<0f){
			this.timerAttacked=0f;
			this.isAttacked = false;
		}

	}
	public void updateImmolation(){
		this.lifePoints=this.getAttribut(Attributs.maxLifepoints);
		this.remainingTime-=1f;
		if(this.remainingTime<=0f){
			//Test if explosion
			if(this.getAttribut(Attributs.explosionWhenImmolate)==1){
				for(Character c : Game.g.plateau.characters){
					if(Utils.distance(c, this)<100f && c!=this){
						c.setLifePoints(c.lifePoints-20f);
					}
				}
			}

			this.lifePoints=-1f;
			this.getGameTeam().special+=this.getGameTeam().data.gainedFaithByImmolation;
			Game.g.addDisplayRessources(new DisplayRessources(this.getGameTeam().data.gainedFaithByImmolation,"faith",this.x,this.y));
		}

	}
	public void updateSetTarget(){

		if(this.getTarget()!=null 
				&& !this.getTarget().isAlive()
				&& (this.getAttribut(Attributs.damage)>0 && this.getTarget().getTeam()!=this.getTeam())){
			this.setTarget(null);
		}
		if(this.getTarget()==null && this.secondaryTargets.size()>0){
			this.setTarget(Game.g.plateau.getById(this.secondaryTargets.get(0)));
		}
		if(this.getTarget()==null){
			// The character has no target, we look for a new one
			Vector<Character> potential_targets;
			if(this.getAttribut(Attributs.damage)>0f) {
				potential_targets = Game.g.plateau.getEnnemiesInSight(this);
			} else if (this.getAttribut(Attributs.damage)<0f) {
				potential_targets = Game.g.plateau.getWoundedAlliesInSight(this);
			} else{
				potential_targets = new Vector<Character>();
			}
			if(potential_targets.size()>0){
				this.setTarget(Utils.nearestObject(potential_targets, this));
			} 
			//			move();
			//			this.setTarget(null);
			//			return;
		}
		if(this.getTarget() instanceof Character){
			Character c =(Character) this.getTarget();
			if(c.getTeam()!=this.getTeam() && !c.collisionBox.intersects(this.sightBox)){
				
				this.setTarget(new Checkpoint(this.getTarget().x,this.getTarget().y,true),null,this.mode);
			}
		}
	}
	public void updateAnimation(){
		if(this.isAttacking){
			this.animation = Math.max(0, Math.min(4,(int)(this.getAttribut(Attributs.attackDuration)/this.attackState)));
		}


	}
	

	public void isAttacked() {
		this.isAttacked=true;
		this.timerAttacked = this.timerMaxValueAttacked;
	}



	public Vector<Character> getGroup() {
		Vector<Character> result = new Vector<Character>();
		
		for(Integer i : this.group){
			Objet o =  Game.g.plateau.getById(i);
			if(o!=null){
				result.add((Character)o );
			}
			
		}
		return result;
	}



	public void setGroup(Vector<Character> group) {
		this.group.clear();
		for(Character c  : group){
			this.group.add(c.id);
		}
	}
	
	public void addInGroup(int id ){
		this.group.addElement(id);
	}
	
	public void removeFromGroup(int id){
		this.group.removeElement(id);
	}





}



