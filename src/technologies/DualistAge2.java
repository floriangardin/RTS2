package technologies;

import model.Plateau;
import model.Player;

public class DualistAge2 extends Technologie {

	public DualistAge2(Plateau p, Player player) {
		this.tech = tech.DualistAge2;
		this.p = p;
		this.player = player;
		this.data = this.player.data;
		
		
	}
	
	public void applyEffect(){
		// Va chercher le player.data correspondant et ajoute le bonus ou ajoute tech concerné
		
	}

	
}
