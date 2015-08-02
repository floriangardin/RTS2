package model;

public abstract class Weapon extends ActionObjet {
	protected float frequency;
	protected float weight;
	private Character owner;
	
	public void setOwner(Character owner){
		this.owner = owner;
		this.setXY(owner.getX(),owner.getY());
	}
}
