package spells;

import java.util.Vector;

import model.Objet;
import model.Plateau;
import model.Player;
import units.Character;

public class SpellConversion extends Spell{
	
	public float faithCost;
	
	public SpellConversion(Plateau p, Player player){
		this.chargeTime = 200f;
		this.faithCost = 1f;
		this.name = "Conversion";
		this.icon = p.images.spellConversion;
		this.range = 50f;
		this.damage = 0f;
		this.player = player;
		this.needToClick=true;
		this.p = p;
	}

	public void launch(Objet target, Character launcher){
		Objet t = p.findTarget(target.x, target.y);
		if(t instanceof Character && t.team!=launcher.team && launcher.player.special>=this.faithCost){
			((Character)t).changeTeam(launcher.team);
		}
	}
}
