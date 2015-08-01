package model;

import java.util.Vector;

import org.newdawn.slick.geom.Circle;

public abstract class Bullet extends ActionObjet {
	protected float damage;
	Circle areaEffect;
	
	private Vector<Character> getCharactersInAreaEffect(){
		return null;
	}
}
