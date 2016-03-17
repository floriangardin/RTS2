package technologies;

import org.newdawn.slick.Image;

public enum Technologies {




	DualistAge2("Fervour Age","techFervourAge",100f,100f,100f,"headquarters",1,0),
	DualistAge3("Fanatism Age","techFanatismAge",200f,200f,100f,"headquarters",2,1),
	DualistBonusFood("Bonus Food","techBonusFood",50f,50f,150f,"mill",2,2),
	DualistBonusGold("Bonus Gold","techBonusGold",50f,50f,150f,"mine",2,3),
	DualistShield2("Shield 2","techShield2",50f,100f,110f,"headquarters",1,4),
	DualistHealth2("Health 2","techFanatismAge",100f,50f,150f,"headquarters",2,5),
	DualistShield3("Shield 3","techFanatismAge",200f,200f,250f,"headquarters",3,6),
	DualistHealth3("Health 3","techFanatismAge",200f,200f,250f,"headquarters",3,7),
	DualistContactAttack2("Contact Attack II","techContactAttack2",100f,150f,200f,"university",2,8),
	DualistRangeAttack2("Range Attack II","techRangeAttack2",100f,200f,200f,"university",2,9),
	DualistContactAttack3("Contact Attack III","techContactAttack3",200f,350f,400f,"university",3,10),
	DualistRangeAttack3("Range Attack III","techRangeAttack3",200f,350f,400f,"university",3,11),
	DualistExplosion("Explosion","techExplosion",100f,100f,120f,"headquarters",2,12),
	EagleView("Eagle View","techEagleView",100f,100f,100f,"university",2,13);
	

	public String name = "";
	public String nameIcon = "";
	public String building;
	public float prodTime;
	public float foodPrice;
	public float goldPrice;
	public int age;
	Image icon;
	
	Technologies(String name,String nameIcon,float food,float gold,float prodTime,String building,int age,int id){

		this.name = name;
		this.nameIcon = nameIcon;
		this.goldPrice = gold;
		this.foodPrice = food;
		this.prodTime = prodTime;
		this.building = building;
		this.age = age;
	}
	
	
	
}


