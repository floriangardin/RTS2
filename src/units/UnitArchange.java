package units;


import events.Events;

import data.Attributs;

import model.Game;
import utils.UnitsList;


public class UnitArchange extends Character {

	

	public UnitArchange(float x, float y, int team) {
		super(x,y,UnitsList.Archange, team);
	}

	
	

	public void useWeapon(){
		Character c = (Character) this.target;
		
		// Attack sound
		float damage = this.getAttribut(Attributs.damage);
	
		if(Game.g.sounds!=null)
			Game.g.sounds.get(this.getAttributString(Attributs.weapon)).play(1f,Game.g.options.soundVolume);

		if(c.getAttribut(Attributs.armor)<damage){
			c.setLifePoints(c.lifePoints+c.getAttribut(Attributs.armor)-damage);
		}
		Game.g.events.addEvent(Events.Attack, this);
		// Reset the state
		this.state = 0f;
		c.isAttacked();
		this.isAttacking = false;
		
	}


}
