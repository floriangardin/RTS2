package technologies;


import data.Attributs;
import utils.ObjetsList;

public class DualistRangeAttack3 extends Technologie {

	public DualistRangeAttack3(int team) {
		this.id = 11;
		this.tech = Technologies.DualistRangeAttack3;
		this.initialize( team,tech);
	}
	
	public void applyEffect(){
		this.getGameTeam().data.addAttribut(ObjetsList.Inquisitor.name, Attributs.damage, 1);
		this.getGameTeam().data.addAttribut(ObjetsList.Crossbowman.name, Attributs.damage, 1);
	}
}