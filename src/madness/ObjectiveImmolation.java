package madness;

import events.Event;
import events.Events;
import model.Game;
import model.GameTeam;

public class ObjectiveImmolation  extends ObjectiveMadness{

	public ObjectiveImmolation(GameTeam gameTeam) {
		super(gameTeam);
		objective= 2;
	}

	
	@Override
	public void action() {
		for(Event e : Game.g.getEvents().getNewEvents()){
			if(e.getName()==Events.Immolation){
				this.current++;
			}
		}
	}
}
