package spells;

import java.util.Vector;

import model.Game;
import model.GameTeam;
import model.Objet;
import units.Character;
import events.Events;

public class SpellImmolation extends Spell{

	public float remainingTime;
	
	public SpellImmolation( GameTeam gameteam){
		this.chargeTime = 0f;
		this.name = "Immolation";
		this.icon = Game.g.images.get("spellImmolation");
		this.range = 0f;
		this.damage = 0f;
		this.remainingTime = 75f;
		this.gameteam = gameteam;
		this.needToClick=false;
	}

	public void launch(Objet target, Character launcher){

		launcher.isImmolating = true;
		launcher.remainingTime = this.remainingTime;
		launcher.spells = new Vector<Spell>();
		Game.g.events.addEvent(Events.Immolation, target);
	}
}
