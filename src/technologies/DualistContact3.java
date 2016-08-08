package technologies;


import data.Attributs;
import utils.ObjetsList;

public class DualistContact3 extends Technologie {

	public DualistContact3(int team) {
		this.id = 11;
		this.tech = Technologies.DualistContactAttack3;
		this.initialize(team, tech);
	}
	
	public void applyEffect(){
		this.getGameTeam().data.addAttribut(ObjetsList.Knight.name, Attributs.damage, 1);
		this.getGameTeam().data.addAttribut(ObjetsList.Spearman.name, Attributs.damage, 1);
	}
}