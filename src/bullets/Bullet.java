package bullets;




import java.util.HashMap;

import buildings.Building;
import model.ActionObjet;
import model.Game;
import multiplaying.OutputModel.OutputBullet;
import units.Character;
import units.UnitArchange;
import units.UnitCrossbowman;
import units.UnitInquisitor;
import units.UnitKnight;
import units.UnitPriest;
import units.UnitSpearman;
import units.UnitTest;

public abstract class Bullet extends ActionObjet {
	public float damage;
	public float areaEffect;
	public Character owner;

		
	public void collision(Building c){
		
	}

	public String toStringBullet(){
		String s = "";
		if(changes.ownerId){
			s+="ownerid:"+owner.id+";";
			changes.ownerId = true;
		}
		if(changes.ownerV){
			s+="vxtarget:"+(this.vx)+";";
			s+="vytarget:"+(this.vy)+";";
			changes.ownerV =true;
		}
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
		Bullet c;
		//Get back the owner 
		Character cha = g.plateau.getCharacterById(Integer.parseInt(hs.get("ownerid")));
		float vx = Float.parseFloat(hs.get("vxtarget"));
		float vy = Float.parseFloat(hs.get("vytarget"));
		float targetX = Float.parseFloat(hs.get("targetX"));
		float targetY = Float.parseFloat(hs.get("targetY"));
		switch(hs.get("name")){
		case "arrow":
			c =  new Arrow(g.plateau,cha,vx,vy,cha.damage,Integer.parseInt(hs.get("id")));
			break;
		case "fireball":
			c =  new Fireball(g.plateau,cha,targetX,targetY,vx,vy,cha.damage,Integer.parseInt(hs.get("id")));	
			break;
		default:
			c = null;
		}
		return c;
		
	}
}
