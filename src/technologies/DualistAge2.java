package technologies;

import model.Game;
import model.GameTeam;
import model.Plateau;


import buildings.Building;
import buildings.BuildingTech;

public class DualistAge2 extends Technologie {

	public DualistAge2(GameTeam gameteam) {
		this.id = 0;
		this.tech = Technologies.DualistAge2;
		this.initialize(gameteam,tech);
	}
	
	public void applyEffect(){
		// Va chercher le player.data correspondant et ajoute le bonus ou ajoute tech concerné
		// Age passing does nothing
		// Then update
		this.gameteam.hq.age = 2;
		this.gameteam.maxPop= 24;
		for(Building b : Game.g.plateau.buildings){
			if(b instanceof BuildingTech && b.getTeam()==gameteam.id){
				((BuildingTech) b).updateProductionList();
				
			}
		}
	}

	
}
