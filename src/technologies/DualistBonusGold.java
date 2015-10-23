package technologies;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import model.GameTeam;
import model.Plateau;
import model.Player;

public class DualistBonusGold extends Technologie {

	public DualistBonusGold(Plateau p, GameTeam gameteam) {
		this.id = 3;
		this.tech = Technologies.DualistBonusGold;
		this.name = tech.name;
		this.p = p;
		this.gameteam = gameteam;
		this.data = this.gameteam.data;
		try {
			this.icon = new Image("pics/tech/bonusGold.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void applyEffect(){
		this.data.bonusGold=1;
		//Update
		
		
	}

	
}
