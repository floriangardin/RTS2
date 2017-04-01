package events;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Sound;

import main.Main;
import model.Character;
import model.Game;
import model.Objet;

public class EventDeath extends Event{

	float x,y;
	private float delta_y;
	private float alpha = 1.0f;
	private Color color;
	private Image im;
	private float duration = 5f;
	
	public EventDeath(Objet parent) {
		super(parent);
		x = parent.x;
		y = parent.y;
		color = parent.getGameTeam().color;
		delta_y = 0;
		int direction = 0;
		if(parent instanceof Character){
			direction = (((Character)parent).orientation/2-1);
			// inverser gauche et droite
			if(direction==1 || direction==2){
				direction = ((direction-1)*(-1)+2);
			}
			
		}
		im = Game.g.images.getUnit(parent.name, direction, 0, parent.getGameTeam().id, false);
		this.sounds = Game.g.sounds.getSoundVector(parent.name, "death");
		if(parent.getGameTeam().id==Game.g.currentPlayer.getGameTeam().id){
			Game.g.sounds.get("deathAlly").play(1f,0.9f*Game.g.options.soundVolume*ratioDistance());
		} else {
			Game.g.sounds.get("deathEnemy").play(1f,0.9f*Game.g.options.soundVolume*ratioDistance());
			Game.g.currentPlayer.getGameTeam().addNewKill();
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