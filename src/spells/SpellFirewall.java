package spells;

import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

import model.ActionObjet;
import model.Checkpoint;
import model.Data;
import units.Character;
import model.Objet;
import model.Plateau;
import model.Player;
import model.Utils;

public class SpellFirewall extends Spell{

	public float remainingTime;
	public float width;
	
	public SpellFirewall(Plateau p, Player player){
		this.chargeTime = 450f;
		this.width = 15f;
		this.name = "Firewall";
		this.icon = p.images.spellFirewall;
		this.range = 200f;
		this.damage = 1f;
		this.remainingTime = 250f;
		this.player = player;
		this.needToClick=true;
	}

	public void launch(Objet target, Character launcher){

		Firewall f = new Firewall(launcher.p,launcher);
		Objet t = realTarget(target, launcher);
		f.damage = this.damage;
		f.remainingTime = this.remainingTime;
		float vx = t.getY()-launcher.getY();
		float vy = launcher.getX()-t.getX();
		float norm = (float)Math.sqrt(vx*vx+vy*vy);
		vx = vx/norm;
		vy = vy/norm;
		float ax,ay,bx,by,cx,cy,dx,dy;
		ax = launcher.getX()+vx*width/2f;
		ay = launcher.getY()+vy*width/2f;
		bx = launcher.getX()-vx*width/2f;
		by = launcher.getY()-vy*width/2f;
		dx = t.getX()+vx*width/2f;
		dy = t.getY()+vy*width/2f;
		cx = t.getX()-vx*width/2f;
		cy = t.getY()-vy*width/2f;
		float[] arg = {ax,ay,bx,by,cx,cy,dx,dy};
		f.collisionBox = new Polygon(arg);
		f.createAnimation(t, launcher);
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
