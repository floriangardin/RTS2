package model;

import data.Attributs;
import main.Main;
import spells.Spell;
import utils.SpellsList;

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
			this.uniqueSpell = Game.g.data.spells.get(SpellsList.Eclair);
			break;
		case "kitanos":
			this.printName = "Kitanos";
			this.uniqueSpell = Game.g.data.spells.get(SpellsList.Heal);
			break;
		case "zinaids":
			this.printName = "Zinaids";
			this.uniqueSpell = Game.g.data.spells.get(SpellsList.Product);
			break;
		}
	}
	
	public void launchSpell(Objet target){
		if(target!=null && gameteam.special>=this.uniqueSpell.getAttribut(Attributs.faithCost) && chargeTime>=uniqueSpell.getAttribut(Attributs.chargeTime)){
			gameteam.special-=this.uniqueSpell.getAttribut(Attributs.faithCost);
			this.uniqueSpell.launch(target, null);
		}
		
	}
	
	public void update(){
		this.chargeTime = (float) Math.min(uniqueSpell.getAttribut(Attributs.chargeTime), chargeTime+Main.increment);
	}


}
