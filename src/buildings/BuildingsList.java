package buildings;

public enum BuildingsList {

	Headquarters,
	Mill,
	Mine,
	Barrack,
	Stable,
	Academy,
	University,
	Tower,
	BonusLifePoint,
	BonusSpeed,
	BonusAttack;

	BuildingsList(){}

	public static BuildingsList switchName(String name){
		switch(name){
		case "barrack" : return Barrack;
		case "mill" : return Mill;
		case "mine" : return Mine;
		case "headquarters" : return Headquarters;
		case "stable" : return Stable;
		case "academy" : return Academy;
		case "university" : return University;
		case "tower" : return Tower;
		default: return null;
		}
	}
}
