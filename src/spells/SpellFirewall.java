package spells;

import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

import model.ActionObjet;
import model.Checkpoint;
import model.Objet;
import model.Utils;

public class SpellFirewall extends Spell{

	public float remainingTime;
	public float width;
	public Shape collisionBox;
	
	public SpellFirewall(ActionObjet owner){
		this.chargeTime = 50f;
		this.width = 10f;
		this.owner = owner;
		this.damage = 3f;
		this.range = 100f;
		this.remainingTime = 150f;
		this.state = 0f;
	}

	public void launch(Objet target){
		
		Firewall f = new Firewall(this.owner.p);
		Objet t = realTarget(target.getX(),target.getY());
		f.damage = this.damage;
		f.remainingTime = this.remainingTime;
		float vx = t.getY()-this.owner.getY();
		float vy = this.owner.getX()-t.getX();
		float norm = (float)Math.sqrt(vx*vx+vy*vy);
		vx = vx/norm;
		vy = vy/norm;
		float ax,ay,bx,by,cx,cy,dx,dy;
		ax = this.owner.getX()+vx*width/2f;
		ay = this.owner.getY()+vy*width/2f;
		bx = this.owner.getX()-vx*width/2f;
		by = this.owner.getY()-vy*width/2f;
		dx = t.getX()+vx*width/2f;
		dy = t.getY()+vy*width/2f;
		cx = t.getX()-vx*width/2f;
		cy = t.getY()-vy*width/2f;
		float[] arg = {ax,ay,bx,by,cx,cy,dx,dy};
		f.collisionBox = new Polygon(arg);
	}
	
	public Objet realTarget(float x, float y){
		if(Utils.distance(new Checkpoint(x, y),this.owner)>range){
			float ux = x - this.owner.getX();
			float uy = y - this.owner.getY();
			float norm = (float) Math.sqrt(ux*ux+uy*uy);
			ux = ux*this.range/norm;
			uy = uy*this.range/norm;
			return new Checkpoint(this.owner.getX()+ux,this.owner.getY()+uy);
		} else {
			return new Checkpoint(x,y);
		}
	}
}
