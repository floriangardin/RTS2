package technologies;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import model.GameTeam;
import model.Plateau;
import model.Player;
import units.Character;

public class DualistShield3 extends Technologie {

	public DualistShield3(Plateau p, GameTeam gameteam) {
		this.id = 6;
		this.tech = Technologies.DualistShield3;
		this.name = tech.name;
		this.p = p;
		this.gameteam = gameteam;
		this.data = this.gameteam.data;
		try {
			this.icon = new Image("pics/tech/shield3.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
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
