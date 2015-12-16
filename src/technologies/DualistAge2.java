package technologies;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import buildings.Building;
import buildings.BuildingTech;
import model.GameTeam;
import model.Plateau;
import model.Player;

public class DualistAge2 extends Technologie {

	public DualistAge2(Plateau p, GameTeam gameteam) {
		this.id = 0;
		this.tech = Technologies.DualistAge2;
		this.name = "Fervour Age";
		this.p = p;
		this.gameteam = gameteam;
		this.data = this.gameteam.data;
		try {
			this.icon = new Image("pics/tech/age2.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void applyEffect(){
		// Va chercher le player.data correspondant et ajoute le bonus ou ajoute tech concerné
		// Age passing does nothing
		// Then update
		this.gameteam.hq.age = 2;
		this.gameteam.maxPop= 24;
		for(Building b : this.p.buildings){
			if(b instanceof BuildingTech && b.getTeam()==gameteam.id){
				((BuildingTech) b).updateProductionList();
				
			}
		}
	}

	
}
