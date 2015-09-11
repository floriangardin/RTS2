package technologies;

import java.util.Vector;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import model.Plateau;
import model.Player;

public class DualistBonusGold extends Technologie {

	public DualistBonusGold(Plateau p, Player player) {
		this.tech = Technologies.DualistBonusGold;
		this.name = tech.name;
		this.p = p;
		this.player = player;
		this.data = this.player.data;
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
