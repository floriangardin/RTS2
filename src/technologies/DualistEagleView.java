package technologies;

import java.util.Vector;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import model.Plateau;
import model.Player;

public class DualistEagleView extends Technologie {

	public DualistEagleView(Plateau p, Player player) {
		this.tech = tech.DualistAge2;
		this.p = p;
		this.player = player;
		this.data = this.player.data;
		this.techRequired = new Vector<Technologie>();
		try {
			this.icon = new Image("pics/tech/eagleView.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void applyEffect(){
		// Va chercher le player.data correspondant et ajoute le bonus ou ajoute tech concerné
		this.player.data.knight.sight*=2;
		this.player.data.priest.sight*=2;
		this.player.data.inquisitor.sight*=2;
		this.player.data.spearman.sight*=2;
		this.player.data.crossbowman.sight*=2;
		// Age passing does nothing
		// Then update
		
	}
}
