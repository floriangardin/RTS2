package technologies;

import java.util.Vector;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import model.Plateau;
import model.Player;

public class DualistBonusFood extends Technologie {

	public DualistBonusFood(Plateau p, Player player) {
		this.tech = tech.DualistBonusFood;
		this.name = "bonus food";
		this.p = p;
		this.player = player;
		this.data = this.player.data;
		try {
			this.icon = new Image("pics/tech/bonusFood.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void applyEffect(){
		// Va chercher le player.data correspondant et ajoute le bonus ou ajoute tech concerné
		// Age passing does nothing
		// Then update
		
	}

	
}
