package model;

import java.util.Vector;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public abstract class Objet {
	
	protected float x;
	protected float y;
	protected Shape collisionBox;
	protected Color color;
	protected Plateau p;
	protected float lifePoints;
	
	protected void destroy(){
		this.lifePoints = -10;
	}
	protected void draw(){}
	protected void collision(Objet o){}
	
}
