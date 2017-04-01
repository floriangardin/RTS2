package events;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Sound;

import model.Game;
import model.Objet;

public class EventDefault extends Event{

	public EventDefault(Objet parent, String s){
		super(parent);
		this.sounds.add(Game.g.sounds.get(s));
	}
	
	public EventDefault(Objet parent, Sound s){
		super(parent);
		this.sounds.add(s);
	}
	
	public boolean play(Graphics g){
		// Calculate intensity in function of options and distance to scene
		playSound();
		return false;
	}

	public void draw(Graphics g) {}
	
	
}
