package units;

import org.newdawn.slick.geom.Circle;

import model.Plateau;
import model.Player;
import weapon.Bow;

public class UnitCrossbowman extends Character {

	public UnitCrossbowman(Plateau p, Player player) {
		super(p, player);
		this.name = "spearman";
		this.maxLifePoints = 60f;
		this.lifePoints = this.maxLifePoints;
		this.sight = 300f;
		this.collisionBox = new Circle(0f,0f,20f);
		this.maxVelocity = 90f;
		this.armor = 2f;
		this.damage = 5f;
		this.chargeTime = 5f;
		this.weapon = new Bow(this.p,this);
		this.weapon.destroy();
		this.civ = 0;
		this.sightBox = new Circle(this.sight,0,0);

		this.updateImage();
	}
	public UnitCrossbowman(UnitCrossbowman unit, float x, float y) {
		super(unit,x,y);
	}

}