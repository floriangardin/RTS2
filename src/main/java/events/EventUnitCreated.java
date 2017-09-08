package events;

import org.newdawn.slick.Graphics;

import control.Player;
import plateau.Objet;
import plateau.Plateau;
import ressources.Sounds;

public strictfp class EventUnitCreated extends Event {

	public EventUnitCreated(Objet parent, Plateau plateau) {
		super(parent, plateau);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean play(Graphics g, Plateau plateau, boolean toDraw) {
		if(Player.team == parent.getTeam().id){			
			Sounds.playSoundAt("heureux", parent.getX(), parent.getY());
		}
		return false;
	}

}
