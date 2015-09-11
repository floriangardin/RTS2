package units;

public enum UnitsList {

	Knight("knight",10f,0f,0f,50f),
	Crossbowman("crossbowman",10f,0f,0f,50f),
	Priest("priest",10f,5f,0f,40f),
	Inquisitor("inquisitor",10f,5f,0f,60f),
	Spearman("spearman",10f,0f,0f,40f),
	Archange("archange",0f,0f,100f,0f);
	
	public String name = "";
	public float goldPrice ;
	public float foodPrice;
	public float specialPrice;
	public float time;
	UnitsList(String name,float food,float gold,float special,float prodTime){

	    this.name = name;
	    this.goldPrice = gold;
	    this.foodPrice = food;
	    this.time = prodTime;
	    this.specialPrice = special;
	 }
	
	public static UnitsList switchName(String name){
		switch(name){
		case "knight" : return Knight;
		case "crossbowman" : return Crossbowman;
		case "spearman" : return Spearman;
		case "priest" : return Priest;
		case "inquisitor" : return Inquisitor;
		case "archange" : return Archange;
		default: return null;
		}
	}
}
