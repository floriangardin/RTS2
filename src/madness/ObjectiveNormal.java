package madness;

import java.util.Vector;

import events.Event;
import events.Events;
import model.Civilisation.AttributsCiv;
import model.Game;
import model.GameTeam;
import utils.ObjetsList;

public class ObjectiveNormal  extends ObjectiveMadness{

	public ObjectiveNormal(GameTeam gameTeam,int value, Vector<ObjetsList> list) {
		super(gameTeam,value,list);
		
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
