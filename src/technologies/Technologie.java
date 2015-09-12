package technologies;

import java.util.Vector;

import org.newdawn.slick.Image;

import model.Data;
import model.Plateau;
import model.Player;

public abstract class Technologie {

	public Technologies tech;
	public Plateau p;
	public Player player;
	public Data data;
	public boolean isDiscovered;
	public Technologie techRequired;
	public Image icon;
	public String name;
	public int id;
	public void applyEffect(){
		

	}
	
	
	
	public static Technologie technologie(int i,Plateau p , Player player){
		switch(i){
		case 0: return new DualistAge2(p,player);
		case 1: return new DualistAge3(p,player);
		case 2: return new DualistBonusFood(p,player);
		case 3: return new DualistBonusGold(p,player);
		case 4: return new 	DualistShield2(p,player);
		case 5: return new DualistHealth2(p,player);
		case 6: return new 	DualistShield3(p,player);
		case 7: return new DualistHealth3(p,player);
		case 8: return new 	DualistContact2(p,player);
		case 9: return new DualistRangeAttack2(p,player);
		case 10: return new DualistContact3(p,player);
		case 11: return new DualistRangeAttack3(p,player);
		case 12: return new DualistExplosion(p,player);
		case 13: return new DualistEagleView(p,player);
		default : return null;
		}
	}
}
