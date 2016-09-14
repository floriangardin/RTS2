package model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Vector;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import data.Attributs;
import madness.ActCard;
import madness.ObjectiveImmolation;
import madness.Objective;
import madness.ObjectiveMeditation;
import main.Main;
import spells.Spell;
import utils.ObjetsList;

public class Civilisation {
	public String name;
	public Spell uniqueSpell;
	public String printName;
	public GameTeam gameteam;
	
	public int madness = 0;
	
	
	public Vector<ObjetsList> cardSelection = new Vector<ObjetsList>();
	
	public Objective objectiveMadness;
	public Objective objectiveWisdom;

	public HashMap<AttributsCiv, String> attributsString = new HashMap<AttributsCiv, String>();
	public HashMap<AttributsCiv, Vector<ActCard>> cardChoices = new HashMap<AttributsCiv, Vector<ActCard>>();
	public static Gson gson = new Gson();


	public float chargeTime;


	public Civilisation(String name,GameTeam gameteam){
		this.name = name;

		this.gameteam = gameteam;
		switch(name.toLowerCase()){
		case "dualists":
			this.printName = "Dualists";
			this.uniqueSpell = Game.g.data.spells.get(ObjetsList.Eclair);
			break;
		case "kitanos":
			this.printName = "Kitanos";
			this.uniqueSpell = Game.g.data.spells.get(ObjetsList.Heal);
			break;
		case "zinaids":
			this.printName = "Zinaids";
			this.uniqueSpell = Game.g.data.spells.get(ObjetsList.Product);
			break;
		}
		try {
			attributsString = gson.fromJson(
					new JsonReader(
							new FileReader("ressources/data/civilisation/"+name.toLowerCase()+".json")), 
					new TypeToken<HashMap<AttributsCiv, String>>(){}.getType());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
		Vector<AttributsCiv> toRemove = new Vector<AttributsCiv>();
		for(AttributsCiv a : attributsString.keySet()){
			if(a.name().startsWith("choice")){
				cardChoices.put(a, new Vector<ActCard>());
				if(attributsString.get(a).length()>2){
					for(String element : attributsString.get(a).split("-")){
						
						cardChoices.get(a).add(ActCard.actCard(ObjetsList.get(element), gameteam.id));
					}
				}
				toRemove.add(a);
			} 

		}
		for(AttributsCiv a : toRemove){
			attributsString.remove(a);
		}
		this.objectiveMadness = new ObjectiveImmolation(this.gameteam,new int[]{2,5,10});
		this.objectiveWisdom = new ObjectiveMeditation(this.gameteam,new int[]{2,5,10});
		
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
	
	public enum AttributsCiv{
		name,
		choiceAct1,
		choiceMadnessAct1,
		choiceReasonAct1,
		choiceAct2,
		choiceMadnessAct2,
		choiceReasonAct2,
		choiceAct3,
		choiceMadnessAct3,
		choiceReasonAct3;
	}
	
	public void print(){
		
		System.out.println(" civilisation : "+this.name);
		System.out.println();
		System.out.println("choix");
		for(AttributsCiv ac : this.cardChoices.keySet()){
			System.out.print(ac.name()+" : ");
			for(ActCard actCard : this.cardChoices.get(ac)){
				System.out.println(actCard.getName()+" ");
			}
			System.out.println();
		}
		System.out.println();
		
	}
	
	public Vector<ActCard> getChoices(int nbChoice, boolean madness, boolean reason){
		String s = "choice"
				+(madness && !reason ? "Madness" : (reason && !madness ? "Reason" : "Act"))+nbChoice;
		return this.cardChoices.get(AttributsCiv.valueOf(s));
	}


}
