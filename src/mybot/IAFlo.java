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
		Vector<IAAllyObject> units = this.getUnits();
		if(units.size()>0){
			IAUnit barrack = units.get(0).getNearestNeutral(ObjetsList.Barracks);
			units.get(0).rightClick(barrack);
			this.sendMessage("Coucou");
		}

		
		round++;	
		
	}
	
	public void tellBoObjectiveFinished(ObjetsList o){
		boManager.finishedChild(o);
	}
	
	
	
}
