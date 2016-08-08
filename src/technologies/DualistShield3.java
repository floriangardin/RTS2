package technologies;


import data.Attributs;
import model.Game;
import model.GameTeam;
import model.Plateau;
import units.Character;
import utils.ObjetsList;

public class DualistShield3 extends Technologie {

	public DualistShield3( int team) {
		this.id = 6;
		this.tech = Technologies.DualistShield3;
		this.initialize(team,tech);
	}
	public DualistShield3(int team,ObjetsList o) {
		this.objet = o;
		this.id = 5;
		this.tech = Technologies.DualistShield3;
		this.initialize(team,tech);
	}
	
	public void applyEffect(){
		// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
		for(ObjetsList o : ObjetsList.getUnits()){
			this.getGameTeam().data.addAttribut(o, Attributs.armor, 2f);
		}
	}
}
