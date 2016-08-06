package technologies;

import model.GameTeam;
import model.Plateau;

public class DualistBonusGold extends Technologie {

	public DualistBonusGold(GameTeam gameteam) {
		this.id = 3;
		this.tech = Technologies.DualistBonusGold;
		this.initialize( gameteam,tech);
	}
	
	public void applyEffect(){
		this.data.bonusGold=4;
		//Update
		
		
	}

	
}
