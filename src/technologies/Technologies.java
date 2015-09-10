package technologies;

import org.newdawn.slick.Image;

public enum Technologies {



	DualistAge2("Age2",10f,0f,50f),
	DualistAge3("Age3",10f,0f,50f),
	Sight100("Eagle View",10f,5f,40f);
	

	String name = "";
	int ageRequired;
	float prodTime;
	float foodPrice;
	float goldPrice;
	Image icon;
	
	Technologies(String name,float food,float gold,float prodTime){

		this.name = name;
		this.goldPrice = gold;
		this.foodPrice = food;
		this.prodTime = prodTime;
	}
	

}


