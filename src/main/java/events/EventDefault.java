package events;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Sound;

import display.Camera;
import plateau.Objet;
import plateau.Plateau;
import ressources.Sounds;

public strictfp class EventDefault extends Event{

	public EventDefault(Objet parent, String s, Plateau plateau, EventNames name){
		super(parent, plateau);
		this.name = name;
		this.topLayer = true;
		this.sounds.add(s);
	}
	
	public EventDefault(Objet parent, String s, Plateau plateau){
		super(parent, plateau);
		this.sounds.add(s);
	}
	
	@Override
	public boolean play(Graphics g, Plateau plateau, boolean toDraw){
		// Calculate intensity in function of options and distance to scene
		playSound();
		return false;
	}

	public void draw(Graphics g) {}

	
	
}
