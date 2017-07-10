package plateau;

import java.io.Serializable;
import java.util.Vector;

import org.newdawn.slick.Color;

import data.Attributs;
import data.Data;
import main.Main;
import model.Colors;
import model.Game;
import utils.ObjetsList;
import ressources.Sounds;
public class Team implements Serializable {


	/**
	 * 
	 */
	public Data data;

	public int id;

	public String colorName;
	public int food;
	public int hq ;
	public Color color;


	// Compteur killing spree
	public int nbKill = 0;
	public float timerKill = 0;
	public float timerMaxKill = 15f;


	public Team(int id, Plateau plateau) {
		this.id = id;
	
		this.data = new Data(this, plateau);
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


	public boolean enoughPop(ObjetsList o, Plateau plateau){
		return (getPop(plateau)+this.data.getAttribut(o, Attributs.popTaken)<=getMaxPop(plateau)|| this.data.getAttribut(o, Attributs.popTaken)==0 );
	}

	public int getPop(Plateau plateau) {

		int result = 0;
		/*
		 * For all buildings check how much pop it gives
		 */
		for(Building b : plateau.buildings){
			if(b.getTeam().id==this.id){
				result+=b.getAttribut(Attributs.popTaken);
			}
		}
		for(Character b : plateau.characters){
			if(b.getTeam().id==this.id){
				result+=b.getAttribut(Attributs.popTaken);
			}
		}
		return result;

	}



	public int getMaxPop(Plateau plateau) {

		int result = 0;
		/*
		 * For all buildings check how much pop it gives
		 */
		for(Building b : plateau.buildings){
			if(b.getTeam().id==this.id){
				result+=b.getAttribut(Attributs.popGiven);
			}
		}
		return result;
	}

	public void addNewKill(){
		this.nbKill += 1;
		this.timerKill = this.timerMaxKill;
//		switch(nbKill){
//		case 2:
//			Sounds.playSound("doubleKill");
//			break;
//		case 3:
//			Sounds.playSound("tripleKill");
//			break;
//		case 4:
//			Sounds.playSound("fourKill");
//			break;
//		case 5:
//			Sounds.playSound("pentaKill");
//			break;
//		default:
//		}

	}
	
	public void update(){
		if(this.timerKill>0f){
			timerKill -= Main.increment;
		} else {
			nbKill = 0;
		}
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
