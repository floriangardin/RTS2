package model;

public enum UnitsList {

	Knight("Knight",10f,0f,50f),
	Bowman("Bowman",10f,0f,50f),
	Priest("Priest",10f,5f,40f),
	Wizard("Wizard",10f,5f,60f),
	Spearman("Spearman",10f,0f,40f);
	
	String name = "";
	float goldPrice ;
	float foodPrice;
	float time;
  UnitsList(String name,float food,float gold,float prodTime){

	    this.name = name;
	    this.goldPrice = gold;
	    this.foodPrice = food;
	    this.time = prodTime;
	  }
}
