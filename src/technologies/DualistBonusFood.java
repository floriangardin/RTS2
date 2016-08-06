package technologies;


import model.GameTeam;
import model.Plateau;

public class DualistBonusFood extends Technologie {

	public DualistBonusFood(int team) {
		this.id = 2;
		this.tech = Technologies.DualistBonusFood;
		this.initialize(team,tech);
	}
	
	public void applyEffect(){
		this.getGameTeam().data.bonusFood=4;
		//Update
		
		
	}

	
}
