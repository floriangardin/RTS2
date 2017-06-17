package display;

import java.util.Vector;

import org.newdawn.slick.Graphics;

import events.Event;
import events.EventNames;
import plateau.Objet;

public class DisplayHandler {

	private static Vector<Event> events;
	private static Vector<DisplayRessources> displayRessources;

	public static void init(){
		events = new Vector<Event>();
		displayRessources = new Vector<DisplayRessources>();
	}
	
	public static void render(Graphics g){
		Vector<Event> toRemove1 = new Vector<Event>();
		for(Event e : events){
			if(!e.play(g)){
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
	

	

	
	public static void addEvent(EventNames name,Objet parent){
		events.addElement(name.createEvent(parent));
	}
	
	public static void addDisplayRessources(DisplayRessources dr){
		displayRessources.addElement(dr);
	}
	
	public static void addEvent(Event event){
		events.addElement(event);
	}
	
	public static Vector<Event> getNewEvents(){
		Vector<Event> res = new Vector<Event>();
		for(Event e: events){
			if(e.isNewEvent()){
				res.add(e);
			}
		}
		return res;
	}
	
	public static Vector<Event> getEvents(){
		return events;
	}

	
	
}
