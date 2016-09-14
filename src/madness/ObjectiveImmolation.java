package madness;


import events.Event;
import events.Events;
import model.Game;
import model.GameTeam;

public class ObjectiveImmolation  extends Objective{

	public ObjectiveImmolation(GameTeam gameTeam,int[] value, boolean madness) {
		super(gameTeam,value, madness);
	}

	@Override
	public boolean action() {
		for(Event e : Game.g.getEvents().getNewEvents()){
			System.out.println(e.getName()+" "+e.getGameTeam()+" "+this.gameTeam.id);
			if(e.getName()==Events.Immolation && e.getGameTeam()==this.gameTeam.id){
				return true;
			}
		}
		return false;
	}
}
