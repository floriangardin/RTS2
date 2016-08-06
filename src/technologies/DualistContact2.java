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

	public DualistContact2(GameTeam gameteam) {
		this.id = 8;
		this.tech = Technologies.DualistContactAttack2;
		this.initialize( gameteam,tech);
	}
	
	public void applyEffect(){
		this.gameteam.data.addAttribut(UnitsList.Knight.name, Attributs.damage, 1);
		this.gameteam.data.addAttribut(UnitsList.Spearman.name, Attributs.damage, 1);
	}
}