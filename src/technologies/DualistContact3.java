package technologies;


import data.Attributs;
import model.Game;
import model.GameTeam;
import model.Plateau;
import units.Character;
import units.UnitKnight;
import units.UnitSpearman;
import utils.UnitsList;

public class DualistContact3 extends Technologie {

	public DualistContact3(GameTeam gameteam) {
		this.id = 11;
		this.tech = Technologies.DualistContactAttack3;
		this.initialize(gameteam, tech);
	}
	
	public void applyEffect(){
		this.gameteam.data.addAttribut(UnitsList.Knight.name, Attributs.damage, 1);
		this.gameteam.data.addAttribut(UnitsList.Spearman.name, Attributs.damage, 1);
	}
}