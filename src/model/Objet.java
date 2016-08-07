package model;


import java.util.HashMap;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import buildings.Bonus;
import buildings.Building;
import bullets.Bullet;
import data.Attributs;
import data.AttributsChange;
import main.Main;
import spells.Spell;
import units.Character;
import utils.ObjetsList;
import utils.SpellsList;

public abstract class Objet implements java.io.Serializable {

	// Animation : mode,orientation,increment
	public int id;
	public int mode;
	public int orientation=2;
	public int increment;
	public float incrementf;
	public float x;
	public float y;
	public int idCase;
	public Shape collisionBox;
	public Rectangle selectionBox;
	public Color color;
	public float lifePoints;
	public String name;
	protected int team;

	// Bonus, équipements, potions et autres stuff
	public Vector<AttributsChange> attributsChanges = new Vector<AttributsChange>();


	// Spells ( what should appear in the bottom bar
	public Vector<SpellsList> spells = new Vector<SpellsList>();
	public Vector<Float> spellsState = new Vector<Float>();

	// visibility boolean 
	public boolean visibleByCurrentTeam;
	public boolean visibleByCamera;

	public int animation = 0;
	public float vx;
	public float vy;
	public Objet target;
	public Checkpoint checkpointTarget;
	public boolean toKeep=false;
	public boolean mouseOver = false;



	public void action(){}
	public void move(){}

	public void updateAttributsChange(){
		Vector<AttributsChange> toDelete = new Vector<AttributsChange>();
		for(AttributsChange ac : this.attributsChanges){
			ac.remainingTime-=1f*Main.increment;
			if(ac.remainingTime<=0){
				toDelete.add(ac);
			}
		}
		for(AttributsChange ac : toDelete){
			this.attributsChanges.remove(ac);
		}
	}
	
	public Objet getTarget(){
		return this.target;
	}
	public void setTarget(Objet t){
		this.setTarget(t,null);
	}
	public void setTarget(Objet t, Vector<Integer> waypoints){
		this.target = t;
		if(t!=null)
			this.checkpointTarget = new Checkpoint(t.getX(),t.getY());
	}
	public void drawIsSelected(Graphics g) {

	}
	public Vector<Integer> computeWay(){
		if(this.getTarget() instanceof Building && !(this.getTarget() instanceof Bonus)){
			Building b = (Building)this.getTarget();
			return Game.g.plateau.mapGrid.pathfinding(x, y, (Rectangle)(b.collisionBox));
		} else {
			float xEnd = this.getTarget().x, yEnd = this.getTarget().y;
			return Game.g.plateau.mapGrid.pathfinding(this.getX(), this.getY(), xEnd, yEnd);
		}
	}

	public String toStringActionObjet(){
		String s = "";

		s+="maxLifePoints:"+getAttribut(Attributs.maxLifepoints)+";";


		return s;
	}


	public void setName(String s){
		this.name = s;
	}
	public String getName(){
		return this.name;
	}
	public int getTeam(){
		return this.team;
	}
	public GameTeam getGameTeam(){
		return Game.g.teams.get(this.team);
	}
	public void setTeam(int i){
		this.team = i;
	}


	protected void destroy(){
		this.lifePoints = -10;

		this.x = -10f;
		this.y = -10f;
	}
	public Graphics draw(Graphics g){
		return g;}
	protected void collision(Objet o){}
	public abstract void collision(Character c);
	public float getX(){
		return x;
	}
	public float getY(){
		return y;
	}
	protected void setXY(float x, float y){

		if(this instanceof Bullet){
			this.x = x;
			this.y = y;
		} else {

			float xt = Math.min(Game.g.plateau.maxX-1f, Math.max(1f, x));
			float yt = Math.min(Game.g.plateau.maxY-1f, Math.max(1f, y));

			this.x = xt;
			this.y = yt;
		}
		this.collisionBox.setCenterX(x);
		this.collisionBox.setCenterY(y);

		this.idCase = Game.g.plateau.mapGrid.getCase(x, y).id;

	}

	public boolean isAlive(){
		return this.lifePoints>0f;
	}

	public void setLifePoints(float lifepoints){
		if(lifepoints<this.getAttribut(Attributs.maxLifepoints))
			this.lifePoints= lifepoints;
		else{
			this.lifePoints = this.getAttribut(Attributs.maxLifepoints);
		}
	}
	
	// TOSTRING METHODS
	public String toStringObjet(){
		return "";
	}
	public String toString(){
		return this.toStringObjet();
	}
	

	public void parse(HashMap<String, String> hs) {

	}

	// Attributs
	public float getAttribut(Attributs attribut){
		float a = this.getGameTeam().data.getAttribut(this.name.toLowerCase(),attribut);
		for(AttributsChange ac : this.attributsChanges){
			if(ac.attribut==attribut){
				a = ac.apply(a);
			}
		}
		return a;
	}
	public float getAttributAndRemoveUsageUnique(Attributs attribut){
		/**
		 * remove the corresponding attributes change if its with usage unique
		 */
		float a = this.getGameTeam().data.getAttribut(this.name.toLowerCase(),attribut);
		Vector<AttributsChange> toDelete = new Vector<AttributsChange>();
		for(AttributsChange ac : this.attributsChanges){
			if(ac.attribut==attribut){
				a = ac.apply(a);
				if(ac.usageUnique){
					toDelete.add(ac);
				}
			}
		}
		for(AttributsChange ac : toDelete){
			this.attributsChanges.remove(ac);
		}
		return a;
	}
	public String getAttributString(Attributs attribut){
		return this.getGameTeam().data.getAttributString(this.name.toLowerCase(),attribut);
	}
	public Vector<String> getAttributList(Attributs attribut){
		return this.getGameTeam().data.getAttributList(this.name.toLowerCase(),attribut);
	}

	// Spells
	public Spell getSpell(int i){
		if(this.spells.size()>i){
			if(this.getGameTeam().data.spells.containsKey(this.spells.get(i))){
				return this.getGameTeam().data.spells.get(this.spells.get(i));
			}
		} else {
			System.out.println("vous essayez d'accéder à un spell inexistant");
		}
		return null;
	}
	public Vector<Spell> getSpells() {
		Vector<Spell>spells = new Vector<Spell>();
		for(int i=0; i<this.spells.size(); i++){
			spells.addElement(getSpell(i));
		}
		return spells;
	}
	
	// Autres
	public float getVisibleSize(){
		if(this.getGameTeam().data.datas.containsKey(this.name)){
			if(this.getGameTeam().data.datas.get(this.name.toLowerCase()).attributs.containsKey(Attributs.size)){
				return getAttribut(Attributs.size)+getAttribut(Attributs.sight);
			} else if(this.getGameTeam().data.datas.get(this.name.toLowerCase()).attributs.containsKey(Attributs.sizeX)){
				float sizeX=this.getGameTeam().data.getAttribut(this.name.toLowerCase(),Attributs.sizeX);
				float sizeY=this.getGameTeam().data.getAttribut(this.name.toLowerCase(),Attributs.sizeY);
				return (float) Math.sqrt(sizeX*sizeX+sizeY*sizeY)+getAttribut(Attributs.sight);
			}
		}
		return 1f;
	}



}

