package events;

import org.newdawn.slick.Graphics;

public abstract class GraphicEvent {
	
	
	public GraphicEvent(Event parent){
		this.parent = parent;
	}
	
	Event parent;
	public abstract void draw(Graphics g);
	
	
	public static GraphicEvent createGraphicEvent(GraphicEvents name,Event parent){
		GraphicEvent e ;
		switch(name){
		case ArrowWind:
			e = new GraphicWindArrow(parent);
			break;
		case BonusTaken:
			e = new GraphicBonusTaken(parent);
			break;
		default:
			return null;
		}
		return e;
	}
}
