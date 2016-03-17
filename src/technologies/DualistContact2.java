package technologies;


import model.GameTeam;
import model.Plateau;
import units.Character;
import units.UnitKnight;
import units.UnitSpearman;

public class DualistContact2 extends Technologie {

	public DualistContact2(Plateau p, GameTeam gameteam) {
		this.id = 8;
		this.tech = Technologies.DualistContactAttack2;
		this.initialize(p, gameteam,tech);
	}
	
	public void applyEffect(){
		// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
		// Age passing does nothing
		// Then update
		this.gameteam.data.knight.damage+=1;
		this.gameteam.data.spearman.damage+=1;

		// Age passing does nothing
		// Then update all existing units
		for(Character c : this.p.characters){
			if(c.getTeam() == this.gameteam.id){
				if(c instanceof UnitKnight || c instanceof UnitSpearman){
					c.damage+=1;
					
				}
				
			}
		}
		

	}
}