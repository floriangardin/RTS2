package technologies;

import java.util.Vector;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import buildings.Building;
import buildings.BuildingTech;
import model.Plateau;
import model.Player;

public class DualistAge2 extends Technologie {

	public DualistAge2(Plateau p, Player player) {
		this.tech = Technologies.DualistAge2;
		this.name = "Fervour Age";
		this.p = p;
		this.player = player;
		this.data = this.player.data;
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
		this.player.hq.age = 2;
		for(Building b : this.p.buildings){
			if(b instanceof BuildingTech && b.team==player.team){
				((BuildingTech) b).updateProductionList();
			}
		}
	}

	
}
