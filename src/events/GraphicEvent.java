package events;

import org.newdawn.slick.Graphics;

public abstract class GraphicEvent {

	Event parent;
	
	
	
	public abstract void draw(Graphics g);
	
	
	public static GraphicEvent createGraphicEvent(GraphicEvents name,Event parent){
		GraphicEvent e ;
		switch(name){
		case ArrowWind:
			e = new GraphicWindArrow();
			e.parent = parent;
			return e;
		case BonusTaken:
			e = new GraphicBonusTaken();
			e.parent = parent;
			return e;

		default:
			return null;
		}
		
	}
}
