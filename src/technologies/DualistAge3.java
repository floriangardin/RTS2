package technologies;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import buildings.Building;
import buildings.BuildingTech;
import model.GameTeam;
import model.Plateau;
import model.Player;

public class DualistAge3 extends Technologie {

	public DualistAge3(Plateau p, GameTeam gameteam) {
		this.id = 1;
		this.tech = Technologies.DualistAge3;
		this.name = "Fanatism Age";
		this.p = p;
		this.gameteam = gameteam;
		this.data = this.gameteam.data;
		try {
			this.icon = new Image("pics/tech/age3.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void applyEffect(){
		// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
		// Age passing does nothing
		// Then update
		this.gameteam.hq.age = 3;
		this.gameteam.maxPop= 24;
		for(Building b : this.p.buildings){
			if(b instanceof BuildingTech && b.getTeam()==gameteam.id){
				((BuildingTech) b).updateProductionList();
			}
		}
	}

	
}
