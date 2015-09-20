package technologies;

import org.newdawn.slick.Image;

public enum Technologies {



	DualistAge2("Age2",00f,00f,30f,"headquarters",1,0),
//	DualistAge2("Age2",100f,100f,300f,"headquarters",1,0),
	DualistAge3("Age3",200f,200f,300f,"headquarters",2,1),
	DualistBonusFood("bonus food",50f,50f,150f,"mill",2,2),
	DualistBonusGold("bonus gold",50f,50f,150f,"mine",2,3),
	DualistShield2("shield 2",50f,100f,150f,"headquarters",2,4),
	DualistHealth2("health 2",100f,50f,150f,"headquarters",2,5),
	DualistShield3("shield 3",200f,200f,250f,"headquarters",3,6),
	DualistHealth3("health 3",200f,200f,250f,"headquarters",3,7),

	DualistContactAttack2("Contact attack 2",100f,150f,200f,"university",2,8),
	DualistRangeAttack2("Range attack 2",100f,200f,200f,"university",2,9),
	DualistContactAttack3("Contact attack 3",200f,350f,400f,"university",3,10),
	DualistRangeAttack3("Range attack 3",200f,350f,400f,"university",3,11),
	
	DualistExplosion("Explosion",300f,300f,300f,"headquarters",3,12),
	EagleView("Eagle View",100f,100f,100f,"university",2,13);
	

	public String name = "";
	public String building;
	public float prodTime;
	public float foodPrice;
	public float goldPrice;
	public int age;
	Image icon;
	
	Technologies(String name,float food,float gold,float prodTime,String building,int age,int id){

		this.name = name;
		this.goldPrice = gold;
		this.foodPrice = food;
		this.prodTime = prodTime;
		this.building = building;
		this.age = age;
	}
	
	
	
}


