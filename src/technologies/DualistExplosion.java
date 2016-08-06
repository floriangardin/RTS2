package technologies;

import model.Game;
import model.GameTeam;
import model.Plateau;
import units.Character;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class DualistExplosion extends Technologie {

	public DualistExplosion(GameTeam gameteam) {
		this.id = 12;
		this.tech = Technologies.DualistExplosion;
		this.initialize(gameteam, tech);
	}
	
	public void applyEffect(){
		// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
		// Age passing does nothing
		// Then update
		//TODO change effect
		this.gameteam.data.explosionWhenImmolate= true;
		
		for(Character c : Game.g.plateau.characters){
			c.explosionWhenImmolate = true;
		}

		
	}

	
}
