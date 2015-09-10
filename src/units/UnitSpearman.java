package units;

import org.newdawn.slick.geom.Circle;

import model.Plateau;
import model.Player;
import weapon.Spear;

public class UnitSpearman extends Character {

	public UnitSpearman(Plateau p, Player player) {
		super(p, player);
		this.name = "spearman";
		this.maxLifePoints = 80f;
		this.lifePoints = this.maxLifePoints;
		this.sight = 300f;
		this.collisionBox = new Circle(0f,0f,20f);
		this.maxVelocity = 80f;
		this.armor = 4f;
		this.damage = 5f;
		this.chargeTime = 5f;
		this.weapon = new Spear(this.p,this);
		this.civ = 0;
		this.sightBox = new Circle(this.sight,0,0);
		this.updateImage();
	}

	public UnitSpearman(UnitSpearman unit, float x, float y) {
		super(unit,x,y);
	}

	
	

}
