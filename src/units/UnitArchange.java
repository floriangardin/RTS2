package units;

import org.newdawn.slick.geom.Circle;

import model.Data;
import model.Plateau;
import model.Player;
import spells.SpellBlessedArea;
import spells.SpellFirewall;
import weapon.Sword;
import weapon.Wand;

public class UnitArchange extends Character {

	public UnitArchange(Plateau p, Player player, Data data) {
		super(p, player);
		this.name = "archange";
		this.maxLifePoints = 200f;
		this.lifePoints = this.maxLifePoints;
		this.sight = 300f;
		this.collisionBox = new Circle(0f,0f,20f);
		this.maxVelocity = 60f;
		this.armor = 5f;
		this.damage = 20f;
		this.chargeTime = 12f;
		this.weapon = new Sword(this.p,this);
		this.civ = 0;
		this.sightBox = new Circle(0,0,this.sight);
		this.range = this.size+20f;
		this.weapon.destroy();
		this.spells.add(data.immolation);
		this.spells.add(data.firewall);
		this.spells.add(data.blessedArea);
		this.updateImage();
	}

	public UnitArchange(UnitArchange archange, float x, float y) {
		super(archange,x,y);
	}

}
