package technologies;


import buildings.Building;
import buildings.BuildingTech;
import model.GameTeam;
import model.Plateau;

public class DualistAge3 extends Technologie {

	public DualistAge3(Plateau p, GameTeam gameteam) {
		this.id = 1;
		this.tech = Technologies.DualistAge3;
		this.initialize(p, gameteam,tech);
	}
	
	public void applyEffect(){
		// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
		// Age passing does nothing
		// Then update
		this.gameteam.hq.age = 3;
		this.gameteam.maxPop= 40;
		for(Building b : this.p.buildings){
			if(b instanceof BuildingTech && b.getTeam()==gameteam.id){
				((BuildingTech) b).updateProductionList();
			}
		}
	}

	
}
