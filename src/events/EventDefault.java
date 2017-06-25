package events;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Sound;

import display.Camera;
import plateau.Objet;
import plateau.Plateau;
import ressources.Sounds;

public class EventDefault extends Event{

	public EventDefault(Objet parent, String s, Plateau plateau, Camera camera){
		super(parent, plateau, camera);
		this.sounds.add(Sounds.get(s));
	}
	
	public EventDefault(Objet parent, Sound s, Plateau plateau, Camera camera){
		super(parent, plateau, camera);
		this.sounds.add(s);
	}
	
	public boolean play(Graphics g){
		// Calculate intensity in function of options and distance to scene
		playSound();
		return false;
	}

	public void draw(Graphics g) {}
	
	
}
