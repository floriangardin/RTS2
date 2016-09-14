package madness;

import events.Event;
import events.Events;
import model.Game;
import model.GameTeam;
import utils.ObjetsList;

import java.util.Vector;

public class ObjectiveMeditation  extends Objective{

	public ObjectiveMeditation(GameTeam gameTeam,int[] value) {
		super(gameTeam,value);
	}
	
	
	@Override
	public void action() {
		for(Event e : Game.g.getEvents().getNewEvents()){
			if(e.getName()==Events.Meditation && e.getGameTeam()==this.gameTeam.id){
				this.current++;
			}
		}
	}
	
	
}
