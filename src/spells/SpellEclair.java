package spells;

import data.Attributs;
import model.Game;
import model.Objet;
import units.Character;
import utils.SpellsList;

// TODO : Sort
public class SpellEclair extends Spell{

	

	public SpellEclair(){
		this.name = SpellsList.Eclair;
	}

	public void launch(Objet target, Character launcher){
		// Check if target intersect an ennemy
		Objet h = target;
		for(Character c : Game.g.plateau.characters){
			if(c.collisionBox.contains(target.collisionBox)){
				h =c;
			}
		}

		if(h instanceof Character && h.getTeam()!=team){
			((Character)h).isBolted = true;
			//TODO add a sound
		}else{
			this.getGameTeam().special+=this.getAttribut(Attributs.faithCost);
		}
	}

}


