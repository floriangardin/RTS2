package technologies;


import data.Attributs;
import model.Game;
import model.GameTeam;
import model.Plateau;
import units.Character;
import utils.ObjetsList;

public class DualistEagleView extends Technologie {

	public DualistEagleView(int team) {
		this.id = 13;
		this.tech = Technologies.EagleView;
		this.initialize(team,tech);
	}

	public void applyEffect(){
		// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
		this.getGameTeam().data.mulAttribut(ObjetsList.Spearman, Attributs.sight, 1.5f);
		this.getGameTeam().data.mulAttribut(ObjetsList.Crossbowman, Attributs.sight, 1.5f);
		this.getGameTeam().data.mulAttribut(ObjetsList.Knight, Attributs.sight, 1.5f);
		this.getGameTeam().data.mulAttribut(ObjetsList.Priest, Attributs.sight, 1.5f);
		this.getGameTeam().data.mulAttribut(ObjetsList.Inquisitor, Attributs.sight, 1.5f);
		this.getGameTeam().data.mulAttribut(ObjetsList.Archange, Attributs.sight, 1.5f);
	}
}
