package spells;

import buildings.BuildingProduction;
import buildings.BuildingTech;
import main.Main;
import model.Game;
import model.GameTeam;
import model.Objet;
import model.Plateau;
import units.Character;

//TODO : sort
public class SpellProduct extends Spell{

	public float remainingTime;
	public float width;

	public SpellProduct(GameTeam gameteam){
		this.chargeTime = 15f;
		this.width = 15f*Main.ratioSpace;
		this.name = "Spell Product";
		this.icon = Game.g.images.get("spellProduct");
		this.range = 200f*Main.ratioSpace;
		this.damage = 1f;
		this.remainingTime = 250f;
		this.gameteam = gameteam;
		this.needToClick=true;
		this.faithCost = 2;
	}

	public void launch(Objet target, Character launcher){
		// Check if target intersect an ennemy
		if(target instanceof BuildingProduction && ((BuildingProduction) target).queue.size()>0){
			BuildingProduction p = (BuildingProduction) target;
			if(p.queue.size()>0){
				p.charge=p.productionList.get(p.queue.get(0)).time;
			}
		}else if(target instanceof BuildingTech && ((BuildingTech) target).queue!=null){
			BuildingTech p = (BuildingTech) target;
			p.techTerminate(p.queue);
		}else{
			this.gameteam.special+=this.faithCost;
		}
		
		
	}

}


