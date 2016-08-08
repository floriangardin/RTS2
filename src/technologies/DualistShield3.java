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
		this.getGameTeam().data.addAttribut(ObjetsList.Spearman.name, Attributs.armor, 2f);
		this.getGameTeam().data.addAttribut(ObjetsList.Crossbowman.name, Attributs.armor, 2f);
		this.getGameTeam().data.addAttribut(ObjetsList.Knight.name, Attributs.armor, 2f);
		this.getGameTeam().data.addAttribut(ObjetsList.Priest.name, Attributs.armor, 2f);
		this.getGameTeam().data.addAttribut(ObjetsList.Inquisitor.name, Attributs.armor, 2f);
		this.getGameTeam().data.addAttribut(ObjetsList.Archange.name, Attributs.armor, 2f);
	}
}
