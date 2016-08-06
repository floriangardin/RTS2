package spells;

import java.util.Vector;

import data.Attributs;
import events.Events;
import model.Game;
import model.Objet;
import units.Character;
import utils.SpellsList;

public class SpellImmolation extends Spell{

	
	public SpellImmolation(){
		this.name = SpellsList.Immolation;
	}

	public void launch(Objet target, Character launcher){

		launcher.isImmolating = true;
		launcher.remainingTime = this.getAttribut(Attributs.totalTime);
		launcher.spells = new Vector<SpellsList>();
		Game.g.events.addEvent(Events.Immolation, target);
	}
}
