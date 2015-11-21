package units;

import org.newdawn.slick.geom.Circle;

import bullets.Arrow;
import model.Data;
import model.GameTeam;
import model.Plateau;
import model.Player;


public class UnitCrossbowman extends Character {

	public UnitCrossbowman(Plateau p, GameTeam gameteam, Data data) {
		super(p, gameteam);
		this.name = "crossbowman";
		this.type = UnitsList.Crossbowman;
		this.maxLifePoints = 60f;
		this.lifePoints = this.maxLifePoints;
		this.sight = 300f;
		this.collisionBox = new Circle(0f,0f,this.size);
		this.maxVelocity = 95f;
		this.armor = 2f;
		this.damage = 5f*data.damageFactor;
		this.chargeTime = 10f;
		this.weapon ="bow";

		this.civ = 0;
		this.range = 200f;
		this.sightBox = new Circle(0,0,this.sight);
		this.spells.add(data.immolation);

		this.updateImage();
	}
	public UnitCrossbowman(UnitCrossbowman unit, float x, float y,int id) {
		super(unit,x,y,id);
	}

	public void useWeapon(){

		new Arrow(this.p,this,this.getTarget().getX()-this.getX(),this.getTarget().getY()-this.getY(),this.damage,-1);
		this.state = 0f;



	}

}
