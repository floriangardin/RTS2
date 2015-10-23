package technologies;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import model.GameTeam;
import model.Plateau;
import model.Player;
import units.Character;
import units.UnitKnight;
import units.UnitSpearman;

public class DualistContact3 extends Technologie {

	public DualistContact3(Plateau p, GameTeam gameteam) {
		this.id = 11;
		this.tech = Technologies.DualistContactAttack3;
		this.name = tech.name;
		this.p = p;
		this.gameteam = gameteam;
		this.data = this.gameteam.data;
		try {
			this.icon = new Image("pics/tech/contact3.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
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
					c.armor+=1;
				}
				
			}
		}
		

	}
}