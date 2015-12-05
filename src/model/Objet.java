package model;


import java.util.HashMap;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

import bullets.Bullet;
import pathfinding.Case;
import units.Character;

public abstract class Objet implements java.io.Serializable {

	// Animation : mode,orientation,increment
	public float maxLifePoints;
	public int id;
	public Image[][][] animations;
	public int mode;
	public int orientation=2;
	public int increment;
	public float incrementf;
	public Image selection_circle;
	public Sound sound;
	public float x;
	public float y;
	public float sight;
	public Case c;
	public Shape collisionBox;
	public Shape selectionBox;
	public Color color;
	public Plateau p;
	public float lifePoints;
	public String name;
	private int team;
	private GameTeam gameteam;

	// visibility boolean 
	public boolean visibleByCurrentPlayer;
	public boolean visibleByCamera;
	public Image image;
	//MULTIPLAYING BOOLEANS
	public Changes changes=new Changes();

	public void setName(String s){
		this.name = s;
	}
	public String getName(){
		return this.name;
	}
	public int getTeam(){
		if(this.gameteam==null)
			return 0;
		return gameteam.id;
	}
	public GameTeam getGameTeam(){
		return gameteam;
	}
	public void setTeam(int i){
		this.team = i;
		for(GameTeam t : this.p.teams){
			if(t.id==i){
				this.gameteam = t;
			}
		}
	}
	public void setTeam(GameTeam g){
		this.team = g.id;
		this.gameteam = g;
	}
	protected void destroy(){
		this.lifePoints = -10;

		this.x = -10f;
		this.y = -10f;
	}
	public Graphics draw(Graphics g){
		return g;}
	protected void collision(Objet o){}
	public void collision(Character c){}
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
			
			float xt = Math.min(this.p.maxX-1f, Math.max(1f, x));
			float yt = Math.min(this.p.maxY-1f, Math.max(1f, y));
			
			this.x = xt;
			this.y = yt;
		}
		this.collisionBox.setCenterX(x);
		this.collisionBox.setCenterY(y);
		
		this.c = this.p.mapGrid.getCase(x, y);
		this.changes.x=true;
		this.changes.y = true;
	}

	public boolean isAlive(){
		return this.lifePoints>0f;
	}
	
	public void setLifePoints(float lifepoints){
		if(lifepoints<this.maxLifePoints)
			this.lifePoints= lifepoints;
		else{
			this.lifePoints = this.maxLifePoints;
		}
		this.changes.lifePoints = true;
	}
	// TOSTRING METHODS
	public String toStringObjet(){
		String s="";
		s+="id:"+id+";";
		s+="name:"+name+";";
		if(changes.team){
			s+="team:"+team+";";
			changes.team = true;
		}
		if(changes.x){
			s+="x:"+x+";";
			changes.x = false;
		}
		if(changes.y){
			s+="y:"+y+";";
			changes.y = false;
		}
		if(changes.orientation){
			s+="orientation:"+orientation+";";
			changes.orientation = false;
		}
		if(changes.lifePoints){
			s+="lifePoints:"+lifePoints+";";
			changes.lifePoints = true;
		}
		if(changes.sight){
			s+="sight:"+sight+";";
			changes.sight = false;
		}
		return s;
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
	public void parseObjet(HashMap<String,String> hs){
		if(hs.containsKey("x")){
			this.setXY(Float.parseFloat(hs.get("x")),Float.parseFloat(hs.get("y")));
		}
		if(hs.containsKey("lifePoints")){
			this.lifePoints=Float.parseFloat(hs.get("lifePoints"));
		}
		if(hs.containsKey("sight")){
			this.sight=Float.parseFloat(hs.get("sight"));
		}
		if(hs.containsKey("team")){
			this.setTeam(Integer.parseInt(hs.get("team")));
		}
		if(hs.containsKey("orientation")){
			this.orientation = Integer.parseInt(hs.get("orientation"));
		}
	}
	public void parse(HashMap<String, String> hs) {
		
	}

	
	
}

