package technologies;

import org.newdawn.slick.Image;

import data.Data;
import model.Game;
import model.GameTeam;

public abstract class Technologie implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3252944888737911231L;


	public Technologies tech;
	public int team;
	public boolean isDiscovered;
	public Technologie techRequired;
	public String icon;
	public String name;
	public String iconName;
	public int id;
	public void applyEffect(){
	}
	public GameTeam getGameTeam(){
		return Game.g.teams.get(this.team);
	}
	
	public static Technologie technologie(int i, int gameteam){
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
	
	public void initialize(int team, Technologies tech){
		this.name = tech.name;
		this.icon = tech.nameIcon;
		this.team = team;		
	}
}
