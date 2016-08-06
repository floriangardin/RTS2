package technologies;


import data.Attributs;
import model.Game;
import model.GameTeam;
import model.Plateau;
import units.Character;
import units.UnitKnight;
import units.UnitSpearman;
import utils.UnitsList;

public class DualistContact2 extends Technologie {

	public DualistContact2(int team) {
		this.id = 8;
		this.tech = Technologies.DualistContactAttack2;
		this.initialize(team,tech);
	}
	
	public void applyEffect(){
		this.getGameTeam().data.addAttribut(UnitsList.Knight.name, Attributs.damage, 1);
		this.getGameTeam().data.addAttribut(UnitsList.Spearman.name, Attributs.damage, 1);
	}
}