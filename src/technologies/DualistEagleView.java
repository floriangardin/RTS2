package technologies;


import data.Attributs;
import model.Game;
import model.GameTeam;
import model.Plateau;
import units.Character;
import utils.UnitsList;

public class DualistEagleView extends Technologie {

	public DualistEagleView(int team) {
		this.id = 13;
		this.tech = Technologies.EagleView;
		this.initialize(team,tech);
	}

	public void applyEffect(){
		// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
		this.getGameTeam().data.mulAttribut(UnitsList.Spearman.name, Attributs.sight, 1.5f);
		this.getGameTeam().data.mulAttribut(UnitsList.Crossbowman.name, Attributs.sight, 1.5f);
		this.getGameTeam().data.mulAttribut(UnitsList.Knight.name, Attributs.sight, 1.5f);
		this.getGameTeam().data.mulAttribut(UnitsList.Priest.name, Attributs.sight, 1.5f);
		this.getGameTeam().data.mulAttribut(UnitsList.Inquisitor.name, Attributs.sight, 1.5f);
		this.getGameTeam().data.mulAttribut(UnitsList.Archange.name, Attributs.sight, 1.5f);
	}
}
