package events;

import org.newdawn.slick.Graphics;

public abstract class GraphicEvent {
	
	
	public GraphicEvent(Event parent){
		this.parent = parent;
	}
	
	Event parent;
	public abstract void draw(Graphics g);
	
	
	public static GraphicEvent createGraphicEvent(GraphicEvents name,Event parent){
		switch(name){
		case ArrowWind:
			return new GraphicWindArrow(parent);
		case BonusTaken:
			return new GraphicBonusTaken(parent);	
		default:
			return null;
		}
		
	}
}
