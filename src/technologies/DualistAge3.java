package technologies;

import java.util.Vector;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import buildings.Building;
import buildings.BuildingTech;
import model.Plateau;
import model.Player;

public class DualistAge3 extends Technologie {

	public DualistAge3(Plateau p, Player player) {
		this.tech = Technologies.DualistAge3;
		this.name = "Fanatism Age";
		this.p = p;
		this.player = player;
		this.data = this.player.data;
		try {
			this.icon = new Image("pics/tech/age3.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void applyEffect(){
		// Va chercher le player.data correspondant et ajoute le bonus ou ajoute tech concerné
		// Age passing does nothing
		// Then update
		this.player.hq.age = 3;
		for(Building b : this.p.buildings){
			if(b instanceof BuildingTech && b.team==player.team){
				((BuildingTech) b).updateProductionList();
			}
		}
	}

	
}
