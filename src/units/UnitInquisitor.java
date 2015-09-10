package units;

import org.newdawn.slick.geom.Circle;

import model.Plateau;
import model.Player;
import weapon.Wand;

public class UnitInquisitor extends Character {

	public UnitInquisitor(Plateau p, Player player) {
		super(p, player);
		this.name = "inquisitor";
		this.maxLifePoints = 60f;
		this.lifePoints = this.maxLifePoints;
		this.sight = 300f;
		this.collisionBox = new Circle(0f,0f,20f);
		this.maxVelocity = 60f;
		this.armor = 0f;
		this.damage = 5f;
		this.chargeTime = 15f;
		this.weapon = new Wand(this.p,this);
		this.civ = 0;
		this.sightBox = new Circle(0,0,this.sight);
		this.range = 200f;
		this.weapon.destroy();
		this.updateImage();
	}

	public UnitInquisitor(UnitInquisitor spearman, float x, float y) {
		super(spearman,x,y);
	}

}
