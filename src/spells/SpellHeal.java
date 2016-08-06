package spells;

import main.Main;
import model.Checkpoint;
import model.Game;
import model.GameTeam;
import model.Objet;
import model.Plateau;
import model.Utils;
import units.Character;

//TODO : sort qui use heal
public class SpellHeal extends Spell{

	public float remainingTime;
	
	public SpellHeal(GameTeam gameteam){
		this.chargeTime = 15f;
		this.name = "heal";
		this.icon = Game.g.images.get("spellHeal");
		this.range = 400f*Main.ratioSpace;
		this.damage = 1f;
		this.faithCost = 2;
		this.remainingTime = 8f;
		this.gameteam = gameteam;
		this.needToClick=false;
	}

	public void launch(Objet target, Character launcher){
		if(realTarget(target, launcher)){
			Heal f = new Heal(launcher,target,-1,gameteam);
			f.remainingTime = this.remainingTime;
		}

	}

	public boolean realTarget(Objet target, Character launcher){
		return Utils.distance(launcher, target)<range;
	}

}
