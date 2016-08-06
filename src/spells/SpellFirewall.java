package spells;

import data.Attributs;
import model.Checkpoint;
import model.Objet;
import units.Character;
import utils.SpellsList;
import utils.Utils;

public class SpellFirewall extends Spell{

	
	public SpellFirewall(){
		this.name = SpellsList.Firewall;
	}

	public void launch(Objet target, Character launcher){
		Objet t = Spell.realTarget(target, launcher, this.getAttribut(Attributs.range));
		Firewall f = new Firewall(launcher,t,-1);
		f.damage = this.getAttribut(Attributs.damage);
		f.remainingTime = this.getAttribut(Attributs.totalTime);
		launcher.stop();
	}
	
	

}
