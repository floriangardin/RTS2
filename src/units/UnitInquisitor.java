package units;

import org.newdawn.slick.geom.Circle;

import bullets.Fireball;
import model.Data;
import model.GameTeam;
import model.Plateau;
import model.Player;

public class UnitInquisitor extends Character {

	public UnitInquisitor(Plateau p, GameTeam gameteam, Data data) {
		super(p, gameteam);
		this.name = "inquisitor";
		this.type = UnitsList.Inquisitor;
		this.maxLifePoints = 60f*data.healthFactor;
		this.lifePoints = this.maxLifePoints;
		this.sight = 300f;
		this.collisionBox = new Circle(0f,0f,this.size);
		this.maxVelocity = 60f;
		this.armor = 0f;
		this.damage = 5f*data.damageFactor;
		this.chargeTime = 15f;
		this.weapon = "wand";
		this.civ = 0;
		this.sightBox = new Circle(0,0,this.sight);
		this.range = 200f;
	
		this.spells.add(data.immolation);
		this.spells.add(data.firewall);
		this.spells.add(data.blessedArea);
		this.updateImage();
	}

	public UnitInquisitor(UnitInquisitor spearman, float x, float y,int id) {
		super(spearman,x,y,id);	
	}
	
	public void useWeapon(){
		new Fireball(this.p,this,this.getTarget().getX(),this.getTarget().getY(),this.getTarget().getX()-this.getX(),this.getTarget().getY()-this.getY(),this.damage,-1);
		this.state = 0f;
	}

}
