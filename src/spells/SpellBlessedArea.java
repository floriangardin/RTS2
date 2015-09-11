package spells;

import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;

import model.*;
import units.Character;

public class SpellBlessedArea extends Spell{
	public float remainingTime;
	public float size;
	public float effect;
	
	public SpellBlessedArea(Plateau p, Player player){
		this.chargeTime = 450f;
		this.name = "Blessed Area";
		this.icon = p.images.spellBlessedArea;
		this.range = 200f;
		this.remainingTime = 250f;
		this.effect= 0.75f;
		this.size = 200f;
		this.player = player;
		this.needToClick=true;
	}

	public void launch(Objet target, Character launcher){

		Objet t = realTarget(target, launcher);
		BlessedArea ba = new BlessedArea(launcher.p,launcher,(Checkpoint)t);
		ba.remainingTime = this.remainingTime;
		ba.size = this.size;
		ba.collisionBox = new Rectangle(t.getX()-size/2f,t.getY()-size/2f,size,size);
		ba.x = t.getX();
		ba.y = t.getY();
		ba.effect = this.effect;
		ba.createAnimation();
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
