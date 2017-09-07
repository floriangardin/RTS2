package events;

import org.newdawn.slick.Graphics;

import plateau.Objet;
import plateau.Plateau;
import ressources.Sounds;

public strictfp class EventBuildingTaken extends Event {

	public EventBuildingTaken(Objet parent, Plateau plateau) {
		super(parent, plateau);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean play(Graphics g, Plateau plateau, boolean toDraw) {
		// TODO Auto-generated method stub
		Sounds.playSoundAt("mystere_decouverte", parent.x, parent.y);
		return false;
	}

}
