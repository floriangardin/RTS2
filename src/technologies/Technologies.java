package technologies;

import org.newdawn.slick.Image;

public enum Technologies {



	DualistAge2("Age2",0f,0f,100f,"headquarters",1),
	DualistAge3("Age3",100f,100f,150f,"headquarters",2),
	DualistBonusFood("bonus food",0f,0f,50f,"mill",2),
	DualistBonusGold("bonus gold",0f,0f,50f,"mine",2),
	DualistShield2("shield 2",20f,20f,200f,"headquarters",2),
	DualistHealth2("health 2",30f,30f,200f,"headquarters",2),
	DualistShield3("shield 3",50f,50f,250f,"headquarters",3),
	DualistHealth3("health 3",50f,50f,250f,"headquarters",3),

	DualistContactAttack2("Contact attack 2",20f,20f,200f,"university",2),
	DualistRangeAttack2("Range attack 2",30f,30f,200f,"university",2),
	DualistContactAttack3("Contact attack 3",50f,50f,250f,"university",3),
	DualistRangeAttack3("Range attack 3",50f,50f,250f,"university",3),
	
	DualistExplosion("Explosion",20f,50f,150f,"headquarters",3),
	EagleView("Eagle View",0f,0f,50f,"university",2);
	

	public String name = "";
	public String building;
	public float prodTime;
	public float foodPrice;
	public float goldPrice;
	public int age;
	Image icon;
	
	Technologies(String name,float food,float gold,float prodTime,String building,int age){

		this.name = name;
		this.goldPrice = gold;
		this.foodPrice = food;
		this.prodTime = prodTime;
		this.building = building;
		this.age = age;
	}
	

}


