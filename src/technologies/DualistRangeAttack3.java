package technologies;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import model.GameTeam;
import model.Plateau;
import model.Player;
import units.Character;
import units.UnitCrossbowman;
import units.UnitInquisitor;

public class DualistRangeAttack3 extends Technologie {

	public DualistRangeAttack3(Plateau p, GameTeam gameteam) {
		this.id = 11;
		this.tech = Technologies.DualistRangeAttack3;
		this.name = tech.name;
		this.p = p;
		this.gameteam = gameteam;
		this.data = this.gameteam.data;
		try {
			this.icon = new Image("pics/tech/rangeAttack3.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
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