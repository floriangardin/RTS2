package events;

import java.util.Vector;

import model.Objet;

import org.newdawn.slick.Graphics;

public class EventQueue {

	private Vector<Event> events = new Vector<Event>();
	public EventQueue(){
	}
	
	public void render(Graphics g){
		Vector<Event> toRemove = new Vector<Event>();
		for(Event e : events){
			if(!e.play(g)){
				toRemove.addElement(e);
			}
		}
		events.removeAll(toRemove);
	}
	

	
	public void addEvent(EventNames name,Objet parent){
		this.events.addElement(name.createEvent(parent));
	}
	
	public void addEvent(Event event){
		this.events.addElement(event);
	}
	
	public Vector<Event> getNewEvents(){
		Vector<Event> res = new Vector<Event>();
		for(Event e: events){
			if(e.isNewEvent()){
				res.add(e);
			}
		}
		return res;
	}
	
	public Vector<Event> getEvents(){
		return events;
	}
	
	
}
