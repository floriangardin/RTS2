package model;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Shape;

public abstract class RidableObjet extends Objet {
	// A ridable objet doesn't do any action ....
	protected float velocity;
	protected Objet owner;
		
}
