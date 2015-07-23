import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public abstract class Objet {
	
	protected Vector<Objet> target = new Vector<Objet>();
	protected Plateau p;
	protected Objet main_target; // 0: nature, 1: first player ....
	protected int camps ; 
	protected boolean alive;
	protected float radius;
	protected Shape box ;
	protected Color color;
	// position time t
	protected float x;
	protected float y;
	// attack speed, define the potential number of attack per second ;
	protected float attack_speed;
	protected float power;
	protected float lifepoints;
	// attack state ;
	protected float attack_state;
	// Speed time t
	protected float vx;
	protected float vy;
	protected Circle sight_range;
	protected float range;
	//norm of a_max ;
	protected float acc ;
	// create a physic model of movement
	public abstract Circle getSightRange();
	public abstract void removeLifePoints(float to_remove);
	public abstract void action(Vector<Objet> target);
	public abstract void draw(Graphics g);
	public abstract void move(float dx, float dy);
	public abstract float getX();
	public abstract float getY();
	public int getCamps() {
		return camps;
	}
	public void setCamps(int camps) {
		this.camps = camps;
	}
	public abstract boolean isAlive();
	public abstract void collision(Objet o);
	public abstract float getVx();
	public abstract float getVy();
	public abstract Shape getBox();
}
