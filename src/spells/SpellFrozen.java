package spells;

import data.Attributs;
import model.Objet;
import units.Character;
import utils.SpellsList;
import utils.Utils;

public class SpellFrozen extends Spell{


	public SpellFrozen(){
		this.name = SpellsList.Frozen;
	}

	public void launch(Objet target, Character launcher){
		if(realTarget(target, launcher)){
			Frozen f = new Frozen(launcher,target,-1);
			f.remainingTime = this.getAttribut(Attributs.totalTime);
		}

	}

	public boolean realTarget(Objet target, Character launcher){
		return Utils.distance(launcher, target)<getAttribut(Attributs.range);
	}

}
