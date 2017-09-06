package spells;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import control.Player;
import data.Attributs;
import main.Main;
import model.Game;
import plateau.Character;
import plateau.Checkpoint;
import plateau.Objet;
import plateau.Plateau;
import utils.ObjetsList;
import utils.Utils;

// TODO : Sort
public class SpellHealth extends Spell{

	public SpellHealth(){
		this.name = ObjetsList.Health;
	}

	public void launch(Objet target, Character launcher, Plateau plateau){
		
		Objet h = target;
		
		if(h instanceof Character && h.getTeam()==team && Utils.distance(target, launcher) <  launcher.getAttribut(Attributs.range)){
			h.setLifePoints(h.lifePoints+(h.getAttribut(Attributs.maxLifepoints)-h.lifePoints)/2, plateau);
			//TODO add a sound
		}
	}
	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean ok, Plateau plateau) {
		g.setLineWidth(2f*Main.ratioSpace);
		g.setAntiAlias(true);
		// Draw on target
		if(Player.mouseOver==-1){			
			Objet t = Spell.realTarget(new Checkpoint(x,y, plateau), launcher, launcher.getAttribut(Attributs.range),true, plateau);
			g.setColor(Color.red);
			g.drawOval(t.x-this.getAttribut(Attributs.size)/2,t.y-this.getAttribut(Attributs.size)/2, this.getAttribut(Attributs.size), this.getAttribut(Attributs.size));
		}
		g.setAntiAlias(false);
	}

}


