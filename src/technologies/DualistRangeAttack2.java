package technologies;


import model.GameTeam;
import model.Plateau;
import units.Character;
import units.UnitCrossbowman;
import units.UnitInquisitor;

public class DualistRangeAttack2 extends Technologie {

	public DualistRangeAttack2(Plateau p, GameTeam gameteam) {
		this.id = 9;
		this.tech = Technologies.DualistRangeAttack2;
		this.initialize(p, gameteam,tech);
	}
	
	public void applyEffect(){
		// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
		// Age passing does nothing
		// Then update
		this.gameteam.data.crossbowman.damage+=1;
		this.gameteam.data.inquisitor.damage+=1;

		// Age passing does nothing
		// Then update all existing units
		for(Character c : this.p.characters){
			if(c.getTeam() == this.gameteam.id){
				if(c instanceof UnitCrossbowman || c instanceof UnitInquisitor){
					c.damage+=1;
					c.damage= c.damage;
				}
				
			}
		}
		

	}
}