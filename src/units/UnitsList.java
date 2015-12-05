package units;

public enum UnitsList {

	Knight("knight",100f,50f,0f,100f),
	Crossbowman("crossbowman",50f,50f,0f,60f),
	Priest("priest",50f,100f,0f,100f),
	Inquisitor("inquisitor",60f,100f,0f,120f),
	Spearman("spearman",100f,0f,0f,60f),
	Archange("archange",0f,0f,5f,0f),
	Test("test",0f,0f,0f,0f);
	
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
		case "test" : return Test;
		default: return null;
		}
	}
	
}
