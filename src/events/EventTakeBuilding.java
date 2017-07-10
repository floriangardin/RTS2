package events;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Sound;

import main.Main;
import model.Building;
import model.Character;
import model.Game;
import model.Objet;

public class EventTakeBuilding extends Event{

	float x,y;

	public Sound sound;

	public EventTakeBuilding(Objet parent) {
		super(parent);
		x = parent.x;
		y = parent.y;
		
		if(parent.getGameTeam().id==Game.g.currentPlayer.getGameTeam().id){
			sound = Game.g.sounds.get("takeBuilding");
			sound.loop(1f, Game.g.options.soundVolume*ratioDistance());
		}else{
			// TODO : Trouver un autre son
			sound = Game.g.sounds.get("takeBuilding");
			sound.loop(1f, Game.g.options.soundVolume*ratioDistance());
		}
	}

	@Override
	public boolean play(Graphics g) {
		// draw shadow of unit
		this.duration -= Main.increment;

		if( !(parent.getTarget() instanceof Building)){
			sound.stop();
			return false;
		}
		return true;
	}
	
	public void drawAfter(Graphics g){

		float x = Game.g.bottomBar.convertXMinimap(this.parent.x);
		float y = Game.g.bottomBar.convertYMinimap(this.parent.y);
		// TODO : Draw somthing on minimap if ennemy attack your building
	}


}
