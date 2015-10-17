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
	public int id;

		
	public void change(OutputBullet ocb){
		this.x = ocb.x;
		this.y = ocb.y;
	}
	
	
	public void collision(Building c){
		
	}

	
	public String toString3(){
		String s = "";
		if(changes.ownerId){
			s+="ownerid:"+owner.id+";";
			changes.ownerId = true;
		}
		return s;
		
	}
	public String toString(){
		return toString1()+toString2()+toString3();
		
	}
	
	
	
	public void parse3(HashMap<String,String> hs){
		if(hs.containsKey("ownerid")){
			//ALREADY DONE IN CREATE BULLET
		}
	}
	public void parse(HashMap<String,String> hs){
		//SEPARATION BETWEEN KEYS

		this.parse1(hs);
		this.parse2(hs);
		
		
		

	}
	

	public static Bullet createNewBullet(HashMap<String,String> hs,Game g){
		Bullet c;
		//Get back the owner 
		Character cha = g.plateau.getCharacterById(Integer.parseInt(hs.get("ownerid")));
		switch(hs.get("name")){
		case "arrow":
			c =  new Arrow(g.plateau,cha,cha.damage);
		case "fireball":
			c =  new Fireball(g.plateau,cha,cha.damage);	
			break;
		default:
			c = null;
		}
		if(c!=null){
			c.parse(hs);
		}
		
		return c;
		
	}
}
