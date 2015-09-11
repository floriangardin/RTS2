package units;

import org.newdawn.slick.geom.Circle;

import model.Data;
import model.Horse;
import model.Plateau;
import model.Player;
import weapon.Sword;

public class UnitKnight extends Character {

	public UnitKnight(Plateau p, Player player, Data data) {
		super(p, player);
		this.name = "knight";
		this.maxLifePoints = 110f;
		this.lifePoints = this.maxLifePoints;
		this.sight = 300f;
		this.collisionBox = new Circle(0f,0f,this.size);
		this.maxVelocity = 110f;
		this.armor = 5f;
		this.damage = 8f;
		this.chargeTime = 7f;
		this.weapon = new Sword(this.p,this);
		this.weapon.destroy();
		this.civ = 0;
		this.sightBox = new Circle(0,0,this.sight);
		this.range = this.size+20f;
		this.horse = new Horse(p,this);
		this.spells.add(data.immolation);
		
		this.updateImage();
	}
	
	public UnitKnight(UnitKnight unit, float x, float y) {
		super(unit,x,y);
	}


}
