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
	
	
	IAUnit target ;
	
	
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
		if(round!=10){
			return;
		}
		// Check if should change objective
		
		
		Vector<IAAllyObject> units = this.getUnits();
		if(units.size()>0){
			IAUnit firstUnit = units.get(0);
			// Find how to do the objective depending on unit or building
			if(currentObjective.getType().equals("Building")){
				// Find nearest building from unit selected
				target = firstUnit.getNearestNeutral(currentObjective);
			}
			if(currentObjective.getType().equals("Character")){
				// Try to produce it if ressources
				// Find the building which produce it
				Vector<IAAllyObject> myProducers = this.getMyProducers(currentObjective);
				if(myProducers.size()>0){
					// Produce in the producer
					myProducers.get(0).produceUnit(currentObjective);
				}
			}
			// Assign everyone on the objective
			for(IAAllyObject u : this.getUnits()){
				
			}
		}

		
		round++;	
		
	}
	
	public void tellBoObjectiveFinished(ObjetsList o){
		boManager.finishedChild(o);
	}
	
	
	
}
