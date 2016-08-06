package model;

import java.util.HashMap;
import java.util.Vector;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;

import buildings.Bonus;
import buildings.Building;
import pathfinding.Case;




public abstract class ActionObjet extends Objet{



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



	public Vector<Case> computeWay(){
		if(this.getTarget() instanceof Building && !(this.getTarget() instanceof Bonus)){
			Building b = (Building)this.getTarget();
			return this.p.mapGrid.pathfinding(x, y, (Rectangle)(b.collisionBox));
		} else {
			float xEnd = this.getTarget().x, yEnd = this.getTarget().y;
			return this.p.mapGrid.pathfinding(this.getX(), this.getY(), xEnd, yEnd);
		}
	}

	public String toStringActionObjet(){
		String s = "";

		s+="maxLifePoints:"+maxLifePoints+";";


		return s;
	}

	public void parseActionObjet(HashMap<String,String> hs){
		if(hs.containsKey("maxLifePoints")){
			this.maxLifePoints=Float.parseFloat(hs.get("maxLifePoints"));
		}
	}
}
