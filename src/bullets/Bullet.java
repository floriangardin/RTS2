package bullets;




import java.util.HashMap;

import org.newdawn.slick.Image;

import buildings.Building;
import model.ActionObjet;
import model.Game;
import units.Character;

public abstract class Bullet extends ActionObjet {
	public float damage;
	public float areaEffect;
	public ActionObjet owner;
	public float size;

	public void collision(Building c){

	}

	public String toStringBullet(){
		String s = "";

		s+="ownerid:"+owner.id+";";
		s+="vxtarget:"+(this.vx)+";";
		s+="vytarget:"+(this.vy)+";";

		return s;
	}
	public String toString(){
		return toStringObjet()+toStringActionObjet()+toStringBullet();
	}

	public void parseBullet(HashMap<String,String> hs){
		if(hs.containsKey("ownerid")){
			//ALREADY DONE IN CREATE BULLET
		}
	}
	public void parse(HashMap<String,String> hs){
		//SEPARATION BETWEEN KEYS
		this.parseObjet(hs);
		this.parseActionObjet(hs);
	}

	public static Bullet createNewBullet(HashMap<String,String> hs,Game g){
		Bullet c=null;
		//Get back the owner 
		Character cha = g.plateau.getCharacterById(Integer.parseInt(hs.get("ownerid")));
		float vx = Float.parseFloat(hs.get("vxtarget"));
		float vy = Float.parseFloat(hs.get("vytarget"));


		switch(hs.get("name")){
		case "arrow":
			c =  new Arrow(g.plateau,cha,vx,vy,cha.damage,Integer.parseInt(hs.get("id")));
			break;
		case "fireball":
			if(hs.containsKey("targetX") && hs.containsKey("targetY") ){
				float targetX = Float.parseFloat(hs.get("targetX"));
				float targetY = Float.parseFloat(hs.get("targetY"));
				c =  new Fireball(g.plateau,cha,targetX,targetY,vx,vy,cha.damage,Integer.parseInt(hs.get("id")));
			}	
			break;
		default:
			c = null;
		}
		return c;

	}
}
