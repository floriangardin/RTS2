package madness;

import events.Event;
import events.Events;
import model.Game;
import model.GameTeam;

public class ObjectiveMeditation  extends ObjectiveMadness{

	public ObjectiveMeditation(GameTeam gameTeam,int value) {
		super(gameTeam);
		objective= value;
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
