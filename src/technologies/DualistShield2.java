package technologies;

import data.Attributs;
import utils.ObjetsList;

public class DualistShield2 extends Technologie {

	public DualistShield2( int team) {
		this.id = 4;
		this.tech = Technologies.DualistShield2;
		this.initialize(team,tech);
	}
	
	public DualistShield2(int team,ObjetsList o) {
		this.objet = o;
		this.id = 0;
		this.tech = Technologies.DualistShield2;
		this.initialize(team,tech);
	}
	
	public void applyEffect(){
		// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
		for(ObjetsList o : ObjetsList.getUnits()){
			this.getGameTeam().data.addAttribut(o, Attributs.armor, 2f);
		}
	}

	
}
