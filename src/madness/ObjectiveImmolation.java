package madness;


import events.Event;
import events.EventNames;
import model.Game;
import model.GameTeam;

public class ObjectiveImmolation  extends Objective{

	public ObjectiveImmolation(GameTeam gameTeam,int[] value, boolean madness) {
		super(gameTeam,value, madness);
	}

	@Override
	public boolean action() {
		for(Event e : Game.g.getEvents().getNewEvents()){
			if(e.getName()==EventNames.Immolation && e.getGameTeam()==this.gameTeam.id){
				return true;
			}
		}
		return false;
	}
}
