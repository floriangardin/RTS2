package technologies;


import utils.ObjetsList;
import buildings.Building;
import model.Game;
import model.GameTeam;
import model.Plateau;

public class DualistAge3 extends Technologie {

	public DualistAge3( int team) {
		this.id = 1;
		this.tech = Technologies.DualistAge3;
		this.initialize(team,tech);
	}
	
	public DualistAge3(int team,ObjetsList o) {
		this.objet = o;
		this.id = 0;
		this.tech = Technologies.DualistAge3;
		this.initialize(team,tech);
	}
	
	public void applyEffect(){
		// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
		// Age passing does nothing
		// Then update
		this.getGameTeam().hq.age = 3;
		this.getGameTeam().maxPop= 40;
		for(Building b : Game.g.plateau.buildings){
			if( b.getTeam()==team){
				b.updateProductionList();
			}
		}
	}

	
}
