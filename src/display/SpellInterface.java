package display;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import model.Civilisation;
import model.Game;
import model.Player;
import model.Utils;

public class SpellInterface extends Bar{
	
	BottomBar parent;
	Civilisation c;
	float x, y, sizeX, sizeY;
	float sizeExtraY;
	Image foi;
	private float debut = Game.nbRoundInit/4, duree = debut;

	public SpellInterface(BottomBar parent){
		this.parent = parent;
		this.x = 0;
		this.y = 0;
		this.sizeX = parent.ratioSpellX*Game.g.resX;
		this.sizeExtraY = sizeX/3;
		this.sizeY = sizeX + sizeExtraY;
		this.foi = Game.g.images.get("imageSpecial");
	}
	
	public Graphics draw(Graphics g){
		
		if(Game.g.round<Game.nbRoundInit)
			x = Math.max(-sizeX-10, Math.min(0, sizeX*(Game.g.round-debut-duree)/duree));
		
		
		this.c = Game.g.currentPlayer.getGameTeam().civ;
		Utils.drawNiceRect(g, Game.g.currentPlayer.getGameTeam().color, x, y, sizeX, sizeY);
		g.drawImage(c.uniqueSpell.icon.getScaledCopy((int)(sizeX-20),(int)(sizeX-20)), x+10, y+10);
		g.setColor(Color.white);
		g.drawRect(x+10, y+10, sizeX-20, sizeX-20);
		if(Game.g.currentPlayer.getGameTeam().special>=c.uniqueSpell.faithCost){
			// afficher le cooldown
			g.setColor(Game.g.currentPlayer.getGameTeam().color);
			g.fillRect(x+10, y + sizeX+10, (sizeX-20)*Math.min(1f, 1f*c.chargeTime/c.uniqueSpell.chargeTime), sizeExtraY-20);
			g.setColor(Color.white);
			g.drawRect(x+10, y + sizeX+10, sizeX-20, sizeExtraY-20);
		} else {
			g.setColor(new Color(0.5f,0.5f,0.5f,0.5f));
			g.fillRect(x+10, y+10, sizeX-20, sizeX-20);
			// afficher le cout en foi
			g.drawImage(foi, x + sizeX/8 - foi.getWidth()/2, y + sizeX + sizeExtraY/2-foi.getHeight()/2);
			g.setColor(Color.gray);
			g.fillRect(x + sizeX/4, y + sizeX+10, (5*sizeX/8)*Math.min(1f, 1f*Game.g.currentPlayer.getGameTeam().special/c.uniqueSpell.faithCost), sizeExtraY-20);
			g.setColor(Color.white);
			g.setLineWidth(2f);
			g.drawRect(x + sizeX/4, y + sizeX+10, 5*sizeX/8, sizeExtraY-20);
			g.setLineWidth(1f);
		}
		return g;
	}

}
