package model;

import units.Character;

public abstract class RidableObjet extends ActionObjet {
	// A ridable objet doesn't do any action ....
	protected float velocity;
	private Character owner;
	
	public void action(){
		if(this.owner!=null)
			this.setXY(this.owner.getX(), this.owner.getY());
	}
	public Character getOwner(){
		return owner;
	}
	public void setOwner(Character owner){
		this.owner = owner;
	}
		
}
