package technologies;


import model.GameTeam;
import model.Plateau;
import units.Character;

public class DualistHealth2 extends Technologie {

	public DualistHealth2(Plateau p, GameTeam gameteam) {

		this.id = 5;
		this.tech = Technologies.DualistHealth2;
		this.initialize(p, gameteam,tech);
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
