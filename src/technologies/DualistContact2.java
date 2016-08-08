package technologies;


import data.Attributs;
import utils.ObjetsList;

public class DualistContact2 extends Technologie {

	public DualistContact2(int team) {
		this.id = 8;
		this.tech = Technologies.DualistContactAttack2;
		this.initialize(team,tech);
	}
	public DualistContact2(int team,ObjetsList o) {
		this.objet = o;
		this.id = 8;
		this.tech = Technologies.DualistContactAttack2;
		this.initialize(team,tech);
	}
	
	
	public void applyEffect(){
		this.getGameTeam().data.addAttribut(ObjetsList.Knight, Attributs.damage, 1);
		this.getGameTeam().data.addAttribut(ObjetsList.Spearman, Attributs.damage, 1);
	}
}