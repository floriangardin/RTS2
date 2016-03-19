package technologies;


import model.GameTeam;
import model.Plateau;

public class DualistBonusFood extends Technologie {

	public DualistBonusFood(Plateau p, GameTeam gameteam) {
		this.id = 2;
		this.tech = Technologies.DualistBonusFood;
		this.initialize(p, gameteam,tech);
	}
	
	public void applyEffect(){
		this.data.bonusFood=4;
		//Update
		
		
	}

	
}
