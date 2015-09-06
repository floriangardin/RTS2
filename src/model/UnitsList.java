package model;

public enum UnitsList {

	Knight("Knight",10f,0f),
	Bowman("Bowman",10f,0f),
	Priest("Priest",10f,5f),
	Wizard("Wizard",10f,5f),
	Spearman("Spearman",10f,0f);
	
	String name = "";
	float goldPrice ;
	float foodPrice;
  UnitsList(String name,float food,float gold){

	    this.name = name;
	    this.goldPrice = gold;
	    this.foodPrice = food;

	  }
}
