package events;

import org.newdawn.slick.Graphics;

import control.Player;
import plateau.Objet;
import plateau.Plateau;
import ressources.Sounds;

public strictfp class EventHealth extends Event {

	public EventHealth(Objet parent, Plateau plateau) {
		super(parent, plateau);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean play(Graphics g, Plateau plateau, boolean toDraw) {
		// TODO Auto-generated method stub
		if(plateau.isVisibleByTeam(Player.team, parent)){			
			Sounds.playSoundAt("health", parent.getX(), parent.getY());
		}
		return false;
	}

}
