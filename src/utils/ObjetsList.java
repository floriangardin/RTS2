package utils;

import java.util.Vector;

public enum ObjetsList {

	// Prototypes
	Objet("Objet"),
	Spell("Objet"),
	Unit("Character"),
	Building("Building"),
	Bullet("Bullet"),
	Technology("Technology"),
	Bonus("Building"),
	// Units
	Knight("Character"),
	Crossbowman("Character"),
	Priest("Character"),
	Inquisitor("Character"),
	Spearman("Character"),
	Archange("Character"),
	Test("Character"),
	// Buildings
	Headquarters("Building"),
	Mill("Building"),
	Mine("Building"),
	Barracks("Building"),
	Stable("Building"),
	Academy("Building"),
	University("Building"),
	Tower("Building"),
	// Bonus
	BonusLifepoints("Building"),
	BonusSpeed("Building"),
	BonusDamage("Building"),
	// Technologies
	DualistAge2("Technology"), 
	DualistAge3("Technology"), 
	DualistBonusFood("Technology"), 
	DualistBonusGold("Technology"), 
	DualistShield2("Technology"), 
	DualistHealth2("Technology"), 
	DualistShield3("Technology"), 
	DualistHealth3("Technology"), 
	DualistContactAttack2("Technology"), 
	DualistRangeAttack2("Technology"), 
	DualistContactAttack3("Technology"), 
	DualistRangeAttack3("Technology"), 
	DualistExplosion("Technology"), 
	DualistEagleView("Technology"),
	DualistImmolationAuto("Technology"),
	
	
	// Bullets
	Arrow("Bullet"),
	Fireball("Bullet"),
	// Nature
	Tree00("NatureObject"),
	Tree01("NatureObject"),
	Tree02("NatureObject"),
	Tree03("NatureObject"),
	Water("NatureObject"), 
	// SpellEffects
	BlessedAreaEffect("SpellEffect"), 
	BurningAreaEffect("SpellEffect"), 
	FirewallEffect("SpellEffect"), 
	FrozenEffect("SpellEffect"), 
	HealEffect("SpellEffect"),
	// Spells
	BlessedArea("Spell"),
	Conversion("Spell"),
	Dash("Spell"),
	Eclair("Spell"),
	Firewall("Spell"),
	Frozen("Spell"),
	Heal("Spell"),
	Immolation("Spell"),
	Meditation("Spell"),
	InstantDeath("Spell"),
	InstantHealth("Spell"),
	SpecialArrow("Spell"),
	Product("Spell"),
	// Others
	ContactWeapon("Weapon"), 
	;
	
	
	String type;
	
	private ObjetsList(String type){
		this.type = type;
	}
	public String getType(){
		
		
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
