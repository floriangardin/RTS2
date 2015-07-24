import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;


public class Arrow extends Objet{
	boolean has_moved;
	Personnage owner;
	int framerate ;
	float vitesse ;
	float power ;
	public Arrow(Personnage owner,float x_origin, float y_origin,float vxo, float vyo,float power,int framerate){
		this.framerate = framerate ;
		this.power = power;
		this.owner=owner;
		this.alive = true;
		this.x = x_origin;
		this.y = y_origin;
		
		
		this.radius = 3f;
		this.vitesse = 0.5f;
		vx =vitesse * vxo;
		vy =vitesse * vyo ;


		this.box = new Circle(this.x, this.y,this.radius);
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

		box.setLocation(x-this.radius, y-this.radius);
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
			o.removeLifePoints(this.power);
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



	@Override
	public void removeLifePoints(float to_remove) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public Circle getSightRange() {
		return this.sight_range;
		
	}



	@Override
	public void action(Vector<Objet> target, Objet leader) {
		// TODO Auto-generated method stub
		
	}
}
