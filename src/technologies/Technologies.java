package technologies;

import org.newdawn.slick.Image;

public enum Technologies {



	DualistAge2("Age2",0f,0f,100f,"headquarters"),
	DualistAge3("Age3",100f,100f,150f,"headquarters"),
	DualistBonusFood("bonus food",0f,0f,50f,"mill"),
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


