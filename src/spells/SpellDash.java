package spells;

import data.Attributs;
import data.AttributsChange;
import data.AttributsChange.Change;
import model.Objet;
import units.Character;
import utils.SpellsList;

public class SpellDash extends Spell{

	
	public SpellDash(){
		this.name = SpellsList.Dash;
	}

	public void launch(Objet target, Character launcher){
		launcher.attributsChanges.add(new AttributsChange(Attributs.damage,Change.SET,this.getAttribut(Attributs.bonusDamage),true));
		launcher.attributsChanges.add(new AttributsChange(Attributs.maxVelocity,Change.SET,this.getAttribut(Attributs.bonusSpeed),this.getAttribut(Attributs.totalTime)));
		launcher.setTarget(target);
		
	}

	
	
}
