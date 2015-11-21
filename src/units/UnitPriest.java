package units;

import org.newdawn.slick.geom.Circle;

import model.Data;
import model.GameTeam;
import model.Horse;
import model.Plateau;
import model.Player;

public class UnitPriest extends Character {

	public UnitPriest(Plateau p, GameTeam gameteam, Data data) {
		super(p, gameteam);
		this.name = "priest";
		this.type = UnitsList.Priest;
		this.maxLifePoints = 60f;
		this.lifePoints = this.maxLifePoints;
		this.sight = 300f;
		this.collisionBox = new Circle(0f,0f,this.size);
		this.maxVelocity = 80f;
		this.armor = 1f;
		this.damage = 0f;
		this.chargeTime = 0.2f;
		this.weapon = "bible";
		
		this.civ = 0;
		this.sightBox = new Circle(0,0,this.sight);
		this.range = 70f;
		this.horse = new Horse(p,this);
		this.spells.add(data.immolation);
		this.spells.add(data.conversion);
		this.updateImage();
	}

	public UnitPriest(UnitPriest unit, float x, float y,int id) {
		super(unit,x,y,id);
	}

	public void useWeapon(){
		Character c = (Character) this.target;
		c.changes.lifePoints=true;
		// Attack sound
		float damage = this.damage;
		//TODO Put SOund
		//this.p.g.sounds.getByName(this.weapon).play(1f,this.p.g.options.soundVolume);
		if(c.armor<damage){
			c.setLifePoints(c.lifePoints+c.armor-damage);
		}
		// Reset the state
		this.state = 0f;
	}
}
