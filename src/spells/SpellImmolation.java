package spells;

import java.util.Vector;

import model.Objet;
import model.Plateau;
import model.Player;
import units.Character;

public class SpellImmolation extends Spell{

	public float remainingTime;
	
	public SpellImmolation(Plateau p, Player player){
		this.chargeTime = 0f;
		this.name = "Immolation";
		this.icon = p.g.images.spellImmolation;
		this.range = 0f;
		this.damage = 0f;
		this.remainingTime = 75f;
		this.player = player;
		this.needToClick=false;
	}

	public void launch(Objet target, Character launcher){

		launcher.isImmolating = true;
		launcher.remainingTime = this.remainingTime;
		launcher.spells = new Vector<Spell>();
	}
}
