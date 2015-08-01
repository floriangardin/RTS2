package model;

public class Sword extends ContactWeapon {

	
	public Sword(Character owner){
		this.damage = 10f;
		this.frequency = 10f;
		this.lifePoints = 1f;
		this.owner = owner;
		
	}
}
