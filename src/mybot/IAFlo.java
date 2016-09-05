package mybot;

import java.util.Vector;

import bot.IA;
import bot.IAAllyObject;
import bot.IAUnit;
import model.Player;
import utils.ObjetsList;

public class IAFlo extends IA{

	public IAFlo(Player p) {
		super(p);
		
	}

	
	@Override
	public void update() {
		// Take nearest barrack with one unit
		Vector<IAAllyObject> units = this.getUnits();
		
//		if(units.size()>0){
//			IAUnit barrack = units.get(0).getNearestNeutral(ObjetsList.Barracks);
//			if(barrack!=null){				
//				units.get(0).rightClick(barrack);
//			}
//		}
		
		
		
	}
	
	
	
}
