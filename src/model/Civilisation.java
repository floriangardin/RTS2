package model;

import spells.Spell;
import spells.SpellEclair;

public class Civilisation {
	String name;
	Spell uniqueSpell;
	
	public Civilisation(String name){
		this.name = name;
		switch(name.toLowerCase()){
		case "dualist":
			this.uniqueSpell = new SpellEclair();
			break;
		case "kitano":
			break;
		case "zinaid":
			break;
		}
		
	}

}
