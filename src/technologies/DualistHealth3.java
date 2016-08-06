package technologies;


import data.Attributs;
import model.Game;
import model.GameTeam;
import model.Plateau;
import units.Character;
import utils.UnitsList;

public class DualistHealth3 extends Technologie {

	public DualistHealth3( GameTeam gameteam) {
		this.id = 7;
		this.tech = Technologies.DualistHealth3;
		this.initialize( gameteam,tech);
	}
	
	public void applyEffect(){
		// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
		this.gameteam.data.addAttribut(UnitsList.Spearman.name, Attributs.maxLifepoints, 30f);
		this.gameteam.data.addAttribut(UnitsList.Crossbowman.name, Attributs.maxLifepoints, 30f);
		this.gameteam.data.addAttribut(UnitsList.Knight.name, Attributs.maxLifepoints, 30f);
		this.gameteam.data.addAttribut(UnitsList.Priest.name, Attributs.maxLifepoints, 30f);
		this.gameteam.data.addAttribut(UnitsList.Inquisitor.name, Attributs.maxLifepoints, 30f);
		this.gameteam.data.addAttribut(UnitsList.Archange.name, Attributs.maxLifepoints, 30f);
	}

	
}
