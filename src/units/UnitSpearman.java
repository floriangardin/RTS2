package units;

import data.Attributs;
import main.Main;
import model.Game;
import utils.UnitsList;

public class UnitSpearman extends Character {
	
	public static float radiusCollisionBox = 40f*Main.ratioSpace;
	public float inDash=0f;
	public boolean bonusAttack;
	public float bonusSpeed =400f;
	public float bonusDamage = 10f;

	public UnitSpearman(float x, float y, int team) {
		super(x, y, UnitsList.Spearman, team);
	}

	public void useWeapon(){
		if(! (this.target instanceof Character)){
			return ;
		}
		Character c = (Character) this.target;
		c.isAttacked();
		// Attack sound
		float bonus = bonusAttack ? bonusDamage : 0f;
		bonusAttack = false;
		float damage = this.getAttribut(Attributs.damage)+bonus;
		if(Game.g.sounds!=null)
			Game.g.sounds.get(this.getAttributString(Attributs.weapon)).play(1f,Game.g.options.soundVolume);
		if(c.horse)
			damage = damage*this.getGameTeam().data.bonusSpearHorse;

		if(c.getAttribut(Attributs.armor)<damage){
			c.setLifePoints(c.lifePoints+c.getAttribut(Attributs.armor)-damage);
		}
		// Reset the state
		this.state = 0f;
		this.isAttacking = false;
	}


}




