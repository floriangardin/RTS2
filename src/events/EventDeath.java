package events;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import display.Camera;
import main.Main;
import model.Game;
import plateau.Character;
import plateau.Objet;
import plateau.Plateau;
import ressources.Images;
import ressources.Sounds;

public class EventDeath extends Event{

	float x,y;
	private float delta_y;
	private float alpha = 1.0f;
	private Color color;
	private Image im;
	private float duration = 5f;
	
	public EventDeath(Objet parent, Plateau plateau, Camera camera) {
		super(parent, plateau, camera);
		x = parent.x;
		y = parent.y;
		color = parent.getTeam().color;
		delta_y = 0;
		int direction = 0;
		if(parent instanceof Character){
			direction = (((Character)parent).orientation/2-1);
			// inverser gauche et droite
			if(direction==1 || direction==2){
				direction = ((direction-1)*(-1)+2);
			}
		}
		im = Images.getUnit(parent.name, direction, 0, parent.getTeam().id, false);
		if(parent.getTeam().id==Game.gameSystem.getCurrentTeam()){
			Sounds.playSound("deathAlly", ratioDistance());
		} else {
			Sounds.playSound("deathEnemy", ratioDistance());
			//GameSystem.currentPlayer.getTeam().addNewKill();
		}
	}

	@Override
	public boolean play(Graphics g) {
		// draw shadow of unit
		this.playSound();
		delta_y+=3.5f;
		alpha*=0.95f;
		im.setAlpha(alpha);
		im.draw(x-im.getWidth()/2,y-delta_y-3*im.getHeight()/4,im.getWidth(),im.getHeight(),color);
		im.setAlpha(1.0f);
		this.duration -= Main.increment;
		return this.duration>0f;
	}


}
