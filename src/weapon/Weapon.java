package weapon;

import buildings.Building;
import model.ActionObjet;
import units.Character;

public abstract class Weapon extends ActionObjet {
	public float state;
	public float chargeTime;
	public Character owner;
	public float damage;

	public void setOwner(Character owner){
		this.owner = owner;
		if(owner!=null)
			this.setXY(owner.getX(),owner.getY());
	}

	public void collision(Building c){
		
	}
	
}
