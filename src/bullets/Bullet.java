package bullets;




import java.util.HashMap;

import buildings.Building;
import model.Game;
import model.Objet;
import units.Character;

public abstract class Bullet extends Objet {
	public float damage;
	public float areaEffect;
	public Objet owner;
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

	public static Bullet createNewBullet(HashMap<String,String> hs){
		Bullet c=null;
		//Get back the owner 
		Character cha = Game.g.plateau.getCharacterById(Integer.parseInt(hs.get("ownerid")));
		float vx = Float.parseFloat(hs.get("vxtarget"));
		float vy = Float.parseFloat(hs.get("vytarget"));


		switch(hs.get("name")){
		case "arrow":
			c =  new Arrow(Game.g.plateau,cha,vx,vy,cha.damage,Integer.parseInt(hs.get("id")));
			break;
		case "fireball":
			if(hs.containsKey("targetX") && hs.containsKey("targetY") ){
				float targetX = Float.parseFloat(hs.get("targetX"));
				float targetY = Float.parseFloat(hs.get("targetY"));
				c =  new Fireball(Game.g.plateau,cha,targetX,targetY,vx,vy,cha.damage,Integer.parseInt(hs.get("id")));
			}	
			break;
		default:
			c = null;
		}
		return c;

	}
}
