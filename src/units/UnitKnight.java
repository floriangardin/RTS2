package units;

import org.newdawn.slick.geom.Circle;

import model.Data;
import model.GameTeam;
import model.Horse;
import model.Plateau;
import model.Player;

public class UnitKnight extends Character {

	public UnitKnight(Plateau p, GameTeam gameteam, Data data) {
		super(p, gameteam);
		this.name = "knight";
		this.type = UnitsList.Knight;
		this.maxLifePoints = 90f;
		this.lifePoints = this.maxLifePoints;
		this.sight = 300f;
		this.collisionBox = new Circle(0f,0f,this.size);
		this.maxVelocity = 110f;
		this.armor = 3f;
		this.damage = 8f*data.damageFactor;
		this.chargeTime = 7f;
		this.weapon = "sword";
		
		this.civ = 0;
		this.sightBox = new Circle(0,0,this.sight);
		this.range = this.size+20f;
		this.horse = new Horse(p,this);
		this.spells.add(data.immolation);
		
		this.updateImage();
	}
	
	public UnitKnight(UnitKnight unit, float x, float y,int id) {
		super(unit,x,y,id);
	}

	
	public void useWeapon(){
		Character c = (Character) this.target;
		c.changes.lifePoints=true;
		// Attack sound
		float damage = this.damage;
		if(this.p.g.sounds!=null)
			this.p.g.sounds.getByName(this.weapon).play(1f,this.p.g.options.soundVolume);
		if(c.weapon=="bow"){
			damage = damage*this.getGameTeam().data.bonusSwordBow;
		}
		if(c.armor<damage){
			c.setLifePoints(c.lifePoints+c.armor-damage);
		}
		// Reset the state
		this.state = 0f;
	}

}
