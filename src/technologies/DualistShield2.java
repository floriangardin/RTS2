package technologies;

import data.Attributs;
import model.Game;
import model.GameTeam;
import model.Plateau;
import units.Character;
import units.UnitKnight;
import units.UnitSpearman;
import utils.UnitsList;

public class DualistShield2 extends Technologie {

	public DualistShield2( GameTeam gameteam) {
		this.id = 4;
		this.tech = Technologies.DualistShield2;
		this.initialize(gameteam,tech);
	}
	
	public void applyEffect(){
		// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
		this.gameteam.data.addAttribut(UnitsList.Spearman.name, Attributs.armor, 2f);
		this.gameteam.data.addAttribut(UnitsList.Crossbowman.name, Attributs.armor, 2f);
		this.gameteam.data.addAttribut(UnitsList.Knight.name, Attributs.armor, 2f);
		this.gameteam.data.addAttribut(UnitsList.Priest.name, Attributs.armor, 2f);
		this.gameteam.data.addAttribut(UnitsList.Inquisitor.name, Attributs.armor, 2f);
		this.gameteam.data.addAttribut(UnitsList.Archange.name, Attributs.armor, 2f);
	}

	
}
