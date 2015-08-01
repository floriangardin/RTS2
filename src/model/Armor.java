package model;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Shape;

public class Armor extends Objet{

	protected float weight;
	protected float damageReductor;
	private Character owner;
	
	public Character getOwner(){
		return owner;
	}
	public void setOwner(Character owner){
		this.owner = owner;
	}
	
// On s'en tamponne que l'armure aie un x ou un y en fait (hérite de objet)
	
}
