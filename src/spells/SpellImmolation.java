package spells;

import java.util.Vector;

import display.DisplayRessources;
import model.Game;
import model.GameTeam;
import model.Objet;
import model.Plateau;
import model.Player;
import units.Character;

public class SpellImmolation extends Spell{

	public float remainingTime;
	
	public SpellImmolation( GameTeam gameteam){
		this.chargeTime = 0f;
		this.name = "Immolation";
		this.icon = Game.g.images.get("spellImmolation");
		this.range = 0f;
		this.damage = 0f;
		this.remainingTime = 75f;
		this.gameteam = gameteam;
		this.needToClick=false;
	}

	public void launch(Objet target, Character launcher){

		launcher.isImmolating = true;
		launcher.remainingTime = this.remainingTime;
		Game.g.sounds.get("fire").play(1f,Game.g.options.soundVolume);
		launcher.spells = new Vector<Spell>();
	}
}
