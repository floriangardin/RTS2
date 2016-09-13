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
	
	
	/*
	 * TODO : Not used yet, to be used with
	 */
	public void action(){
		//Update sounds
		for(Event e: events){
			e.action();
		}
	}
	
	public void addEvent(Events name,Objet o){
		this.events.addElement(new Event(name,o));
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
