package spells;

import java.util.Vector;

import data.Attributs;
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
		Game.g.sounds.get("fire").play(1f,Game.g.options.soundVolume);
		launcher.spells = new Vector<SpellsList>();
	}
}
