package spells;

import main.Main;
import model.Game;
import model.GameTeam;
import model.Objet;
import model.Plateau;
import model.Player;
import model.Utils;
import units.Character;

// TODO : Sort
public class SpellEclair extends Spell{

	public float remainingTime;
	public float width;
	

	public SpellEclair(Plateau p, GameTeam gameteam){
		this.p = p;
		this.chargeTime = 15f;
		this.faithCost = 2;
		this.width = 15f*Main.ratioSpace;
		this.name = "Instant Death";
		this.icon = Game.g.images.get("spellEclair");
		this.range = 200f*Main.ratioSpace;
		this.damage = 1f;
		this.remainingTime = 250f;
		this.gameteam = gameteam;
		this.needToClick=true;
	}

	public void launch(Objet target, Character launcher){
		// Check if target intersect an ennemy
		Objet h = target;
		for(Character c : p.characters){
			if(c.collisionBox.contains(target.collisionBox)){
				h =c;
			}
		}

		if(h instanceof Character && h.getTeam()!=gameteam.id && this.range>=Utils.distance(h, launcher)){
			((Character)h).isBolted = true;
			//TODO add a sound
		}else{
			this.gameteam.special+=this.faithCost;
		}
	}

}


