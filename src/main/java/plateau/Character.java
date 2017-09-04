package plateau;


import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

import data.Attributs;
import events.EventAttackDamage;
import events.EventHandler;
import events.EventNames;
import main.Main;
import model.Colors;
import nature.Tree;
import pathfinding.Case;
import render.SimpleRenderEngine;
import ressources.Images;
import ressources.Sounds;
import spells.SpellEffect;
import system.Debug;
import utils.ObjetsList;
import utils.Utils;

public class Character extends Objet{


	/**
	 * 
	 */
	private static final long serialVersionUID = -6156105360565271761L;




	public static int MOVE=0;
	public static int AGGRESSIVE=1;
	public static int TAKE_BUILDING=2;
	public static int NORMAL  = 3;
	public static int HOLD_POSITION = 4;
	public static int DESTROY_BUILDING = 5;

	// General attributes
	public Circle sightBox;

	public boolean moveAhead;
	public float distanceToTarget=-1f;
	public float state;
	public boolean isAttacking  = false;
	public float attackState = 0f;



	private Vector<Integer> group = new Vector<Integer>();

	// Equipment attributes
	public boolean horse;

	// About drawing
	public float animationValue=0f;


	// Vecteur de spells
	public Vector<Integer> spellsEffect = new Vector<Integer>();

	// Special Abilities or subisse

	public boolean isBolted = false;
	public float frozen = 0f;

	public float remainingTime;

	// UnitsList associated
	public Vector<Integer> secondaryTargets = new Vector<Integer>();
	public Vector<Integer> waypoints = new Vector<Integer>();

	// Copy constructor , to really create an unit
	public Character(float x,float y,ObjetsList name, Team team, Plateau plateau){
		super(plateau);
		this.name= name;
		this.setTarget(null, plateau);
		this.team = team;
		this.lifePoints = this.getAttribut(Attributs.maxLifepoints);
		this.x = x;
		this.y = y;
		this.collisionBox = new Circle(this.x,this.y,this.getAttribut(Attributs.size));
		this.selectionBox = new Rectangle(this.x-this.getAttribut(Attributs.size),this.y-3*this.getAttribut(Attributs.size)/2,2*this.getAttribut(Attributs.size),3*this.getAttribut(Attributs.size));
		this.sightBox = new Circle(this.x,this.y,this.getAttribut(Attributs.sight));
		this.setGroup(new Vector<Character>());
		this.getGroup(plateau).add(this);
		plateau.addCharacterObjets(this);
		this.mode = NORMAL;
		// TODO : ajouter les sorts
		for(String s: this.getAttributList(Attributs.spells)){
			this.addSpell(ObjetsList.valueOf(s));
			this.spellsState.addElement(0f);
		}


	}

	public void addSpellEffect(SpellEffect e){
		spellsEffect.add(e.id);
	}

