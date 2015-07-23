import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;


public class Personnage extends Objet{
	int framerate;
	protected Circle box;

	public Personnage(Plateau p, float x_origin, float y_origin,float w,float vitesse_attaque,float acc,float power,float lifepoints,float range,float 
			sight_range,int camps,int framerate){
		this.camps = camps;
		this.framerate = framerate ; 
		this.alive = true;
		this.p = p;
		if(p!=null){
			p.add(this);
		}
		//Shape stats 
		this.x = x_origin;
		this.y = y_origin;
		x_pre = x_origin;
		y_pre = y_origin ;
		this.radius = w;
		
		// General stats
		this.power = power;
		this.lifepoints = lifepoints;
		this.range = range;
		this.sight_range = new Circle(x_origin,y_origin,sight_range);
		this.attack_speed = vitesse_attaque/framerate;
		this.acc = acc;
		

		
		this.attack_state = 0.99f;
		
		// The center of the circle is redefinited to match with x,y ...
		this.box = new Circle(this.x, this.y, this.radius);
		switch(camps){
		case 0:
			this.color=Color.gray;
			break;
		case 1:

			this.color = Color.magenta;
			break;
		case 2:

			this.color = Color.red;
			break;
		default: 
			this.color = Color.gray;
		}

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
		if(f!=0 || g!=0){
			f = f - x ;
			g = g -y ;
			// normalize 
			float norm = (float) Math.sqrt(f*f+g*g);
			if(norm<0.01f){
				f=0;
				g=0;
			}
			else{
				f=  f/norm;
				g =  g/norm;
			}

		}

		// Update acceleration
		ax =acc * f/framerate;
		ay = acc * g/framerate;
		// update speed

		vx = 0.99f*vx + ax;
		vy = 0.99f*vy + ay;


		// update position ;
		this.setXY(x+vx, y+vy);

		// If reach bound collision cancel speed
		if(this.getX()>p.getMaxX()){
			vx = 0;
			this.setXY(p.getMaxX()-2f, this.y);
		}
		else if(this.getX()<0){
			vx = 0;
			this.setXY(2f, this.y);;
		}
		else if(this.getY()> p.getMaxY()){
			vy = 0;
			this.setXY(this.x,p.getMaxY()-2f);
		}
		else if(this.getY()<0){
			vy = 0;
			this.setXY(this.x, 2f);

		}

		box.setLocation(this.x-this.radius, this.y-this.radius);
	}

	public void attack(float vxo, float vyo){
		// If not in range move toward ennemy
		if(Utils.distance_2(this,this.main_target)<this.range*this.range){
			if(this.attack_state>1f){
				// calculate angle : 
				vxo = vxo - x ;
				vyo = vyo -y ;
				// normalize 
				float norm = (float) Math.sqrt(vxo*vxo+vyo+vyo);
				vxo =  vxo/norm;
				vyo =  vyo/norm;
				p.add(new Arrow(this,x,y,vxo,vyo,this.power,framerate));
				this.attack_state = 0f;
			}
		}

		else{
			this.move(this.main_target.getX(),this.main_target.getY());
		}
		
		// We have killed our ennemy !
		if(!this.main_target.isAlive()){
			
			this.main_target = null;
			// Now require a new target if possible
			Vector<Objet> potential_targets = p.getEnnemiesInSight(this);
			if(potential_targets.size()>0){
				//Take the nearest target :
				this.main_target = Utils.nearestObject(potential_targets, this);
			}
		}
	}


	@Override
	public void draw(Graphics g) {
		g.setColor(this.color);
		g.fill(box);
		g.setColor(Color.black);
		g.draw(this.sight_range);

	}

	public float getX(){
		return x;
	}
	public float getY(){
		return y;
	}


	@Override
	public boolean isAlive() {

		return alive;
	}

	@Override
	public void collision(Objet o) {

		// If collision test who have the highest velocity
		// The highest velocity continues 
		// The lowest velocity move away ( he is pushed at the pace of the other ) 
		if(o instanceof Arrow){

		}
		else{
			if((this.vx*this.vx+this.vy*this.vy)<o.getVx()*o.getVx()+o.getVy()*o.getVy()){
				this.setXY( x+o.getVx(),y+o.getVy());
			}
			else{
				this.setXY(x-vx, y-vy);
			}

			//this.move(this.vx+this.x,this.vy+this.y );
			//}
		}



	}
	public void setXY(float x,float y ) {
		this.x = x;
		this.y = y;
		this.box.setLocation(this.x -this.radius,this.y-this.radius);
		//this.sight_range.setLocation(this.x -this.sight_, this.y-this.radius);
		this.sight_range.setCenterX(x);
		this.sight_range.setCenterY(y);
	}

	@Override
	public float getVx() {

		return this.vx;
	}


	@Override
	public float getVy() {
		// TODO Auto-generated method stub
		return this.vy;
	}


	@Override
	public Shape getBox() {
		// TODO Auto-generated method stub
		return box;
	}


	@Override
	public void action(Vector<Objet> target) {
		// Choose main target : 
		// TODO : choose a proper target strategy
		// If we action from user or IA change the target according to a strategy  :
		if(target.size()>0){
			this.main_target = target.get(0) ;
		}

		// if main target on our side or nature go toward them
		if(this.main_target!=null){

			if(this.main_target.getCamps()==0 || this.main_target.getCamps()==this.getCamps()){
				this.move(this.main_target.getX(),this.main_target.getY());
			}
			if(this.main_target.getCamps()!=0 && this.main_target.getCamps()!=this.getCamps()){
				this.attack(this.main_target.getX(),this.main_target.getY());
			}

		}
		// update the attack state;
		if(this.attack_state<1f){
			this.attack_state+=this.attack_speed;
		}

	}
	public void removeLifePoints(float to_remove){
		this.lifepoints -= to_remove;
		if(this.lifepoints<0f){
			this.alive = false;
		}
	}
	public void stop(){
		this.vx = 0;
		this.vy = 0;
	}


	@Override
	public Circle getSightRange() {
		
		return this.sight_range;
		
	}
}
