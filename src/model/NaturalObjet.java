package model;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Shape;

public abstract class NaturalObjet extends Objet {

	public NaturalObjet(float x, float y, Shape collisionBox, Color color, Plateau p, float lifePoints) {
		super(x, y, collisionBox, color, p, lifePoints);
		// TODO Auto-generated constructor stub
	}

}
