package technologies;


import model.Game;
import model.GameTeam;
import model.Plateau;
import units.Character;
import units.UnitKnight;
import units.UnitSpearman;

public class DualistContact3 extends Technologie {

	public DualistContact3(GameTeam gameteam) {
		this.id = 11;
		this.tech = Technologies.DualistContactAttack3;
		this.initialize(gameteam, tech);
	}
	
	public void applyEffect(){
		// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
		// Age passing does nothing
		// Then update
		this.gameteam.data.knight.damage+=1;
		this.gameteam.data.spearman.damage+=1;

		// Age passing does nothing
		// Then update all existing units
		for(Character c : Game.g.plateau.characters){
			if(c.getTeam() == this.gameteam.id){
				if(c instanceof UnitKnight || c instanceof UnitSpearman){
					c.armor+=1;
				}
				
			}
		}
		

	}
}