package spells;

import java.util.Vector;

import model.GameTeam;
import model.Objet;
import model.Plateau;
import model.Player;
import units.Character;

public class SpellImmolation extends Spell{

	public float remainingTime;
	
	public SpellImmolation(Plateau p, GameTeam gameteam){
		this.chargeTime = 0f;
		this.name = "Immolation";
		this.icon = p.g.images.get("spellImmolation");
		this.range = 0f;
		this.damage = 0f;
		this.remainingTime = 75f;
		this.gameteam = gameteam;
		this.needToClick=false;
	}

	public void launch(Objet target, Character launcher){

		launcher.isImmolating = true;
		launcher.remainingTime = this.remainingTime;
		launcher.p.g.sounds.fire.play(1f,launcher.p.g.options.soundVolume);
		launcher.spells = new Vector<Spell>();
	}
}
