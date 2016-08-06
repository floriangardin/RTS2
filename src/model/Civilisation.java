package model;

import main.Main;
import spells.Spell;
import spells.SpellEclair;
import spells.SpellHeal;
import spells.SpellProduct;

public class Civilisation {
	public String name;
	public Spell uniqueSpell;
	public String printName;
	public GameTeam gameteam;
	
	public float chargeTime;
	
	public Civilisation(String name,GameTeam gameteam){
		this.name = name;
		this.gameteam = gameteam;
		switch(name.toLowerCase()){
		case "dualists":
			this.printName = "Dualists";
			this.uniqueSpell = new SpellEclair(gameteam);
			break;
		case "kitanos":
			this.printName = "Kitanos";
			this.uniqueSpell = new SpellHeal(gameteam);
			break;
		case "zinaids":
			this.printName = "Zinaids";
			this.uniqueSpell = new SpellProduct(gameteam);
			break;
		}
	}
	
	public void launchSpell(Objet target){
		if(target!=null && gameteam.special>=this.uniqueSpell.faithCost && chargeTime>=uniqueSpell.chargeTime){
			gameteam.special-=this.uniqueSpell.faithCost;
			this.uniqueSpell.launch(target, null);
		}
		
	}
	
	public void update(){
		this.chargeTime = (float) Math.min(uniqueSpell.chargeTime, chargeTime+Main.increment);
	}


}
