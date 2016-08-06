package technologies;


import data.Attributs;
import model.Game;
import model.GameTeam;
import model.Plateau;
import units.Character;
import utils.UnitsList;

public class DualistShield3 extends Technologie {

	public DualistShield3( int team) {
		this.id = 6;
		this.tech = Technologies.DualistShield3;
		this.initialize(team,tech);
	}
	
	public void applyEffect(){
		// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
		this.getGameTeam().data.addAttribut(UnitsList.Spearman.name, Attributs.armor, 2f);
		this.getGameTeam().data.addAttribut(UnitsList.Crossbowman.name, Attributs.armor, 2f);
		this.getGameTeam().data.addAttribut(UnitsList.Knight.name, Attributs.armor, 2f);
		this.getGameTeam().data.addAttribut(UnitsList.Priest.name, Attributs.armor, 2f);
		this.getGameTeam().data.addAttribut(UnitsList.Inquisitor.name, Attributs.armor, 2f);
		this.getGameTeam().data.addAttribut(UnitsList.Archange.name, Attributs.armor, 2f);
	}
}
