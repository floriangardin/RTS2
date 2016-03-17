package technologies;

import org.newdawn.slick.Image;

import model.Data;
import model.Game;
import model.GameTeam;
import model.Plateau;

public abstract class Technologie {

	public Technologies tech;
	public Plateau p;
	public GameTeam gameteam;
	public Data data;
	public boolean isDiscovered;
	public Technologie techRequired;
	public Image icon;
	public String name;
	public String iconName;
	public int id;
	public void applyEffect(){
	}
	
	
	public static Technologie technologie(int i,Plateau p , GameTeam gameteam){
		switch(i){
		case 0: return new DualistAge2(p,gameteam);
		case 1: return new DualistAge3(p,gameteam);
		case 2: return new DualistBonusFood(p,gameteam);
		case 3: return new DualistBonusGold(p,gameteam);
		case 4: return new 	DualistShield2(p,gameteam);
		case 5: return new DualistHealth2(p,gameteam);
		case 6: return new 	DualistShield3(p,gameteam);
		case 7: return new DualistHealth3(p,gameteam);
		case 8: return new 	DualistContact2(p,gameteam);
		case 9: return new DualistRangeAttack2(p,gameteam);
		case 10: return new DualistContact3(p,gameteam);
		case 11: return new DualistRangeAttack3(p,gameteam);
		case 12: return new DualistExplosion(p,gameteam);
		case 13: return new DualistEagleView(p,gameteam);
		default : return null;
		}
	}
	
	public boolean equals(Object o){
		return o instanceof Technologie && ((Technologie)o).id==this.id;
	}
	
	public void initialize(Plateau p, GameTeam gameteam, Technologies tech){
		this.name = tech.name;
		this.iconName  = tech.nameIcon;
		this.p = p;
		this.gameteam = gameteam;
		this.data = this.gameteam.data;
		this.icon = Game.g.images.get(iconName);		
	}
}
