package units;

public enum UnitsList {

	Knight("knight",75f,50f,0f,150f),
	Crossbowman("crossbowman",40f,20f,0f,80f),
	Priest("priest",50f,100f,0f,100f),
	Inquisitor("inquisitor",60f,100f,0f,180f),
	Spearman("spearman",60f,0f,0f,100f),
	Archange("archange",0f,0f,7f,0f);
	
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
