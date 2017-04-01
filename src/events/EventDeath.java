package events;

import org.newdawn.slick.Graphics;

import model.Objet;

public class EventDeath extends Event{

	private float x,y;
	private float delta_y;
	private float duration = 20f;
	
	public EventDeath(Objet parent) {
		super(parent);
		x = parent.x;
		y = parent.y;
		delta_y = 0;
	}

	@Override
	public boolean play(Graphics g) {
		// TODO Auto-generated method stub
		return false;
	}


}
