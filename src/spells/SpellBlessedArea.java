package spells;

import data.Attributs;
import model.Checkpoint;
import model.Objet;
import units.Character;
import utils.SpellsList;

public class SpellBlessedArea extends Spell{
	
	protected SpellBlessedArea(){
		this.name = SpellsList.BlessedArea;
	}

	public void launch(Objet target, Character launcher){
		Objet t = Spell.realTarget(target, launcher, this.getAttribut(Attributs.range));
		BlessedArea ba = new BlessedArea(launcher,(Checkpoint)t,-1);
		ba.remainingTime = this.getAttribut(Attributs.totalTime);
		ba.size = this.getAttribut(Attributs.size);
		launcher.stop();
	}


	
	
}
