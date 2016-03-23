package units;

public enum UnitsList {

	Knight("Knight","Chevalier",100f,50f,0f,80f),
	Crossbowman("Crossbowman","Archer",50f,50f,0f,60f),
	Priest("Priest","Prêtre",50f,100f,0f,60f),
	Inquisitor("Inquisitor","Inquisiteur",60f,100f,0f,80f),
	Spearman("Spearman","Lancier",100f,0f,0f,60f),
	Archange("Archange","Archange",0f,0f,5f,0f),
	Test("test","Vaneau",0f,0f,0f,0f);
	
	public String name = "";
	public String printName = "";
	public float goldPrice ;
	public float foodPrice;
	public float specialPrice;
	public float time;
	UnitsList(String name,String printName, float food,float gold,float special,float prodTime){

	    this.name = name;
	    this.printName = name;
	    this.goldPrice = gold;
	    this.foodPrice = food;
	    this.time = prodTime;
	    this.specialPrice = special;
	 }
	
	public static UnitsList switchName(String name){
		switch(name){
		case "Knight" : return Knight;
		case "Crossbowman" : return Crossbowman;
		case "Spearman" : return Spearman;
		case "Priest" : return Priest;
		case "Inquisitor" : return Inquisitor;
		case "Archange" : return Archange;
		case "Test" : return Test;
		default: return null;
		}
	}
	
}
