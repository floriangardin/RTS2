import java.util.Vector;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Shape;

// This class is a marker for deplacement .. 
public class Marqueur extends Objet {

	
	protected Point box;
	protected int framerate;
	public Marqueur(Plateau p, float x_origin, float y_origin,float w,float vitesse,float acc,int camps,int framerate){
		this.p = p;
		this.x = x_origin;
		this.y = y_origin;
		this.camps = 0 ;
		this.framerate = framerate ;
		
	}
	@Override
	public void action(Vector<Objet> target) {
		
		
	}

	@Override
	public void draw(Graphics g) {
		
	}

	@Override
	public void move(float dx, float dy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getX() {
		
		return this.x;
	}

	@Override
	public float getY() {
		// TODO Auto-generated method stub
		return this.y;
	}

	@Override
	public boolean isAlive() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void collision(Objet o) {
		
	}

	@Override
	public float getVx() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getVy() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Shape getBox() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void removeLifePoints(float to_remove) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Circle getSightRange() {
		return this.sight_range;
		
	}

}
