package technologies;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import model.Plateau;
import model.Player;
import units.Character;

public class DualistHealth2 extends Technologie {

	public DualistHealth2(Plateau p, Player player) {

		this.id = 5;
		this.tech = Technologies.DualistHealth2;
		this.name = tech.name;
		this.p = p;
		this.player = player;
		this.data = this.player.data;
		try {
			this.icon = new Image("pics/tech/health2.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void applyEffect(){
		// Va chercher le player.data correspondant et ajoute le bonus ou ajoute tech concerné
		// Age passing does nothing
		// Then update
		this.player.data.knight.maxLifePoints+=10;
		this.player.data.priest.maxLifePoints+=10;
		this.player.data.inquisitor.maxLifePoints+=10;
		this.player.data.spearman.maxLifePoints+=10;
		this.player.data.crossbowman.maxLifePoints+=10;
		// Age passing does nothing
		// Then update all existing units
		for(Character c : this.p.characters){
			if(c.team == this.player.team){
				c.maxLifePoints+=10;
			}
		}
		
	}

	
}
