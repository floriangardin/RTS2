package units;

public enum UnitsList {

	Knight("knight",10f,0f,50f),
	Crossbowman("crossbowman",10f,0f,50f),
	Priest("priest",10f,5f,40f),
	Inquisitor("inquisitor",10f,5f,60f),
	Spearman("spearman",10f,0f,40f);
	
	public String name = "";
	public float goldPrice ;
	public float foodPrice;
	public float time;
  UnitsList(String name,float food,float gold,float prodTime){

	    this.name = name;
	    this.goldPrice = gold;
	    this.foodPrice = food;
	    this.time = prodTime;
	  }
}
