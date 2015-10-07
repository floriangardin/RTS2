package technologies;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import model.Plateau;
import model.Player;
import units.Character;

public class DualistEagleView extends Technologie {

	public DualistEagleView(Plateau p, Player player) {
		this.id = 13;
		this.tech = Technologies.EagleView;
		this.name = "Eagle View";
		this.p = p;
		this.player = player;
		this.data = this.player.data;
		
		try {
			this.icon = new Image("pics/tech/eagleView.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void applyEffect(){
		// Va chercher le player.data correspondant et ajoute le bonus ou ajoute tech concerné
		this.player.data.knight.sight*=1.5;
		this.player.data.priest.sight*=1.5;
		this.player.data.inquisitor.sight*=1.5;
		this.player.data.spearman.sight*=1.5;
		this.player.data.crossbowman.sight*=1.5;
		// Age passing does nothing
		// Then update all existing units
		for(Character c : this.p.characters){
			if(c.team == this.player.team){
				c.sight*=1.5;
			}
		}
		
	}
}
