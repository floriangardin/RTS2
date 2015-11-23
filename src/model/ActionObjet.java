package model;

import java.util.HashMap;
import java.util.Vector;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;

import buildings.Building;
import pathfinding.Case;




public abstract class ActionObjet extends Objet{
	public float size;
	public int animation = 0;
	public float vx;
	public float vy;
	public Objet target;
	public Checkpoint checkpointTarget;
	public boolean toKeep=false;
	public boolean mouseOver = false;
	
	public Sound soundSetTarget;
	//CHANGED BOOLEAN

	
	public void destroy(){
		this.lifePoints = -10f;
		this.target = null;
		this.x = -100f;
		this.y = -100f;
	}
	public void action(){


	}
	public void move(){

	}

	public Objet getTarget(){
		return this.target;
	}
	public void setTarget(Objet t){
		this.setTarget(t,null);
	}
	public void setTarget(Objet t, Vector<Case> waypoints){
		this.target = t;
		if(t!=null)
			this.checkpointTarget = new Checkpoint(t.getX(),t.getY());
	}
	public void drawIsSelected(Graphics g) {

	}
	public Vector<Case> computeWay(){
		if(this.getTarget() instanceof Building){
			Building b = (Building)this.getTarget();
			return this.p.mapGrid.pathfinding(x, y, (Rectangle)(b.collisionBox));
		} else {
			float xEnd = this.getTarget().x, yEnd = this.getTarget().y;
			return this.p.mapGrid.pathfinding(this.getX(), this.getY(), xEnd, yEnd);
		}
	}

	public String toStringActionObjet(){
		String s = "";
		if(changes.maxLifePoints){
			s+="maxLifePoints:"+maxLifePoints+";";
			changes.maxLifePoints = false;
		}
		return s;
	}
	
	public void parseActionObjet(HashMap<String,String> hs){
		if(hs.containsKey("maxLifePoints")){
			this.maxLifePoints=Float.parseFloat(hs.get("maxLifePoints"));
		}
	}
}
