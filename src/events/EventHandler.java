package events;

import java.util.Vector;

import org.newdawn.slick.Graphics;

import display.Camera;
import display.DisplayRessources;
import plateau.Objet;
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
	

	public static void render(Graphics g, Plateau plateau){
		Vector<Event> toRemove1 = new Vector<Event>();
		for(Event e : events){
			if(!e.play(g, plateau)){
				toRemove1.addElement(e);
			}
		}
		events.removeAll(toRemove1);
		Vector<DisplayRessources> toRemove2 = new Vector<DisplayRessources>();
		for(DisplayRessources dr : displayRessources ){
			dr.update();
			if(dr.isDead())
				toRemove2.add(dr);
			else
				dr.draw(g);
		}
		displayRessources.removeAll(toRemove2);
	}
	
	public static void addEvent(EventNames name,Objet parent, Plateau plateau){
		if(!isInit){
			return;
		}
		events.addElement(name.createEvent(parent, plateau));
	}
	
	public static void addDisplayRessources(DisplayRessources dr, Plateau plateau){
		if(plateau==null){
			return;
		}
		displayRessources.addElement(dr);
	}
	
	public static void addEvent(Event event, Plateau plateau){
		if(plateau==null){
			return;
		}
		events.addElement(event);
	}
	
	public static Vector<Event> getNewEvents(Plateau plateau){
		Vector<Event> res = new Vector<Event>();
		for(Event e: events){
			if(e.isNewEvent(plateau)){
				res.add(e);
			}
		}
		return res;
	}
	
	public static Vector<Event> getEvents(){
		return events;
	}

	
	
}
