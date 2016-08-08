package utils;

public enum ObjetsList {

	Knight,
	Crossbowman,
	Priest,
	Inquisitor,
	Spearman,
	Archange,
	Test,
	Headquarters,
	Mill,
	Mine,
	Barracks,
	Stable,
	Academy,
	University,
	Tower,
	BonusLifepoints,
	BonusSpeed,
	BonusDamage,
	DualistAge2, 
	DualistAge3, 
	DualistBonusFood, 
	DualistBonusGold, 
	DualistShield2, 
	DualistHealth2, 
	DualistShield3, 
	DualistHealth3, 
	DualistContact2, 
	DualistRangeAttack2, 
	DualistContact3, 
	DualistRangeAttack3, 
	DualistExplosion, 
	DualistEagleView
	;

	
public String name;

private ObjetsList(){
	this.name = this.name().toLowerCase();
}

	
	
}
