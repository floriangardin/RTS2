package spells;

import main.Main;
import model.Checkpoint;
import model.Game;
import model.GameTeam;
import model.Objet;
import model.Plateau;
import model.Player;
import model.Utils;
import units.Character;

public class SpellBlessedArea extends Spell{
	public float remainingTime;
	public float size;
	public float effect;
	
	public SpellBlessedArea(GameTeam gameteam){
		this.chargeTime = 450f;
		this.name = "Blessed Area";
		this.icon = Game.g.images.get("spellBlessedArea");
		this.range = 200f*Main.ratioSpace;
		this.remainingTime = 250f;
		this.effect= 0.75f;
		this.gameteam = gameteam;
		this.needToClick=false;
	}

	public void launch(Objet target, Character launcher){
		Objet t = realTarget(target, launcher);
		BlessedArea ba = new BlessedArea(launcher,(Checkpoint)t,-1);
		ba.remainingTime = this.remainingTime;
		ba.effect = this.effect;
		ba.size = this.size;
		launcher.stop();
	}
	
	public Objet realTarget(Objet target, Character launcher){
		if(Utils.distance(target,launcher)>range){
			float ux = target.getX() - launcher.getX();
			float uy = target.getY() - launcher.getY();
			float norm = (float) Math.sqrt(ux*ux+uy*uy);
			ux = ux*this.range/norm;
			uy = uy*this.range/norm;
			return new Checkpoint(launcher.getX()+ux,launcher.getY()+uy);
		} else {
			return target;
		}
	}
}
