package model;


import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

public abstract class Bullet extends ActionObjet {
	protected float damage;
	float areaEffect;
	Character owner;
	private Vector<Character> getCharactersInAreaEffect(){
		return null;
	}
		


}
