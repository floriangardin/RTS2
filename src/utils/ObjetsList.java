package utils;

import java.util.Vector;

public enum ObjetsList {

	// Prototypes
	Objet,
	Spell,
	Unit,
	Building,
	Bullet,
	Technology,
	Bonus,
	// Units
	Knight,
	Crossbowman,
	Priest,
	Inquisitor,
	Spearman,
	Archange,
	Test,
	// Buildings
	Headquarters,
	Mill,
	Mine,
	Barracks,
	Stable,
	Academy,
	University,
	Tower,
	// Bonus
	BonusLifepoints,
	BonusSpeed,
	BonusDamage,
	// Technologies
	DualistAge2, 
	DualistAge3, 
	DualistBonusFood, 
	DualistBonusGold, 
	DualistShield2, 
	DualistHealth2, 
	DualistShield3, 
	DualistHealth3, 
	DualistContactAttack2, 
	DualistRangeAttack2, 
	DualistContactAttack3, 
	DualistRangeAttack3, 
	DualistExplosion, 
	DualistEagleView,
	// Bullets
	Arrow,
	Fireball,
	// Nature
	Tree00,
	Tree01,
	Tree02,
	Tree03,
	Water, 
	// SpellEffects
	BlessedAreaEffect, 
	BurningAreaEffect, 
	FirewallEffect, 
	FrozenEffect, 
	HealEffect,
	// Spells
	BlessedArea,
	Conversion,
	Dash,
	Eclair,
	Firewall,
	Frozen,
	Heal,
	Immolation,
	InstantDeath,
	InstantHealth,
	SpecialArrow,
	Product,
	// Others
	ContactWeapon
	;

	public String getName(){
		return this.name().toLowerCase();
	}
	public static ObjetsList get(String name){
		for(ObjetsList o : ObjetsList.values()){
			if(o.name().toLowerCase().equals(name.toLowerCase())){
				return o;
			}
		}
		System.out.println(name);
		return null;
	}

	public static Vector<ObjetsList> getUnits() {
		Vector<ObjetsList> v = new Vector<ObjetsList>();
		v.add(Spearman);
		v.add(Knight);
		v.add(Crossbowman);
		v.add(Inquisitor);
		v.add(Priest);
		v.add(Archange);
		return v;
	}
	


	public static Vector<ObjetsList> getSpells() {
		Vector<ObjetsList> v = new Vector<ObjetsList>();
		v.add(BlessedArea);
		v.add(Conversion);
		v.add(Dash);
		v.add(Eclair);
		v.add(Firewall);
		v.add(Frozen);
		v.add(Heal);
		v.add(Immolation);
		v.add(InstantDeath);
		v.add(InstantHealth);
		v.add(SpecialArrow);
		v.add(Product);
		return v;
	}




}
