package model;

public enum UnitsList {

	Knight("Knight"),
	Bowman("Bowman"),
	Priest("Priest"),
	Wizard("Wizard"),
	Spearman("Spearman");
	
	String name = "";
	
  UnitsList(String name){

	    this.name = name;

	  }
}
