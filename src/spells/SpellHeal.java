package spells;

import main.Main;
import model.Checkpoint;
import model.GameTeam;
import model.Objet;
import model.Plateau;
import model.Utils;
import units.Character;

//TODO : sort qui use heal
public class SpellHeal extends Spell{

	public float remainingTime;

	public SpellHeal(Plateau p, GameTeam gameteam){
		this.chargeTime = 15f;
		this.p = p;
		this.name = "heal";
		this.icon = p.g.images.get("spellHeal");
		this.range = 400f*Main.ratioSpace;
		this.damage = 1f;
		this.remainingTime = 5f;
		this.gameteam = gameteam;
		this.needToClick=false;
	}

	public void launch(Objet target, Character launcher){
		if(realTarget(target, launcher)){
			Heal f = new Heal(p,launcher,target,-1,gameteam);
			f.remainingTime = this.remainingTime;
		}

	}

	public boolean realTarget(Objet target, Character launcher){
		return Utils.distance(launcher, target)<range;
	}

}
