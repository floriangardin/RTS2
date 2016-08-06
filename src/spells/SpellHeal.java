package spells;

import data.Attributs;
import model.Objet;
import units.Character;
import utils.SpellsList;
import utils.Utils;

//TODO : sort qui use heal
public class SpellHeal extends Spell{

	
	public SpellHeal(){
		this.name = SpellsList.Heal;
	}

	public void launch(Objet target, Character launcher){
		if(realTarget(target, launcher)){
			Heal f = new Heal(launcher,target,-1);
			f.remainingTime = this.getAttribut(Attributs.totalTime);
		}

	}

	public boolean realTarget(Objet target, Character launcher){
		return Utils.distance(launcher, target)<getAttribut(Attributs.range);
	}

}
