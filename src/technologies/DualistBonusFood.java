package technologies;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import model.GameTeam;
import model.Plateau;
import model.Player;

public class DualistBonusFood extends Technologie {

	public DualistBonusFood(Plateau p, GameTeam gameteam) {
		this.id = 2;
		this.tech = Technologies.DualistBonusFood;
		this.name = tech.name;
		this.p = p;
		this.gameteam = gameteam;
		this.data = this.gameteam.data;
		try {
			this.icon = new Image("pics/tech/bonusFood.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void applyEffect(){
		this.data.bonusFood=1;
		//Update
		
		
	}

	
}
