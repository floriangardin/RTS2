package events;

import java.util.Vector;

import org.newdawn.slick.Graphics;

import display.Camera;
import display.DisplayRessources;
import plateau.Objet;
import plateau.Plateau;

public class EventHandler {

	private static Vector<Event> events;
	private static Vector<DisplayRessources> displayRessources;
	private static Plateau plateau;
	private static Camera camera;

	public static void init(Plateau plateau, Camera camera){
		events = new Vector<Event>();
		displayRessources = new Vector<DisplayRessources>();
		EventHandler.plateau= plateau;
		EventHandler.camera = camera;
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
		if(plateau==null){
			return;
		}
		events.addElement(name.createEvent(parent, plateau, camera));
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
