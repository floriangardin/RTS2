package units;

import data.Attributs;
import main.Main;
import utils.UnitsList;

public class UnitPriest extends Character {

	public static float radiusCollisionBox = 20f*Main.ratioSpace;


	public UnitPriest(float x, float y,int team) {
		super(x,y,UnitsList.Priest,team);
	}

	public void action(){
		if(this.mode==TAKE_BUILDING){
			this.mode = NORMAL;
		}
		mainAction();
	}
	
	
	public void useWeapon(){
		Character c = (Character) this.target;
		// Attack sound
		float damage = this.getAttribut(Attributs.damage);
		//TODO Put SOund
		//this.p.g.sounds.getByName(this.weapon).play(1f,this.p.g.options.soundVolume);
		if(c.getAttribut(Attributs.armor)<damage){
			c.setLifePoints(c.lifePoints+c.getAttribut(Attributs.armor)-damage);
		}
		// Reset the state
		this.state = 0f;
		this.isAttacking = false;
	}
}
