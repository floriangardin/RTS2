package madness;

import events.Event;
import events.EventNames;
import model.Game;
import plateau.Team;

public class ObjectiveMeditation  extends Objective{

	public ObjectiveMeditation(Team gameTeam,int[] value, boolean madness) {
		super(gameTeam,value, madness);
	}
	
	
	@Override
	public boolean action() {
		for(Event e : Game.g.getEvents().getNewEvents()){
			if(e.getName()==EventNames.Meditation && e.getGameTeam()==this.gameTeam.id){
				return true;
			}
		}
		return false;
	}
	
	
}
