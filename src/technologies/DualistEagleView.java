package technologies;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import model.GameTeam;
import model.Plateau;
import model.Player;
import units.Character;

public class DualistEagleView extends Technologie {

	public DualistEagleView(Plateau p, GameTeam gameteam) {
		this.id = 13;
		this.tech = Technologies.EagleView;
		this.name = "Eagle View";
		this.p = p;
		this.gameteam = gameteam;
		this.data = this.gameteam.data;

		try {
			this.icon = new Image("pics/tech/eagleView.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public void applyEffect(){
		// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
		this.gameteam.data.knight.sight*=1.5;
		this.gameteam.data.priest.sight*=1.5;
		this.gameteam.data.inquisitor.sight*=1.5;
		this.gameteam.data.spearman.sight*=1.5;
		this.gameteam.data.crossbowman.sight*=1.5;
		// Age passing does nothing
		// Then update all existing units
		for(Character c : this.p.characters){
			if(c.getTeam() == this.gameteam.id){
				c.sight*=1.5;
			}

		}

	}
}
