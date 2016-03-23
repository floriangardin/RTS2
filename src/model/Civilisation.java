package model;

import spells.Spell;
import spells.SpellEclair;
import spells.SpellHeal;
import spells.SpellProduct;

public class Civilisation {
	public String name;
	public Spell uniqueSpell;
	public String printName;
	public GameTeam gameteam;
	
	public int chargeTime;
	
	public Civilisation(String name,GameTeam gameteam){
		this.name = name;
		this.gameteam = gameteam;
		switch(name.toLowerCase()){
		case "dualists":
			this.printName = "Dualists";
			this.uniqueSpell = new SpellEclair(Game.g.plateau,gameteam);
			break;
		case "kitanos":
			this.printName = "Kitanos";
			this.uniqueSpell = new SpellHeal(Game.g.plateau,gameteam);
			break;
		case "zinaids":
			this.printName = "Zinaids";
			this.uniqueSpell = new SpellProduct(Game.g.plateau,gameteam);
			break;
		}
	}
	
	public void launchSpell(Objet target){
		if(target!=null && gameteam.special>=this.uniqueSpell.faithCost){
			gameteam.special-=this.uniqueSpell.faithCost;
			this.uniqueSpell.launch(target, null);
		}
		
	}


}
