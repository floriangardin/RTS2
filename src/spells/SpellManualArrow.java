package spells;

import main.Main;
import model.Checkpoint;
import model.GameTeam;
import model.Objet;
import model.Plateau;
import model.Utils;
import units.Character;
import units.UnitCrossbowman;

public class SpellManualArrow extends Spell{

	public float remainingTime;
	
	public SpellManualArrow(Plateau p, GameTeam gameteam){
		this.chargeTime = 100f;
		this.p = p;
		this.name = "Manual Arrow";
		this.icon = p.g.images.get("iconCrossbowman");
		this.range = 200f*Main.ratioSpace;
		this.damage = 1f;
		this.remainingTime = 60f;
		this.gameteam = gameteam;
		this.needToClick=false;
	}

	public void launch(Objet target, Character launcher){
		if(launcher instanceof UnitCrossbowman){
			UnitCrossbowman unit = (UnitCrossbowman) launcher;
			unit.useWeapon(target);
		}
		else {
			
		}
		launcher.stop();
	}
	
	
}
