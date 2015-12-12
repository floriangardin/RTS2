package units;

import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;

import model.Data;
import model.Game;
import model.GameTeam;
import model.Horse;
import model.Plateau;
import model.Player;

public class UnitPriest extends Character {

	public UnitPriest(Plateau p, GameTeam gameteam, Data data) {
		super(p, gameteam);
		this.name = "priest";
		this.type = UnitsList.Priest;
		this.unitType = PRIEST;
		this.maxLifePoints = 60f*data.healthFactor;
		this.lifePoints = this.maxLifePoints;
		this.sight = 300f*Game.ratioSpace;
		this.attackDuration = 2f;
		this.collisionBox = new Circle(0f,0f,this.size);
		this.selectionBox = new Rectangle(-1.5f*this.image.getWidth()/5,-2.5f*this.image.getHeight()/4,3*this.image.getWidth()/5,3*this.image.getHeight()/4);
		this.maxVelocity = 80f*Game.ratioSpace;
		this.armor = 1f;
		this.damage = 0f*data.damageFactor;
		this.chargeTime = 0.2f;
		this.weapon = "bible";
		this.explosionWhenImmolate = data.explosionWhenImmolate;
		this.civ = 0;
		this.sightBox = new Circle(0,0,this.sight);
		this.range = 70f*Game.ratioSpace;
		this.horse = new Horse(p,this);
		this.spells.add(data.immolation);
		this.spells.add(data.conversion);
		this.updateImage();
	}

	public UnitPriest(UnitPriest unit, float x, float y,int id) {
		super(unit,x,y,id);
	}

	public void action(){
		if(this.mode==TAKE_BUILDING){
			this.mode = NORMAL;
		}
		mainAction();
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
		this.isAttacking = false;
	}
}
