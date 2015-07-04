import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;


public class Balle extends Objet{
	boolean has_moved;
	Personnage owner;
	int framerate ;
	float vitesse ;
	public Balle(Personnage owner,float x_origin, float y_origin,float vxo, float vyo,int framerate){
		this.framerate = framerate ;
		this.owner=owner;
		this.alive = true;
		this.x = x_origin;
		this.y = y_origin;
		x_pre = x_origin;
		y_pre = y_origin ;
		this.w = 3f;
		this.vitesse = 0.2f;
		vx =vitesse * vxo;
		vy =vitesse * vyo ;
		vx_pre = vxo;
		vy_pre = vyo;

		this.box = new Circle(this.x, this.y,this.w);
		this.color = Color.black;
	}



	public void move(float f, float g){
		// dx, dy represent the force applied on P
		// implement the physic
		// ma = sum(f)
		// m*ax = alpha*dx
		// m*ay = alpha*dy replaced by :
		// a_x = dx
		// a_y = dy
		// a_x = v_x(t) - v_x(t-1)
		// v_x = x(t) - x(t-1)
		// temporary variables

		// Update position
		x = x + vx;
		y = y + vy;

		box.setLocation(x-this.w, y-this.w);
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(this.color);
		g.fill(box);

	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}



	@Override
	public boolean isAlive() {

		return alive;
	}



	@Override
	public void collision(Objet o) {
		if(o instanceof Personnage && o.getCamps()!=this.owner.getCamps()){
			this.alive= false;
		}
		
	}



	@Override
	public float getVx() {
		return vx;
	}



	@Override
	public float getVy() {

		return vy;
	}



	@Override
	public Shape getBox() {

		return box;
	}



	@Override
	public void action(Vector<Objet> target) {
		this.move(0,0);
		
	}
}
