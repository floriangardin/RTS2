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

	public String toString(){
		String s = toString1()+toString2();
		return s;
	}
	
	
	public void parse(HashMap<String,String> hs){
		//SEPARATION BETWEEN KEYS

		this.parse1(hs);
		this.parse2(hs);
		

	}

	public static Bullet createNewBullet(HashMap<String,String> hs,Game g){
		Bullet c;
		//Get back the owner 
		Character cha = g.plateau.getCharacterById(Integer.parseInt(hs.get("id")));
		if(!hs.containsKey("name")){
			return null;
		}
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
