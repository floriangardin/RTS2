package technologies;


import data.Attributs;
import model.Game;
import model.GameTeam;
import model.Plateau;
import units.Character;
import units.UnitCrossbowman;
import units.UnitInquisitor;
import utils.UnitsList;

public class DualistRangeAttack2 extends Technologie {

	public DualistRangeAttack2( int team) {
		this.id = 9;
		this.tech = Technologies.DualistRangeAttack2;
		this.initialize( team,tech);
	}
	
	public void applyEffect(){
		this.getGameTeam().data.addAttribut(UnitsList.Inquisitor.name, Attributs.damage, 1);
		this.getGameTeam().data.addAttribut(UnitsList.Crossbowman.name, Attributs.damage, 1);
	}
}