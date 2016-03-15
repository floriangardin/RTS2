package spells;

import model.Checkpoint;
import model.Game;
import model.GameTeam;
import model.Objet;
import model.Plateau;
import model.Utils;
import units.Character;

public class SpellFence extends Spell{

	public float remainingTime;
	
	public SpellFence(Plateau p, GameTeam gameteam){
		this.chargeTime = 450f;
		this.p = p;
		this.name = "Fence";
		this.icon = p.g.images.spellFirewall;
		this.range = 200f*Game.ratioSpace;
		this.damage = 1f;
		this.remainingTime = 30f;
		this.gameteam = gameteam;
		this.needToClick=false;
	}

	public void launch(Objet target, Character launcher){
		Objet t = realTarget(target, launcher);
		Fence f = new Fence(launcher.p,launcher,t,-1);
		f.remainingTime = this.remainingTime;
		launcher.stop();
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
