package technologies;


import data.Attributs;
import model.Game;
import model.GameTeam;
import model.Plateau;
import units.Character;
import units.UnitCrossbowman;
import units.UnitInquisitor;
import utils.UnitsList;

public class DualistRangeAttack3 extends Technologie {

	public DualistRangeAttack3(GameTeam gameteam) {
		this.id = 11;
		this.tech = Technologies.DualistRangeAttack3;
		this.initialize( gameteam,tech);
	}
	
	public void applyEffect(){
		this.gameteam.data.addAttribut(UnitsList.Inquisitor.name, Attributs.damage, 1);
		this.gameteam.data.addAttribut(UnitsList.Crossbowman.name, Attributs.damage, 1);
	}
}