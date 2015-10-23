package technologies;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import model.GameTeam;
import model.Plateau;
import model.Player;
import units.Character;

public class DualistHealth3 extends Technologie {

	public DualistHealth3(Plateau p, GameTeam gameteam) {
		this.id = 7;
		this.tech = Technologies.DualistHealth3;
		this.name = tech.name;
		this.p = p;
		this.gameteam = gameteam;
		this.data = this.gameteam.data;
		try {
			this.icon = new Image("pics/tech/health3.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void applyEffect(){
		// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
		// Age passing does nothing
		// Then update
		this.gameteam.data.knight.maxLifePoints+=10;
		this.gameteam.data.priest.maxLifePoints+=10;
		this.gameteam.data.inquisitor.maxLifePoints+=10;
		this.gameteam.data.spearman.maxLifePoints+=10;
		this.gameteam.data.crossbowman.maxLifePoints+=10;
		// Age passing does nothing
		// Then update all existing units
		for(Character c : this.p.characters){
			if(c.getTeam() == this.gameteam.id){
				c.maxLifePoints+=10;
			}
		}
		
	}

	
}
