package technologies;

import org.newdawn.slick.Image;

public enum Technologies {



	DualistAge2("Age2",0f,0f,100f,"headquarters"),
	DualistAge3("Age3",100f,100f,150f,"headquarters"),
	DualistBonusFood("bonus food",0f,0f,50f,"mill"),
	DualistBonusGold("bonus gold",0f,0f,50f,"mine"),
	DualistShield2("shield 2",20f,20f,200f,"headquarters"),
	DualistHealth2("health 2",30f,30f,200f,"headquarters"),
	DualistShield3("shield 3",50f,50f,250f,"headquarters"),
	DualistHealth3("health 3",50f,50f,250f,"headquarters"),

	DualistContactAttack2("Contact attack 2",20f,20f,200f,"university"),
	DualistRangeAttack2("Range attack 2",30f,30f,200f,"university"),
	DualistContactAttack3("Contact attack 3",50f,50f,250f,"university"),
	DualistRangeAttack3("Range attack 3",50f,50f,250f,"university"),
	
	DualistExplosion("Explosion",20f,50f,150f,"headquarters"),
	EagleView("Eagle View",0f,0f,50f,"university");
	

	public String name = "";
	public String building;
	public float prodTime;
	public float foodPrice;
	public float goldPrice;
	Image icon;
	
	Technologies(String name,float food,float gold,float prodTime,String building){

		this.name = name;
		this.goldPrice = gold;
		this.foodPrice = food;
		this.prodTime = prodTime;
		this.building = building;
	}
	

}


