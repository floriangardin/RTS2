package model;

import spells.Spell;
import spells.SpellEclair;

public class Civilisation {
	public String name;
	public Spell uniqueSpell;
	public String printName;
	
	
	public Civilisation(String name){
		this.name = name;
		switch(name.toLowerCase()){
		case "dualists":
			this.uniqueSpell = new SpellEclair();
			break;
		case "kitanos":
			break;
		case "zinaids":
			break;
		}
	}


}
