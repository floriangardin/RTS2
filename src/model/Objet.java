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
import units.Character;

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
	public static HashMap<String,String> preParse(String s){
		String[] u = s.split(";");
		HashMap<String,String> hs = new HashMap<String,String>();
//		if(u.length<=1){
//			return hs;
//		}
		for(int i=0;i<u.length;i++){
			String[] r = u[i].split("\\:");
			if(r.length>1){
				hs.put(r[0], r[1]);
			}
			else{
				hs.put(r[0],"");
			}
			
		}
		return hs;
	}
	
	public void parse(HashMap<String, String> hs) {
		
	}

	public float getAttribut(Attributs attribut){
		return this.getGameTeam().data.getAttribut(this.name.toLowerCase(),attribut);
	}
	public String getAttributString(Attributs attribut){
		return this.getGameTeam().data.getAttributString(this.name.toLowerCase(),attribut);
	}
	
	public float getMaxSize(){
		if(this.getGameTeam().data.datas.containsKey(this.name)){
			if(this.getGameTeam().data.datas.get(this.name.toLowerCase()).attributs.containsKey(Attributs.size)){
				return getAttribut(Attributs.size)+10f;
			} else if(this.getGameTeam().data.datas.get(this.name.toLowerCase()).attributs.containsKey(Attributs.sizeX)){
				float sizeX=this.getGameTeam().data.getAttribut(this.name.toLowerCase(),Attributs.sizeX);
				float sizeY=this.getGameTeam().data.getAttribut(this.name.toLowerCase(),Attributs.sizeY);
				return (float) Math.sqrt(sizeX*sizeX+sizeY*sizeY)+10f;
			}
		}
		return 1f;
	}


	
}

