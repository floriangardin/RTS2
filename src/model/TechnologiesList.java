package model;

import org.newdawn.slick.Image;

public enum TechnologiesList {



	Age2("Age2",10f,0f,50f),
	Age3("Age3",10f,0f,50f),
	Sight100("Priest",10f,5f,40f);
	

	String name = "";
	int ageRequired;
	float prodTime;
	float foodPrice;
	float goldPrice;
	Technologie techRequired;
	Image icon;
	
	TechnologiesList(String name,float food,float gold,float prodTime){

		this.name = name;
		this.goldPrice = gold;
		this.foodPrice = food;
		this.prodTime = prodTime;
	}
	
	public void applyEffect(){
		switch(this){
		case Age2:
			break;
		case Age3:
			break;
		case Sight100:
			break;
		}
	}
}


