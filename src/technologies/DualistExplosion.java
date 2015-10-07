package technologies;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import model.Plateau;
import model.Player;

public class DualistExplosion extends Technologie {

	public DualistExplosion(Plateau p, Player player) {
		this.id = 12;
		this.tech = Technologies.DualistExplosion;
		this.name = tech.name;
		this.p = p;
		this.player = player;
		this.data = this.player.data;
		try {
			this.icon = new Image("pics/tech/explosion.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void applyEffect(){
		// Va chercher le player.data correspondant et ajoute le bonus ou ajoute tech concerné
		// Age passing does nothing
		// Then update
		//TODO change effect

		
	}

	
}
