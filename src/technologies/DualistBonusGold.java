package technologies;

import model.GameTeam;
import model.Plateau;

public class DualistBonusGold extends Technologie {

	public DualistBonusGold(int team) {
		this.id = 3;
		this.tech = Technologies.DualistBonusGold;
		this.initialize(team,tech);
	}
	
	public void applyEffect(){
		this.getGameTeam().data.bonusGold=4;
		//Update
		
		
	}

	
}
