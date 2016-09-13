package madness;

import events.Event;
import events.Events;
import model.Game;
import model.GameTeam;
import utils.ObjetsList;

import java.util.Vector;

public class ObjectiveMeditation  extends ObjectiveMadness{

	public ObjectiveMeditation(GameTeam gameTeam,int value, Vector<ObjetsList> list) {
		super(gameTeam,value,list);
		
	}

	
	@Override
	public void action() {
		for(Event e : Game.g.getEvents().getNewEvents()){
			if(e.getName()==Events.Meditation){
				this.current++;
			}
		}
	}
}
