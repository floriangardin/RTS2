package units;

import org.newdawn.slick.geom.Circle;

import model.Data;
import model.Plateau;
import model.Player;
import weapon.Bow;

public class UnitCrossbowman extends Character {

	public UnitCrossbowman(Plateau p, Player player, Data data) {
		super(p, player);
		this.name = "crossbowman";
		this.maxLifePoints = 60f;
		this.lifePoints = this.maxLifePoints;
		this.sight = 300f;
		this.collisionBox = new Circle(0f,0f,this.size);
		this.maxVelocity = 100f;
		this.armor = 2f;
		this.damage = 5f;
		this.chargeTime = 5f;
		this.weapon = new Bow(this.p,this);
		this.weapon.destroy();
		this.civ = 0;
		this.range = 200f;
		this.sightBox = new Circle(0,0,this.sight);
		this.spells.add(data.immolation);

		this.updateImage();
	}
	public UnitCrossbowman(UnitCrossbowman unit, float x, float y) {
		super(unit,x,y);
	}

}
