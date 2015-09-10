package units;

import org.newdawn.slick.geom.Circle;

import model.Horse;
import model.Plateau;
import model.Player;
import weapon.Bible;

public class UnitPriest extends Character {

	public UnitPriest(Plateau p, Player player) {
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
		this.sightBox = new Circle(this.sight,0,0);
		this.horse = new Horse(p,this);
		this.updateImage();
	}

	public UnitPriest(UnitPriest unit, float x, float y) {
		super(unit,x,y);
	}


}
