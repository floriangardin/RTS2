package model;

public class Bible extends ContactWeapon{

	public Bible(Character owner){
		this.damage = -10f;
		this.frequency = 10f;
		this.lifePoints = 1f;
		this.setOwner(owner);
		this.setXY(owner.getX(),owner.getY());
	}
}