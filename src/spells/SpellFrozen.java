package spells;

import main.Main;
import model.Game;
import model.GameTeam;
import model.Objet;
import units.Character;
import utils.Utils;

public class SpellFrozen extends Spell{

	public float remainingTime;

	public SpellFrozen(GameTeam gameteam){
		this.chargeTime = 450f;
		this.name = "Fence";
		this.icon = Game.g.images.get("spellFirewall");
		this.range = 400f*Main.ratioSpace;
		this.damage = 1f;
		this.remainingTime = 5f;
		this.gameteam = gameteam;
		this.needToClick=false;
	}

	public void launch(Objet target, Character launcher){
		if(realTarget(target, launcher)){
			Frozen f = new Frozen(launcher,target,-1);
			f.remainingTime = this.remainingTime;
		}

	}

	public boolean realTarget(Objet target, Character launcher){
		return Utils.distance(launcher, target)<range;
	}

}
