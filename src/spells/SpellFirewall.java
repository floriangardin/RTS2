package spells;

import model.Checkpoint;
import model.GameTeam;
import model.Objet;
import model.Plateau;
import model.Utils;
import units.Character;

public class SpellFirewall extends Spell{

	public float remainingTime;
	
	public SpellFirewall(Plateau p, GameTeam gameteam){
		this.chargeTime = 450f;
		this.name = "Firewall";
		this.icon = p.g.images.spellFirewall;
		this.range = 200f;
		this.damage = 1f;
		this.remainingTime = 250f;
		this.gameteam = gameteam;
		this.needToClick=true;
	}

	public void launch(Objet target, Character launcher){
		Objet t = realTarget(target, launcher);
		Firewall f = new Firewall(launcher.p,launcher,t,-1);
		f.damage = this.damage;
		f.remainingTime = this.remainingTime;
	}
	
	public Objet realTarget(Objet target, Character launcher){
		if(Utils.distance(target,launcher)>range){
			float ux = target.getX() - launcher.getX();
			float uy = target.getY() - launcher.getY();
			float norm = (float) Math.sqrt(ux*ux+uy*uy);
			ux = ux*this.range/norm;
			uy = uy*this.range/norm;
			return new Checkpoint(p,launcher.getX()+ux,launcher.getY()+uy);
		} else {
			return target;
		}
	}

}
