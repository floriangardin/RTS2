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

	public DualistRangeAttack3(int team) {
		this.id = 11;
		this.tech = Technologies.DualistRangeAttack3;
		this.initialize( team,tech);
	}
	
	public void applyEffect(){
		this.getGameTeam().data.addAttribut(UnitsList.Inquisitor.name, Attributs.damage, 1);
		this.getGameTeam().data.addAttribut(UnitsList.Crossbowman.name, Attributs.damage, 1);
	}
}