package technologies;

import org.newdawn.slick.Image;

import model.Data;
import model.Game;
import model.GameTeam;
import model.Plateau;

public abstract class Technologie {

	public Technologies tech;
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
	
	
	public static Technologie technologie(int i, GameTeam gameteam){
		switch(i){
		case 0: return new DualistAge2(gameteam);
		case 1: return new DualistAge3(gameteam);
		case 2: return new DualistBonusFood(gameteam);
		case 3: return new DualistBonusGold(gameteam);
		case 4: return new 	DualistShield2(gameteam);
		case 5: return new DualistHealth2(gameteam);
		case 6: return new 	DualistShield3(gameteam);
		case 7: return new DualistHealth3(gameteam);
		case 8: return new 	DualistContact2(gameteam);
		case 9: return new DualistRangeAttack2(gameteam);
		case 10: return new DualistContact3(gameteam);
		case 11: return new DualistRangeAttack3(gameteam);
		case 12: return new DualistExplosion(gameteam);
		case 13: return new DualistEagleView(gameteam);
		default : return null;
		}
	}
	
	public boolean equals(Object o){
		return o instanceof Technologie && ((Technologie)o).id==this.id;
	}
	
	public void initialize(GameTeam gameteam, Technologies tech){
		this.name = tech.name;
		this.iconName  = tech.nameIcon;
		this.gameteam = gameteam;
		this.data = this.gameteam.data;
		this.icon = Game.g.images.get(iconName);		
	}
}
