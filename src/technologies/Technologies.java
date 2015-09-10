package technologies;

import org.newdawn.slick.Image;

public enum Technologies {



	DualistAge2("Age2",0f,0f,100f),
	DualistAge3("Age3",100f,100f,150f),
	EagleView("Eagle View",10f,5f,50f);
	

	String name = "";
	public float prodTime;
	public float foodPrice;
	public float goldPrice;
	Image icon;
	
	Technologies(String name,float food,float gold,float prodTime){

		this.name = name;
		this.goldPrice = gold;
		this.foodPrice = food;
		this.prodTime = prodTime;
	}
	

}


