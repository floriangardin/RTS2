package technologies;

import model.GameTeam;
import model.Plateau;
import units.Character;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class DualistExplosion extends Technologie {

	public DualistExplosion(Plateau p, GameTeam gameteam) {
		this.id = 12;
		this.tech = Technologies.DualistExplosion;
		this.name = tech.name;
		this.p = p;
		this.gameteam = gameteam;
		this.data = this.gameteam.data;
		try {
			this.icon = new Image("pics/tech/explosion.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void applyEffect(){
		// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
		// Age passing does nothing
		// Then update
		//TODO change effect
		this.gameteam.data.explosionWhenImmolate= true;
		
		for(Character c : this.p.characters){
			c.explosionWhenImmolate = true;
		}

		
	}

	
}
