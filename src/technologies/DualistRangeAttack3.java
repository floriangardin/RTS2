package technologies;


import model.Game;
import model.GameTeam;
import model.Plateau;
import units.Character;
import units.UnitCrossbowman;
import units.UnitInquisitor;

public class DualistRangeAttack3 extends Technologie {

	public DualistRangeAttack3(GameTeam gameteam) {
		this.id = 11;
		this.tech = Technologies.DualistRangeAttack3;
		this.initialize( gameteam,tech);
	}
	
	public void applyEffect(){
		// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
		// Age passing does nothing
		// Then update
		this.gameteam.data.crossbowman.damage+=1;
		this.gameteam.data.inquisitor.damage+=1;

		// Age passing does nothing
		// Then update all existing units
		for(Character c : Game.g.plateau.characters){
			if(c.getTeam() == this.gameteam.id){
				if(c instanceof UnitCrossbowman || c instanceof UnitInquisitor){
					c.damage+=1;
					c.damage= c.damage;
				}
			}
		}
		

	}
}