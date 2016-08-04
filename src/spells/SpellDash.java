package spells;

import main.Main;
import model.Checkpoint;
import model.Game;
import model.GameTeam;
import model.Objet;
import model.Plateau;
import model.Utils;
import units.Character;
import units.UnitCrossbowman;
import units.UnitSpearman;

public class SpellDash extends Spell{

	public float remainingTime;
	
	public SpellDash(Plateau p, GameTeam gameteam){
		this.chargeTime = 500f;
		this.p = p;
		this.name = "Dash";
		this.icon = Game.g.images.get("iconSpearman");
		this.range = 200f*Main.ratioSpace;
		this.damage = 1f;
		this.remainingTime = 1f;
		this.gameteam = gameteam;
		this.needToClick=false;
	}

	public void launch(Objet target, Character launcher){
		if(launcher instanceof UnitSpearman){
			UnitSpearman unit = (UnitSpearman) launcher;
			unit.setTarget(target);
			unit.inDash = this.remainingTime;
			unit.bonusAttack = true;
		}
	}
	
	
}
