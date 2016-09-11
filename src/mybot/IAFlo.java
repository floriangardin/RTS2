package mybot;

import java.util.Vector;

import bot.IA;
import bot.IAAllyObject;
import bot.IAUnit;
import model.Player;
import utils.ObjetsList;

public class IAFlo extends IA{
	
	
	BOObjective boManager;
	ObjetsList currentObjective;
	boolean newObjective = false;
	int round = 0;
	
	public IAFlo(Player p) {
		super(p);
		
	}

	
	@Override
	public void update() {
		// Settle objective
		if(round==10){
			this.boManager = new BOObjective(this,ObjetsList.Crossbowman);
		}
		if(boManager!=null && currentObjective==null){
			currentObjective = boManager.getObjective();
			newObjective = true;
		}
		
		// Find how to do the objective depending on unit or building
		
		// Assign everyone on the objective
		for(IAAllyObject u : this.getUnits()){
			
		}
		
		round++;	
		
	}
	
	public void tellBoObjectiveFinished(ObjetsList o){
		boManager.finishedChild(o);
	}
	
	
	
}
