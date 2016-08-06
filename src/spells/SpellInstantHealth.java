package spells;

import data.Attributs;
import model.Game;
import model.Objet;
import units.Character;
import utils.SpellsList;
import utils.Utils;

public class SpellInstantHealth extends Spell{


	public SpellInstantHealth(){
		this.name = SpellsList.InstantHealth;
	}

	public void launch(Objet target, Character launcher){
		// Check if target intersect an ennemy
		Objet h = target;
		
		for(Character c : Game.g.plateau.characters){
			if(c.collisionBox.contains(target.collisionBox)){
				h =c;
			}
		}

		if(h instanceof Character && h.getTeam()==launcher.getTeam() && launcher!=h && this.getAttribut(Attributs.range)>=Utils.distance(h, launcher)){
			h.lifePoints = ((Character) h ).getAttribut(Attributs.maxLifepoints);
			
		}
	}

}


