package display;

import org.newdawn.slick.Graphics;

import model.Civilisation;
import model.Game;
import model.Utils;

public class SpellInterface extends Bar{
	
	BottomBar parent;
	Civilisation c;
	float x, y, sizeX, sizeY;

	public SpellInterface(BottomBar parent){
		this.parent = parent;
		this.c = Game.g.currentPlayer.getGameTeam().civ;
		this.x = 0;
		this.y = 0;
		this.sizeX = parent.ratioSelectionX*Game.g.resX;
		this.sizeY = sizeX*1.5f;
	}
	
	public Graphics draw(Graphics g){
		Utils.drawNiceRect(g, Game.g.currentPlayer.getGameTeam().color, x, y, sizeX, sizeY);
		g.drawImage(Game.g.images.get(c.uniqueSpell.name).getScaledCopy((int)(sizeX-20),(int)(sizeX-20)), x+10, y+10);
		// TODO handle Cooldown
		return g;
	}

}
