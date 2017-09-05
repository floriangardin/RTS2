package events;

import org.newdawn.slick.Graphics;

import plateau.Objet;
import plateau.Plateau;
import ressources.Sounds;

public class EventUnitCreated extends Event {

	public EventUnitCreated(Objet parent, Plateau plateau) {
		super(parent, plateau);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean play(Graphics g, Plateau plateau, boolean toDraw) {
		Sounds.playSoundAt("heureux", parent.x, parent.y);
		return false;
	}

}
