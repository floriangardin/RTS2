package madness;

import events.Event;
import events.Events;
import model.Civilisation.AttributsCiv;
import model.Game;
import model.GameTeam;

public class ObjectiveImmolation  extends ObjectiveMadness{

	public ObjectiveImmolation(GameTeam gameTeam,int value) {
		super(gameTeam);

		objective= value;

		this.cardList = AttributsCiv.choiceMadnessAct1;

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
