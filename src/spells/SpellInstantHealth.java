package spells;

import model.Objet;
import model.Plateau;
import model.Player;
import model.Utils;
import units.Character;

public class SpellInstantHealth extends Spell{

	public float remainingTime;
	public float width;

	public SpellInstantHealth(Plateau p, Player player){
		this.p = p;
		this.chargeTime = 450f;
		this.width = 15f;
		this.name = "Instant Health";
		this.icon = p.g.images.spellInstantHealth;
		this.range = 200f;
		this.damage = 1f;
		this.remainingTime = 250f;
		this.player = player;
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

		if(h instanceof Character && h.team==launcher.team && launcher!=h && this.range>=Utils.distance(h, launcher)){
			h.lifePoints = ((Character) h ).maxLifePoints;
			
		}
	}

}


