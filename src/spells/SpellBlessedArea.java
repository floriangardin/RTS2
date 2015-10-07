package spells;

import model.Checkpoint;
import model.Objet;
import model.Plateau;
import model.Player;
import model.Utils;
import units.Character;

public class SpellBlessedArea extends Spell{
	public float remainingTime;
	public float size;
	public float effect;
	
	public SpellBlessedArea(Plateau p, Player player){
		this.chargeTime = 450f;
		this.name = "Blessed Area";
		this.icon = p.g.images.spellBlessedArea;
		this.range = 200f;
		this.remainingTime = 250f;
		this.effect= 0.75f;
		this.player = player;
		this.needToClick=true;
	}

	public void launch(Objet target, Character launcher){
		Objet t = realTarget(target, launcher);
		BlessedArea ba = new BlessedArea(launcher.p,launcher,(Checkpoint)t);
		ba.remainingTime = this.remainingTime;
		ba.effect = this.effect;
		ba.size = this.size;
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
