package events;

import org.newdawn.slick.Graphics;

public abstract class GraphicEvent {

	Event parent;
	
	
	
	public abstract void draw(Graphics g);
	
	
	public static GraphicEvent createGraphicEvent(GraphicEvents name,Event parent){
		
		switch(name){
		case ArrowWind:
			return new GraphicWindArrow(parent);
		}
		return null;
	}
}
