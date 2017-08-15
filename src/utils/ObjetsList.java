package utils;

import java.util.Vector;

public enum ObjetsList {

	// Prototypes
	Objet(ObjetType.Objet),
	Spell(ObjetType.Objet),
	Unit(ObjetType.Character),
	Building(ObjetType.Building),
	Bullet(ObjetType.Bullet),
	Technology(ObjetType.Technology),
	Bonus(ObjetType.Bonus),
	Checkpoint(ObjetType.Checkpoint),
	// Units
	Knight(ObjetType.Character),
	Crossbowman(ObjetType.Character),
	Priest(ObjetType.Character),
	Inquisitor(ObjetType.Character),
	Spearman(ObjetType.Character),
	Archange(ObjetType.Character),
	// Buildings
	Headquarters(ObjetType.Building),
	Mill(ObjetType.Building),
	Mine(ObjetType.Building),
	Barracks(ObjetType.Building),
	Stable(ObjetType.Building),
	Academy(ObjetType.Building),
	University(ObjetType.Building),
	Tower(ObjetType.Building),
	// Bonus
	BonusLifepoints(ObjetType.Bonus),
	BonusSpeed(ObjetType.Bonus),
	BonusDamage(ObjetType.Bonus),
	// Technologies
	DualistAge2(ObjetType.Technology), 
	DualistAge3(ObjetType.Technology), 
	DualistBonusFood(ObjetType.Technology), 
	DualistBonusGold(ObjetType.Technology), 
	DualistShield2(ObjetType.Technology), 
	DualistHealth2(ObjetType.Technology), 
	DualistShield3(ObjetType.Technology), 
	DualistHealth3(ObjetType.Technology), 
	DualistContactAttack2(ObjetType.Technology), 
	DualistRangeAttack2(ObjetType.Technology), 
	DualistContactAttack3(ObjetType.Technology), 
	DualistRangeAttack3(ObjetType.Technology), 
	DualistExplosion(ObjetType.Technology), 
	DualistEagleView(ObjetType.Technology),
	DualistImmolationAuto(ObjetType.Technology),
	
	
	// Bullets
	Arrow(ObjetType.Bullet),
	Fireball(ObjetType.Bullet),
	// Nature
	Tree00(ObjetType.NatureObject),
	Tree01(ObjetType.NatureObject),
	Tree02(ObjetType.NatureObject),
	Tree03(ObjetType.NatureObject),
	// SpellEffects
	BlessedAreaEffect(ObjetType.SpellEffect), 
	BurningAreaEffect(ObjetType.SpellEffect), 
	FirewallEffect(ObjetType.SpellEffect), 
	FrozenEffect(ObjetType.SpellEffect), 
	HealEffect(ObjetType.SpellEffect),
	// Spells
	BlessedArea(ObjetType.Spell),
	Conversion(ObjetType.Spell),
	Dash(ObjetType.Spell),
	Eclair(ObjetType.Spell),
	Firewall(ObjetType.Spell),
	Frozen(ObjetType.Spell),
	Heal(ObjetType.Spell),
	Immolation(ObjetType.Spell),
	Meditation(ObjetType.Spell),
	InstantDeath(ObjetType.Spell),
	InstantHealth(ObjetType.Spell),
	SpecialArrow(ObjetType.Spell),
	Product(ObjetType.Spell),
	// Weapons
	
	ContactWeapon(ObjetType.Weapon),
	;
	
	public ObjetType type;
	
	private ObjetsList(ObjetType type){
		this.type = type;
	}
	
	
	public ObjetType getType(){
		return type;
	}

	public String getName(){
		return this.name().toLowerCase();
	}
	public static ObjetsList get(String name){
		for(ObjetsList o : ObjetsList.values()){
			if(o.name().toLowerCase().equals(name.toLowerCase())){
				return o;
			}
		}
		System.out.println(name+" pose un problème car est nul !");
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
	
	public static Vector<ObjetsList> getBuildings() {
		Vector<ObjetsList> v = new Vector<ObjetsList>();
		v.add(Barracks);
		v.add(Academy);
		v.add(Stable);
		v.add(Mill);
		v.add(Mine);
		v.add(Headquarters);
		v.add(University);
		v.add(Tower);
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
		v.add(Meditation);
		v.add(InstantDeath);
		v.add(InstantHealth);
		v.add(SpecialArrow);
		v.add(Product);
		return v;
	}




}
