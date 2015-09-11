package units;

import org.newdawn.slick.geom.Circle;

import model.Data;
import model.Horse;
import model.Plateau;
import model.Player;
import weapon.Bible;

public class UnitPriest extends Character {

	public UnitPriest(Plateau p, Player player, Data data) {
		super(p, player);
		this.name = "priest";
		this.maxLifePoints = 60f;
		this.lifePoints = this.maxLifePoints;
		this.sight = 300f;
		this.collisionBox = new Circle(0f,0f,20f);
		this.maxVelocity = 80f;
		this.armor = 1f;
		this.damage = -1f;
		this.chargeTime = 0.2f;
		this.weapon = new Bible(this.p,this);
		this.weapon.destroy();
		this.civ = 0;
		this.sightBox = new Circle(0,0,this.sight);
		this.range = 50f;
		this.horse = new Horse(p,this);
		this.spells.add(data.immolation);
		this.updateImage();
	}

	public UnitPriest(UnitPriest unit, float x, float y) {
		super(unit,x,y);
	}


}
