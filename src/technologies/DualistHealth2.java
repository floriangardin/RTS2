package technologies;


import data.Attributs;
import model.Game;
import model.GameTeam;
import model.Plateau;
import units.Character;
import utils.UnitsList;

public class DualistHealth2 extends Technologie {

	public DualistHealth2(int team) {

		this.id = 5;
		this.tech = Technologies.DualistHealth2;
		this.initialize(team,tech);
	}
	
	public void applyEffect(){
		// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
		this.getGameTeam().data.addAttribut(UnitsList.Spearman.name, Attributs.maxLifepoints, 20f);
		this.getGameTeam().data.addAttribut(UnitsList.Crossbowman.name, Attributs.maxLifepoints, 20f);
		this.getGameTeam().data.addAttribut(UnitsList.Knight.name, Attributs.maxLifepoints, 20f);
		this.getGameTeam().data.addAttribut(UnitsList.Priest.name, Attributs.maxLifepoints, 20f);
		this.getGameTeam().data.addAttribut(UnitsList.Inquisitor.name, Attributs.maxLifepoints, 20f);
		this.getGameTeam().data.addAttribut(UnitsList.Archange.name, Attributs.maxLifepoints, 20f);
	}

	
}
