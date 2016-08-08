package technologies;


import data.Attributs;
import model.Game;
import model.GameTeam;
import model.Plateau;
import units.Character;
import utils.ObjetsList;

public class DualistHealth2 extends Technologie {

	public DualistHealth2(int team) {

		this.id = 5;
		this.tech = Technologies.DualistHealth2;
		this.initialize(team,tech);
	}
	
	
	public DualistHealth2(int team,ObjetsList o) {
		this.objet = o;
		this.id = 5;
		this.tech = Technologies.DualistHealth2;
		this.initialize(team,tech);
	}
	public void applyEffect(){
		// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
		this.getGameTeam().data.addAttribut(ObjetsList.Spearman.name, Attributs.maxLifepoints, 20f);
		this.getGameTeam().data.addAttribut(ObjetsList.Crossbowman.name, Attributs.maxLifepoints, 20f);
		this.getGameTeam().data.addAttribut(ObjetsList.Knight.name, Attributs.maxLifepoints, 20f);
		this.getGameTeam().data.addAttribut(ObjetsList.Priest.name, Attributs.maxLifepoints, 20f);
		this.getGameTeam().data.addAttribut(ObjetsList.Inquisitor.name, Attributs.maxLifepoints, 20f);
		this.getGameTeam().data.addAttribut(ObjetsList.Archange.name, Attributs.maxLifepoints, 20f);
	}

	
}