	public boolean isMobile(){
		return vx*vx+vy*vy>0.01f;
	}
	public void setVXVY(float vx, float vy, Plateau plateau){
		this.vx = vx;
		this.vy = vy;
		float R2 = vx*vx+vy*vy;
		SimpleRenderEngine.old_vx = (float) (this.vx/Math.sqrt(R2));
		SimpleRenderEngine.old_vy = (float) (this.vy/Math.sqrt(R2));
		int sector = 0;
		if(vx==0 && vy==0){
			//Orientation toward target

			if(this.getTarget(plateau)!=null){
				vx =this.getTarget(plateau).x-this.x;
				vy = this.getTarget(plateau).y-this.y;
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
	//// ACTION METHODS
	// Main method called on every time loop
	// define the behavior of the character according to the attributes
	// ATTACK METHOD IF AT RANGE AND CHARGE TIME OK
	public void useWeapon(Plateau plateau){
		if(!(this.getTarget(plateau) instanceof Character)){
			return ;
		}
		String weapon = this.getAttributString(Attributs.weapon);

		//arme de corps à corps
		if(plateau.teams.get(0).data.getAttributList(ObjetsList.ContactWeapon, Attributs.list).contains(weapon)){
			Character c = (Character) this.getTarget(plateau);

			// Attack sound
			EventHandler.addEvent(EventNames.Attack, this, plateau);
			// compute damages
			float damage = computeDamage(c);

			if(damage<0 || c.getAttribut(Attributs.armor)<damage){
				c.setLifePoints(c.lifePoints+c.getAttribut(Attributs.armor)-damage, plateau);

			}			
		} else {
			// autres armes
			switch(weapon){
			case "bow" :
				new Arrow(this,this.getTarget(plateau).getX()-this.getX(),this.getTarget(plateau).getY()-this.getY(),this.getAttribut(Attributs.damage), plateau);
				break;
			case "wand" :
				new Fireball(this,this.getTarget(plateau).getX(),this.getTarget(plateau).getY(),this.getTarget(plateau).getX()-this.getX(),this.getTarget(plateau).getY()-this.getY(),this.getAttribut(Attributs.damage), plateau);
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
			damage = damage*this.getTeam().data.bonusSpearHorse;
		if(this.getAttributString(Attributs.weapon)=="bow" && !target.horse)
			damage = damage*this.getTeam().data.bonusBowFoot;
		return damage;
	}

	public void action(Plateau plateau){
		if(this.frozen>0f){
			this.frozen-=Main.increment;
			return;
		}
		if(isBolted){
			this.lifePoints-=20*Main.increment;
		}
		this.updateChargeTime();
		// Update spell effects
		Vector<Integer> toRemove = new Vector<Integer>();
		for(Integer i : spellsEffect){
			SpellEffect e = (SpellEffect) plateau.getById(i);
			if(e==null){
				toRemove.add(i);
				continue;
			}

		}

		if(canMove){
			this.actionIAScript(plateau);
			this.updateAnimation();
			this.updateAttributsChange(plateau);
		}

	}

	// Movement method
	// the character move toward its target
	public void move(Plateau plateau){
		Objet target = this.getTarget(plateau);
		if(mode == AGGRESSIVE){
			Vector<Character> targets  = plateau.getEnnemiesInSight(this);
			if(targets.size()>0){
				this.setTarget(Utils.nearestObject(targets, this),null,NORMAL, plateau);
			}
		}
		if(this.getTarget(plateau)==null ){
			return;
		}
		if(this.moveAhead){
			this.moveToward(target.x,target.y, plateau);
			return;
		}
		if(this.idCase == target.idCase){
			this.moveToward(target.x,target.y, plateau);
		} else if(this.waypoints.size()>0){
			if(plateau.round%20==this.id%20){
				this.waypoints = this.computeWay(plateau);
			}
			if(this.idCase==this.waypoints.get(0)){
				this.waypoints.remove(0);
				Case co;
				while(this.waypoints.size()>1){
					co = plateau.mapGrid.getCase(this.waypoints.get(1));
					if(plateau.mapGrid.isLineOk(this.x, this.y, co.x+co.sizeX/2, co.y+co.sizeY/2).size()>0){
						this.waypoints.remove(0);
					} else {
						break;
					}
				}
				if(this.waypoints.size()==1){
					this.moveAhead = true;
				}
			} 
			this.moveToward(this.waypoints.get(0), plateau);
		} else {
			this.waypoints = plateau.mapGrid.pathfinding(this.getX(), this.getY(), this.getTarget(plateau).getX(),this.getTarget(plateau).getY());
		}
	}
	// Moving toward method method
	public void moveToward(int idCase, Plateau plateau){
		Case c0 = plateau.mapGrid.getCase(this.idCase);
		Case c1 = plateau.mapGrid.getCase(idCase);
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
			newX = c1.x+c1.sizeX/2;
			//			newX = (float)(Math.min(Math.max(c+d*a/b, c1.x+getAttribut(Attributs.size)+getAttribut(Attributs.maxVelocity)/2),c1.x+c1.sizeX-getAttribut(Attributs.size)-getAttribut(Attributs.maxVelocity)/2));
		} else if (c0.y==c1.y) {
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
			newY = c1.y+c1.sizeY/2;
			//			newY = (float)(Math.min(Math.max(c+d*a/b, c1.y+getAttribut(Attributs.size)+getAttribut(Attributs.maxVelocity)/2),c1.y+c1.sizeY-getAttribut(Attributs.size)-getAttribut(Attributs.maxVelocity)/2));
		}else {
			// déplacement azimut brutal
			newX = c1.x+c1.sizeX/2f;
			newY = c1.y+c1.sizeY/2f;
		}

		moveToward(newX,newY, plateau);
	}
	private void moveToward(float x , float y, Plateau plateau){

		float newvx, newvy;
		newvx = x-this.getX();
		newvy = y-this.getY();
		//Creating the norm of the acceleration and the new velocities among x and y
		float maxVNorm = this.getAttribut(Attributs.maxVelocity)/(Main.framerate);
		//System.out.println(Game.deplacementGroupIntelligent+ " "+this.group);
		float vNorm = (float) Math.sqrt(newvx*newvx+newvy*newvy);

		//Checking if the point is not too close of the target
		if((this.getGroup(plateau).size()>1 && vNorm<maxVNorm) || vNorm<maxVNorm){
			// 1st possible call of stop: the target is near
			this.stop(plateau);
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
		if(newX>plateau.maxX-this.collisionBox.getBoundingCircleRadius()){
			newX = plateau.maxX-this.collisionBox.getBoundingCircleRadius();
			newvx = Math.min(0f, newvx);
		}
		if(newY>plateau.maxY-this.collisionBox.getBoundingCircleRadius()){
			newY = plateau.maxY-this.collisionBox.getBoundingCircleRadius();
			newvy = Math.min(0f, newvy);
		}

		//eventually we reassign the position and velocity variables
		this.setVXVY(newvx, newvy, plateau);

		this.setXY(newX, newY, plateau);


		this.animationValue+=this.getAttribut(Attributs.animStep)/(float)this.getTeam().data.FRAMERATE;
		if(this.animationValue>=4f){
			this.animationValue = 0f;
			this.animation = (this.animation+1)%5;
			if(this.animation == 0){
				this.animation = 1;
			}

		}


	}
	public void stop(Plateau plateau){
		this.animation = 0;
		if(this.mode!=TAKE_BUILDING ){
			this.mode = NORMAL;
		}
		if(this.getTarget(plateau) instanceof Checkpoint){
			if(this.secondaryTargets.size()==0){
				this.getTarget(plateau).lifePoints = -1f;
				this.setTarget(null, plateau);
				this.animationValue=0f;
			}else{
				this.setTarget(plateau.getById(this.secondaryTargets.firstElement()), plateau);
				this.secondaryTargets.remove(0);
				return;
			}

		}
		this.setVXVY(0, 0, plateau);
	}


	//// COLLISIONS

	// Collision with other Characters
	public void collision(Character o, Plateau plateau) {
		// If collision test who have the highest velocity
		// The highest velocity continues 
		// The lowest velocity move away ( he is pushed instead of the other ) 

		// set the tolerance for collision:
		//   - 0: collision is totally authorized
		//   - 1: no collision but clipping
		float toleranceCollision = 0.01f;
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
			this.setXY(newx,newy, plateau);
		}
		else{

			this.setXY(this.getX()+toleranceCollision*(this.getX()-o.getX()),this.getY()+toleranceCollision*(this.getY()-o.getY()), plateau);
		}
		//this.move(this.vx+this.x,this.vy+this.y );
	}
	// Collision with other round object inamovible
	public void collision(Circle c, Plateau plateau) {
		float xi = c.getCenterX();
		float yi = c.getCenterY();
		float x0 = this.x - this.vx;
		float y0 = this.y - this.vy;
		float x1 = this.x;
		float y1 = this.y;
		float R2 = vx*vx+vy*vy;
		float ux = xi - x0;
		float uy = yi - y0;
		float n = (float) Math.sqrt(ux*ux+uy*uy);
		ux /= n;
		uy /= n;
		float newx, newy;
		float Z = this.getAttribut(Attributs.size)+c.radius+1;
		float D2 = (xi-x0)*(xi-x0)+(yi-y0)*(yi-y0);
		if(R2==0){
			newx = xi - Z*ux;
			newy = yi - Z*uy;
		} else {
			R2 = (float) Math.max(R2, 0.1+(Z-Math.sqrt(D2))*(Z-Math.sqrt(D2)));
			float A2 = (Z*Z+D2-R2)*(Z*Z+D2-R2)/(4*Z*Z*D2);
			float h = (float) (Z*Math.sqrt(1-A2));
			float d = 0f;
			if(h*h<=R2){
				d = (float) Math.sqrt(R2-h*h);	
			}
			float vx = uy;
			float vy = -ux;
			float signv = Math.signum((x1-x0)*vx+(y1-y0)*vy);
			float signu = Math.signum(D2-Z*Z);
			newx = x0 + signu*d*ux + signv*h*vx;
			newy = y0 + signu*d*uy + signv*h*vy;
			System.out.println(Z+" "+Math.sqrt(R2)+" "+Math.sqrt(D2)+" "+A2);
			System.out.println(Z*Z+D2-R2+" "+2*Z*Math.sqrt(D2));
			System.out.println(h+" "+d);
			System.out.println(ux+" "+uy+" "+vx+" "+vy);
			System.out.println(this.x+" "+this.y+" "+newx+" "+newy);
			System.out.println(signv);
			System.out.println(Utils.distance(newx, newy, xi, yi)+" "+Z);
			System.out.println();
			SimpleRenderEngine.ux = ux;
			SimpleRenderEngine.vx = vx;
			SimpleRenderEngine.old_vx = (float) (this.vx/Math.sqrt(R2));
			SimpleRenderEngine.old_vy = (float) (this.vy/Math.sqrt(R2));
			SimpleRenderEngine.uy = uy;
			SimpleRenderEngine.vy = vy;
			SimpleRenderEngine.d = d;
			SimpleRenderEngine.h = h;
			SimpleRenderEngine.signv = signv;
			SimpleRenderEngine.signu = signu;
			System.out.println();
		}
		this.setXY(newx,newy,plateau);
		this.vx = this.x - x0;
		this.vy = this.y - y0;
		//this.move(this.vx+this.x,this.vy+this.y );
	}
	// Collision with NaturalObjets
	public void collision(NaturalObjet o, Plateau plateau) {
		if(o instanceof Tree){
			this.collision((Circle)o.collisionBox, plateau);
		} else {
			this.collisionRect((Rectangle)o.collisionBox, plateau);
		}
	}
	// Collision with EnemyGenerator
	public void collision(Building o, int corner, Plateau plateau) {
		if(corner>0){
			Circle c;
			System.out.println(corner);
			float xi=0, yi=0;
			float r = 50f;
			switch(corner){
			case 1: xi = o.x-o.getAttribut(Attributs.sizeX)/2f+r; yi = o.y-o.getAttribut(Attributs.sizeY)/2f+r; break;
			case 2: xi = o.x+o.getAttribut(Attributs.sizeX)/2f-r; yi = o.y-o.getAttribut(Attributs.sizeY)/2f+r; break;
			case 3: xi = o.x+o.getAttribut(Attributs.sizeX)/2f-r; yi = o.y+o.getAttribut(Attributs.sizeY)/2f-r; break;
			case 4: xi = o.x-o.getAttribut(Attributs.sizeX)/2f+r; yi = o.y+o.getAttribut(Attributs.sizeY)/2f-r; break;
			}
			if(Utils.distance(xi, yi, x, y)<=r+getAttribut(Attributs.size)){
				c = new Circle(xi, yi, r);
				SimpleRenderEngine.circle = c;
				this.collision(c, plateau);
			}
		} else {
			this.collisionRect((Rectangle)o.collisionBox, plateau);
		}
	}

	public void collisionRect(Rectangle o, Plateau plateau) {
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
		case 1: newX = o.getMaxX()+this.collisionBox.getBoundingCircleRadius()+1;
		break;
		case 2:	newY = o.getMaxY()+this.collisionBox.getBoundingCircleRadius()+1;
		break;
		case 3: newX = o.getMinX()-this.collisionBox.getBoundingCircleRadius()-1;
		break;
		case 4: newY = o.getMinY()-this.collisionBox.getBoundingCircleRadius()-1;
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
				b = this.getTarget(plateau)!=null && (this.getTarget(plateau).getY()<o.getMaxY() && this.getTarget(plateau).getY()>o.getMinY());
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
				this.setVXVY(finalX-x0, 0f, plateau);
				break;
			case 0:
				// en haut ou en bas
				b = this.getTarget(plateau)!=null && (this.getTarget(plateau).getX()<o.getMaxX() && this.getTarget(plateau).getX()>o.getMinX());
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
				this.setVXVY(0f, finalY-y0, plateau);
				break;
			default:
			}
		}
		this.setXY(finalX, finalY, plateau);

	}



	//// UPDATE FUNCTIONS


	public void setTarget(Objet t, Vector<Integer> waypoints,int mode, Plateau plateau){
		//		if(t!=null)
		//			System.out.println("id "+t.id);
		//		
		//		System.out.println(this.getTarget(plateau));
		//		System.out.println(this.waypoints.size());
		//		System.out.println(this.secondaryTargets.size());
		this.mode = mode;
		if(this.getTarget(plateau)!=null && this.getTarget(plateau) instanceof Checkpoint ){
			((Checkpoint)this.getTarget(plateau)).lifePoints =-1f;
		}
		if(this.getTarget(plateau)!=null && this.getTarget(plateau) instanceof Building){
			((Building) this.getTarget(plateau)).marker.state = ((Building) this.getTarget(plateau)).marker.maxDuration+1f;
		}
		if(t!=null && t instanceof Building){
			((Building) t).marker.state = 0;
		}
		this.setTarget(t, plateau);

		if(t!=null){

			if(waypoints==null){
				this.moveAhead = (plateau.mapGrid.isLineOk(x, y, t.getX(), t.getY()).size()>0);
				if(!this.moveAhead)	
					this.waypoints = this.computeWay(plateau);
				else
					this.waypoints = new Vector<Integer>();
			}else{
				this.waypoints = new Vector<Integer>();
				for(Integer cas:waypoints)
					this.waypoints.addElement(cas);
			}
		}
		//System.out.println("target" +target+" this "+this.id);
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


	public void actionIAScript(Plateau plateau){
		this.updateSetTarget(plateau);
		Circle range = new Circle(this.getX(), this.getY(), this.getAttribut(Attributs.range));
		// move toward target ?
		if(!isAttacking && this.getTarget(plateau)!=null && 
				(this.getTarget(plateau) instanceof Checkpoint || 
						!range.intersects(this.getTarget(plateau).collisionBox) ||
						(!(this.getTarget(plateau) instanceof Building) && plateau.mapGrid.isLineOk(getX(), getY(), getTarget(plateau).getX(), getTarget(plateau).getY()).size()==0)
						)
				){
			if(this.mode!=Character.HOLD_POSITION){
				this.move(plateau);
				if(this.getTarget(plateau)!=null){
					float newDistanceToTarget = (this.getTarget(plateau).x-this.x)*(this.getTarget(plateau).x-this.x)+(this.getTarget(plateau).y-this.y)*(this.getTarget(plateau).y-this.y);
					if(this.distanceToTarget>=0f){
						if(this.distanceToTarget<this.getAttribut(Attributs.size)*this.getAttribut(Attributs.size) && distanceToTarget<=newDistanceToTarget){
							this.stop(plateau);
						}
					}
					this.distanceToTarget = newDistanceToTarget;
				}
			}
			if(!this.isMobile())
				return;
			if(this.getGroup(plateau)!=null){
				// Handling the group movement
				boolean nextToStop = false;
				boolean oneHasArrived = false;
				if(Utils.distance(this, this.getTarget(plateau))<(float)(2*Math.log(this.getGroup(plateau).size()+1)*1*this.getAttribut(Attributs.size))){
					for(Character c: this.getGroup(plateau)){
						if(c!=null && c!=this && !c.isMobile() && Utils.distance(c, this)<this.collisionBox.getBoundingCircleRadius()+c.collisionBox.getBoundingCircleRadius()+2f)
							nextToStop = true;
						if(c!=null && Utils.distance(c, this.getTarget(plateau))< c.collisionBox.getBoundingCircleRadius()+2f)
							oneHasArrived = true;
					}
					//				if(nextToStop && Utils.distance(this, this.getTarget(plateau))>200f)
					//					nextToStop = false;
					if(nextToStop && oneHasArrived){
						this.stop(plateau);
						return;
					}
				}
				// avoiding problem if two members of the group are close to the target
				if(Utils.distance(this, this.getTarget(plateau))<2*this.getAttribut(Attributs.size)){
					for(Character c:this.getGroup(plateau)){
						if(Utils.distance(c, c.getTarget(plateau))<2*c.getAttribut(Attributs.size)){
							this.stop(plateau);
							c.stop(plateau);
						}
					}
				}
			}
		}else{
			if(this.getTarget(plateau)==null){
				return;
			}
			if(!isAttacking){
				this.stop(plateau);
			}
			if(state>=getAttribut(Attributs.chargeTime) && this.getTarget(plateau)!=null && this.getTarget(plateau) instanceof Character && canAttack(plateau) ){
				if(!this.isAttacking){
					this.stop(plateau);
					this.attackState = 0f;
					this.isAttacking = true;
				}
			}
			if(this.getTarget(plateau)!=null && this.isAttacking && this.attackState>this.getAttribut(Attributs.attackDuration)-2*Main.increment && this.mode!=TAKE_BUILDING &&  canAttack(plateau)){
				this.useWeapon(plateau);
				this.attackState = 0f;
				this.isAttacking= false;
			}

		}


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
			this.spellsState.set(i, Math.min(this.getSpell(i).getAttribut(Attributs.chargeTime), this.spellsState.get(i)+1f));
		}
	}

	public void updateSetTarget(Plateau plateau){

		if(this.getTarget(plateau)!=null 
				&& !this.getTarget(plateau).isAlive()
				&& (this.getAttribut(Attributs.damage)>0 && this.getTarget(plateau).getTeam()!=this.getTeam())){

			this.setTarget(null, plateau);
		}
		if(this.getTarget(plateau)==null && this.secondaryTargets.size()>0){
			this.setTarget(plateau.getById(this.secondaryTargets.get(0)), plateau);
		}
		if(this.getTarget(plateau)==null){
			// The character has no target, we look for a new one
			Vector<Character> potential_targets;
			if(this.getAttribut(Attributs.damage)>0f) {
				potential_targets = plateau.getEnnemiesInSight(this);
			} else if (this.getAttribut(Attributs.damage)<0f) {
				potential_targets = plateau.getWoundedAlliesInSight(this);
			} else{
				potential_targets = new Vector<Character>();
			}
			if(potential_targets.size()>0){
				this.setTarget(Utils.nearestObject(potential_targets, this), plateau);
			} 
			//			move();
			//			this.setTarget(null);
			//			return;
		}
		if(this.getTarget(plateau) instanceof Character){
			Character c =(Character) this.getTarget(plateau);
			if(c.getTeam()!=this.getTeam() && !c.collisionBox.intersects(this.sightBox)){
				this.setTarget(new Checkpoint(this.getTarget(plateau).x,this.getTarget(plateau).y,true, plateau),null,this.mode, plateau);
			}
		}
	}
	public void updateAnimation(){
		if(this.isAttacking){
			this.animation = Math.max(0, Math.min(4,(int)(this.getAttribut(Attributs.attackDuration)/this.attackState)));
		}
	}


	public Vector<Character> getGroup(Plateau plateau) {
		Vector<Character> result = new Vector<Character>();

		for(Integer i : this.group){
			Objet o =  plateau.getById(i);
			if(o!=null && o instanceof Character){
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



