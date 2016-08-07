package technologies;

import utils.ObjetsList;


public enum Technologies {




	DualistAge2("Âge de la Ferveur","techFervourAge",100f,100f,100f,ObjetsList.Headquarters,1,0),
	DualistAge3("Âge du Fanatisme","techFanatismAge",200f,200f,100f,ObjetsList.Headquarters,2,1),
	DualistBonusFood("Bonus de Nourriture","techBonusFood",50f,50f,150f,ObjetsList.Mill,2,2),
	DualistBonusGold("Bonus d'Or","techBonusGold",50f,50f,150f,ObjetsList.Mine,2,3),
	DualistShield2("Bouclier 2","techShield2",50f,100f,110f,ObjetsList.Headquarters,1,4),
	DualistHealth2("Santé 2","techHealth2",100f,50f,150f,ObjetsList.Headquarters,2,5),
	DualistShield3("Bouclier 3","techShield3",200f,200f,250f,ObjetsList.Headquarters,3,6),
	DualistHealth3("Santé 3","techHealth3",200f,200f,250f,ObjetsList.Headquarters,3,7),
	DualistContactAttack2("Attaque au Contact 2","techContactAttack2",100f,150f,200f,ObjetsList.University,2,8),
	DualistRangeAttack2("Attaque à Distance II","techRangeAttack2",100f,200f,200f,ObjetsList.University,2,9),
	DualistContactAttack3("Attaque au Contact III","techContactAttack3",200f,350f,400f,ObjetsList.University,3,10),
	DualistRangeAttack3("Attaque à Distance III","techRangeAttack3",200f,350f,400f,ObjetsList.University,3,11),
	DualistExplosion("Explosion","techExplosion",100f,100f,120f,ObjetsList.Headquarters,2,12),
	EagleView("Vision d'Aigle","techEagleView",100f,100f,100f,ObjetsList.University,2,13);
	

	public String name = "";
	public String nameIcon = "";
	public ObjetsList building;
	public float prodTime;
	public float foodPrice;
	public float goldPrice;
	public int age;
	
	Technologies(String name,String nameIcon,float food,float gold,float prodTime,ObjetsList building,int age,int id){

		this.name = name;
		this.nameIcon = nameIcon;
		this.goldPrice = gold;
		this.foodPrice = food;
		this.prodTime = prodTime;
		this.building = building;
		this.age = age;
	}
	
	
	
}


