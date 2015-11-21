package units;

import org.newdawn.slick.geom.Circle;

import model.Data;
import model.GameTeam;
import model.Plateau;
import model.Player;

public class UnitSpearman extends Character {

	public UnitSpearman(Plateau p, GameTeam gameteam, Data data) {
		super(p, gameteam);
		
		this.name = "spearman";
		this.type = UnitsList.Spearman;
		this.maxLifePoints = 80f*data.healthFactor;
		this.lifePoints = this.maxLifePoints;
		this.sight = 300f;
		this.collisionBox = new Circle(0f,0f,this.size);
		this.maxVelocity = 80f;
		this.armor = 4f;
		this.damage = 10f*data.damageFactor;
		this.chargeTime = 7f;
		this.weapon = "spear";

		this.civ = 0;
		this.sightBox = new Circle(0,0,this.sight);
		this.range = this.size+20f;
		this.spells.add(data.immolation);
		this.updateImage();
	}
	public UnitSpearman(UnitSpearman unit, float x, float y,int id) {
		super(unit,x,y,id);
	}

	public void useWeapon(){
		Character c = (Character) this.target;
		c.changes.lifePoints=true;
		// Attack sound
		float damage = this.damage;
		
		if(this.p.g.sounds!=null)
			this.p.g.sounds.getByName(this.weapon).play(1f,this.p.g.options.soundVolume);
		if(c.horse!=null)
			damage = damage*this.getGameTeam().data.bonusSpearHorse;

		if(c.armor<damage){
			c.setLifePoints(c.lifePoints+c.armor-damage);
		}
		// Reset the state
		this.state = 0f;
	}


}




