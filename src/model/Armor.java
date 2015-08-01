package model;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Shape;

public class Armor extends Objet{

	protected float weight;
	protected float damageReductor;
	public Armor(float x, float y, Shape collisionBox, Color color, Plateau p, float lifePoints, float weight, float damageReductor) {
		super(x, y, collisionBox, color, p, lifePoints);
		this.weight = weight;
		this.damageReductor = damageReductor;
		// TODO Auto-generated constructor stub
	}
// On s'en tamponne que l'armure aie un x ou un y en fait (hérite de objet)
	
}
