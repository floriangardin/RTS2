package technologies;


import utils.ObjetsList;
import model.GameTeam;
import model.Plateau;

public class DualistBonusFood extends Technologie {

	public DualistBonusFood(int team) {
		this.id = 2;
		this.tech = Technologies.DualistBonusFood;
		this.initialize(team,tech);
	}
	public DualistBonusFood(int team,ObjetsList o) {
		this.objet = o;
		this.id = 0;
		this.tech = Technologies.DualistAge2;
		this.initialize(team,tech);
	}
	public void applyEffect(){
		this.getGameTeam().data.bonusFood=4;
		//Update
		
		
	}

	
}
