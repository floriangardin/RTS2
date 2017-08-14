package events;

import java.util.Vector;

import org.newdawn.slick.Graphics;

import display.Camera;
import display.DisplayRessources;
import plateau.Objet;
import plateau.Character;
import plateau.Plateau;

public class EventHandler {

	private static Vector<Event> events = new Vector<Event>();
	private static Vector<DisplayRessources> displayRessources = new Vector<DisplayRessources>();
	private static boolean isInit;

	public static void init(){
		events = new Vector<Event>();
		displayRessources = new Vector<DisplayRessources>();
		isInit=true;
	}
	

	public static void render(Graphics g, Plateau plateau, boolean topLayer){
		Vector<Event> toRemove1 = new Vector<Event>();
		for(Event e : events){
			if(e.topLayer!=topLayer){
				continue;
			}
			if(!e.play(g, plateau)){
				toRemove1.addElement(e);
			}
		}
		events.removeAll(toRemove1);
		if(!topLayer){
			return;
		}
	}
	
	public static void addEvent(EventNames name,Objet parent, Plateau plateau){
		if(!isInit){
			return;
		}
		events.addElement(name.createEvent(parent, plateau));
	}
	
	public static void addEvent(Event event, Plateau plateau){
		if(plateau==null){
			return;
		}
		events.addElement(event);
	}
	
	public static void addEventBuildingTaking(Character parent, Objet building, Plateau plateau){
		if(!isInit){
			return;
		}
		EventBuildingTakingGlobal ebgt = null;
		for(Event e : events){
			if(e instanceof EventBuildingTakingGlobal && ((EventBuildingTakingGlobal)e).parent.id==building.id){
				ebgt = ((EventBuildingTakingGlobal)e);
				break;
			}
		}
		for(Event e : events){
			if(e instanceof EventBuildingTaking && e.parent.id == parent.id && ((EventBuildingTaking)e).idTarget == building.id){
				if(!((EventBuildingTaking)e).isActive){
					((EventBuildingTaking)e).isActive = true;
					ebgt.addAttacker(parent.team.id);
				}
				return;
			}
		}
		ebgt.addAttacker(parent.team.id);
		events.addElement(new EventBuildingTaking(parent, plateau, ebgt));
	}
	
}
