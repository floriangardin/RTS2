package madness;


import events.Event;
import events.Events;
import model.Civilisation.AttributsCiv;
import model.Game;
import model.GameTeam;

public class ObjectiveImmolation  extends Objective{

	public ObjectiveImmolation(GameTeam gameTeam,int[] value) {
		super(gameTeam,value);
		
		this.cardList = AttributsCiv.choiceMadnessAct1;
	}

	
	@Override
	public void action() {
		System.out.println("roger");
		for(Event e : Game.g.getEvents().getNewEvents()){
			if(e.getName()==Events.Immolation && e.getGameTeam()==this.gameTeam.id){
				this.current++;
				System.out.println("vaneau");
			}
		}
	}
}
