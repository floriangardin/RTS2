package technologies;

import utils.ObjetsList;
import model.GameTeam;
import model.Plateau;

public class DualistBonusGold extends Technologie {

	public DualistBonusGold(int team) {
		this.id = 3;
		this.tech = Technologies.DualistBonusGold;
		this.initialize(team,tech);
	}
	public DualistBonusGold(int team,ObjetsList o) {
		this.objet = o;
		this.id = 0;
		this.tech = Technologies.DualistAge2;
		this.initialize(team,tech);
	}
	public void applyEffect(){
		this.getGameTeam().data.bonusGold=4;
		//Update
		
		
	}

	
}
