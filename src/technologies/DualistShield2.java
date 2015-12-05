package technologies;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import model.GameTeam;
import model.Plateau;
import model.Player;
import units.Character;
import units.UnitKnight;
import units.UnitSpearman;

public class DualistShield2 extends Technologie {

	public DualistShield2(Plateau p, GameTeam gameteam) {
		this.id = 4;
		this.tech = Technologies.DualistShield2;
		this.name = tech.name;
		this.p = p;
		this.gameteam = gameteam;
		this.data = this.gameteam.data;
		try {
			this.icon = new Image("pics/tech/shield2.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void applyEffect(){
		// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
		// Age passing does nothing
		// Then update
		this.gameteam.data.knight.armor+=1;
		this.gameteam.data.priest.armor+=2;
		this.gameteam.data.inquisitor.armor+=2;
		this.gameteam.data.spearman.armor+=1;
		this.gameteam.data.crossbowman.armor+=2;
		// Age passing does nothing
		// Then update all existing units
		for(Character c : this.p.characters){
			if(c.getTeam() == this.gameteam.id){
				if(c instanceof UnitSpearman && c instanceof UnitKnight){
					c.armor+=1;
				}
				else {
					c.armor+=2;
				}
			}
		}
		
	}

	
}
