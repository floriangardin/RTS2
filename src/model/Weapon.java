package model;

public abstract class Weapon extends ActionObjet {
	protected float state;
	protected float chargeTime;
	protected float weight;
	protected Character owner;
	protected float damage;

	public void setOwner(Character owner){
		this.owner = owner;
		if(owner!=null)
			this.setXY(owner.getX(),owner.getY());
	}
	
	
}
