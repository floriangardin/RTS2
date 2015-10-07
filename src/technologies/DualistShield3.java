package technologies;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import model.Plateau;
import model.Player;
import units.Character;

public class DualistShield3 extends Technologie {

	public DualistShield3(Plateau p, Player player) {
		this.id = 6;
		this.tech = Technologies.DualistShield3;
		this.name = tech.name;
		this.p = p;
		this.player = player;
		this.data = this.player.data;
		try {
			this.icon = new Image("pics/tech/shield3.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void applyEffect(){
		// Va chercher le player.data correspondant et ajoute le bonus ou ajoute tech concerné
		// Age passing does nothing
		// Then update
		this.player.data.knight.armor+=1;
		this.player.data.priest.armor+=1;
		this.player.data.inquisitor.armor+=1;
		this.player.data.spearman.armor+=1;
		this.player.data.crossbowman.armor+=1;
		// Age passing does nothing
		// Then update all existing units
		for(Character c : this.p.characters){
			if(c.team == this.player.team){
				c.armor+=1;
			}
		}
		

	}
}
