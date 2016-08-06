package technologies;


import data.Attributs;
import model.Game;
import model.GameTeam;
import model.Plateau;
import units.Character;
import utils.UnitsList;

public class DualistHealth2 extends Technologie {

	public DualistHealth2(GameTeam gameteam) {

		this.id = 5;
		this.tech = Technologies.DualistHealth2;
		this.initialize(gameteam,tech);
	}
	
	public void applyEffect(){
		// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
		this.gameteam.data.addAttribut(UnitsList.Spearman.name, Attributs.maxLifepoints, 20f);
		this.gameteam.data.addAttribut(UnitsList.Crossbowman.name, Attributs.maxLifepoints, 20f);
		this.gameteam.data.addAttribut(UnitsList.Knight.name, Attributs.maxLifepoints, 20f);
		this.gameteam.data.addAttribut(UnitsList.Priest.name, Attributs.maxLifepoints, 20f);
		this.gameteam.data.addAttribut(UnitsList.Inquisitor.name, Attributs.maxLifepoints, 20f);
		this.gameteam.data.addAttribut(UnitsList.Archange.name, Attributs.maxLifepoints, 20f);
	}

	
}
