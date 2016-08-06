package technologies;


import data.Attributs;
import model.Game;
import model.GameTeam;
import model.Plateau;
import units.Character;
import utils.UnitsList;

public class DualistHealth3 extends Technologie {

	public DualistHealth3( int team) {
		this.id = 7;
		this.tech = Technologies.DualistHealth3;
		this.initialize( team,tech);
	}
	
	public void applyEffect(){
		// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
		this.getGameTeam().data.addAttribut(UnitsList.Spearman.name, Attributs.maxLifepoints, 30f);
		this.getGameTeam().data.addAttribut(UnitsList.Crossbowman.name, Attributs.maxLifepoints, 30f);
		this.getGameTeam().data.addAttribut(UnitsList.Knight.name, Attributs.maxLifepoints, 30f);
		this.getGameTeam().data.addAttribut(UnitsList.Priest.name, Attributs.maxLifepoints, 30f);
		this.getGameTeam().data.addAttribut(UnitsList.Inquisitor.name, Attributs.maxLifepoints, 30f);
		this.getGameTeam().data.addAttribut(UnitsList.Archange.name, Attributs.maxLifepoints, 30f);
	}

	
}
