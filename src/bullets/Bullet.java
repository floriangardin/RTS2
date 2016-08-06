package bullets;




import java.util.HashMap;

import buildings.Building;
import data.Attributs;
import model.Game;
import model.Objet;
import units.Character;

public abstract class Bullet extends Objet {
	public float damage;
	public float areaEffect;
	public Objet owner;
	public float size;
	public String soundLaunch;

	public void collision(Building c){

	}


	public static Bullet createNewBullet(HashMap<String,String> hs){
		Bullet c=null;
		//Get back the owner 
		Character cha = Game.g.plateau.getCharacterById(Integer.parseInt(hs.get("ownerid")));
		float vx = Float.parseFloat(hs.get("vxtarget"));
		float vy = Float.parseFloat(hs.get("vytarget"));


		switch(hs.get("name")){
		case "arrow":
			c =  new Arrow(cha,vx,vy,cha.getAttribut(Attributs.damage),Integer.parseInt(hs.get("id")));
			break;
		case "fireball":
			if(hs.containsKey("targetX") && hs.containsKey("targetY") ){
				float targetX = Float.parseFloat(hs.get("targetX"));
				float targetY = Float.parseFloat(hs.get("targetY"));
				c =  new Fireball(cha,targetX,targetY,vx,vy,cha.getAttribut(Attributs.damage),Integer.parseInt(hs.get("id")));
			}	
			break;
		default:
			c = null;
		}
		return c;

	}
}
