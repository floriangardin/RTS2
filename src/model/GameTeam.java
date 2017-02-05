package model;

import java.util.Vector;

import org.newdawn.slick.Color;

import data.Attributs;
import data.Data;
import madness.ActCard;
import utils.ObjetsList;

public class GameTeam {

	
	public Data data;
	
	public int id;
	public Civilisation civ;
	public String civName;
	public String colorName;
	public int food;
	public Building hq ;
	public Color color;
	private int madness;
	// Objectives of madness
	
	// Current choice of actCard
	public Vector<Vector<ActCard>> currentChoices = new Vector<Vector<ActCard>>();
	// Made choices of actcard
	public Vector<ActCard> choices = new Vector<ActCard>();
	
	
	public GameTeam(Vector<Player> players, int id, int civ) {
		this.id = id;
		switch(civ){
		case 0 : civName = "Dualists"; this.civ = new Civilisation("dualists",this);
		Vector<ObjetsList> imo = new Vector<ObjetsList>();
		imo.addElement(ObjetsList.AgeIImmolationAuto);
		imo.addElement(ObjetsList.AgeIExplosion);
		Vector<ObjetsList> medit = new Vector<ObjetsList>();
		medit.addElement(ObjetsList.AgeIRessource);
		medit.addElement(ObjetsList.Age1AttackBonusMeditation);
		Vector<ObjetsList> normal = new Vector<ObjetsList>();
		normal.addElement(ObjetsList.AgeIRessource);
		normal.addElement(ObjetsList.Age1AttackBonusMeditation);
		
		
		
		break;
		case 1 : civName = "Zinaids";this.civ = new Civilisation("zinaids",this);break;
		case 2 : civName = "kitano";this.civ = new Civilisation("kitanos",this);break;
		default:
		}
		
		
		this.data = new Data(id,this.civ.name);
		if(id==0){
			color = Colors.team0;
			colorName = "neutral";
		}
		else if(id==1){
			color = Colors.team1;
			colorName = "blue";
		}
		else if(id==2){
			color = Colors.team2;
			colorName = "red";
		}
		
	
		
		this.food = 100;
	}
	
	
	public boolean enoughPop(ObjetsList o){
		return (getPop()+this.data.getAttribut(o, Attributs.popTaken)<=getMaxPop()|| this.data.getAttribut(o, Attributs.popTaken)==0 );
	}
	
	
	public int getMadness(){
		return madness;
	}
	
	public void addMadness(int add){
		madness = madness+add;
	}

	public int getPop() {
		
		int result = 0;
		/*
		 * For all buildings check how much pop it gives
		 */
		for(Building b : Game.g.plateau.buildings){
			if(b.getGameTeam().id==this.id){
				result+=b.getAttribut(Attributs.popTaken);
			}
		}
		for(Character b : Game.g.plateau.characters){
			if(b.getGameTeam().id==this.id){
				result+=b.getAttribut(Attributs.popTaken);
			}
		}
		return result;
		
	}



	public int getMaxPop() {
		
		int result = 0;
		/*
		 * For all buildings check how much pop it gives
		 */
		for(Building b : Game.g.plateau.buildings){
			if(b.getGameTeam().id==this.id){
				result+=b.getAttribut(Attributs.popGiven);
			}
		}
		return result;
	}


	
//	public void successObjective(ObjectiveMadness om){
//		objectivesMadness.remove(om);
//	}
//	public void successObjectives(Vector<ObjectiveMadness> om){
//		objectivesMadness.removeAll(om);
//	}
//	public Vector<ObjectiveMadness> getObjectivesMadness(){
//		return objectivesMadness;
//	}
	
	
}
