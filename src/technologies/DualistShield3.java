package technologies;


import model.GameTeam;
import model.Plateau;
import units.Character;

public class DualistShield3 extends Technologie {

	public DualistShield3(Plateau p, GameTeam gameteam) {
		this.id = 6;
		this.tech = Technologies.DualistShield3;
		this.initialize(p, gameteam,tech);
	}
	
	public void applyEffect(){
		// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
		// Age passing does nothing
		// Then update
		this.gameteam.data.knight.armor+=1;
		this.gameteam.data.priest.armor+=1;
		this.gameteam.data.inquisitor.armor+=1;
		this.gameteam.data.spearman.armor+=1;
		this.gameteam.data.crossbowman.armor+=1;
		// Age passing does nothing
		// Then update all existing units
		for(Character c : this.p.characters){
			if(c.getTeam() == this.gameteam.id){
				c.armor+=1;
			}
		}
		

	}
}
