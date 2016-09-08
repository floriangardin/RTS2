package mybot;

import java.util.Vector;

import bot.IA;
import bot.IAAllyObject;
import bot.IAUnit;
import model.Player;
import utils.ObjetsList;

public class IAFlo extends IA{
	
	
	BOObjective boManager;
	int round = 0;
	
	public IAFlo(Player p) {
		super(p);
		
	}

	
	@Override
	public void update() {
		// Take nearest barrack with one unit
		Vector<IAAllyObject> units = this.getUnits();
		if(round == 10){
			
			this.boManager = new BOObjective(this,ObjetsList.Crossbowman);
		}
//		if(units.size()>1){
//			IAUnit barrack = units.get(0).getNearestNeutral(ObjetsList.Barracks);
//			if(barrack!=null){				
//				units.get(0).rightClick(barrack);
//			}
//			if(units.get(1).canLaunch(ObjetsList.Dash)){
//				units.get(1).launchSpell(units.get(1).getX()+50f,units.get(1).getY()+50f , ObjetsList.Dash);
//			}
//		}
		
		round++;
		
	}
	
	public void tellBoObjectiveFinished(ObjetsList o){
		boManager.finishedChild(o);
	}
	
	
	
}
