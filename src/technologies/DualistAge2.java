package technologies;

import model.Game;
import utils.ObjetsList;
import buildings.Building;


public class DualistAge2 extends Technologie {

	public DualistAge2(int team) {
		this.id = 0;
		this.tech = Technologies.DualistAge2;
		this.initialize(team,tech);
	}
	
	public DualistAge2(int team,ObjetsList o) {
		this.objet = o;
		this.id = 0;
		this.tech = Technologies.DualistAge2;
		this.initialize(team,tech);
	}
	
	public void applyEffect(){
		// Va chercher le player.data correspondant et ajoute le bonus ou ajoute tech concerné
		// Age passing does nothing
		// Then update
		this.getGameTeam().hq.age = 2;
		this.getGameTeam().maxPop= 24;
		for(Building b : Game.g.plateau.buildings){
			if(b instanceof Building && b.getTeam()==team){
				((Building) b).updateProductionList();
				
			}
		}
	}

	
}
