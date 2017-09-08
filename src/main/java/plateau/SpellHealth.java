package plateau;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import control.Player;
import data.Attributs;
import main.Main;
import model.Game;
import stats.StatsHandler;
import utils.ObjetsList;
import utils.Utils;

// TODO : Sort
public strictfp class SpellHealth extends Spell{

	public SpellHealth(){
		this.name = ObjetsList.Health;
	}

	public boolean launch(Objet target, Character launcher, Plateau plateau){
		
		Objet h = target;
		
		if(h instanceof Character && h.getTeam()==team && Utils.distance(target, launcher) <  launcher.getAttribut(Attributs.range)){
			h.setLifePoints(h.getLifePoints()+(h.getAttribut(Attributs.maxLifepoints)-h.getLifePoints())/2, plateau);
			StatsHandler.pushDamage(plateau, launcher, (h.getAttribut(Attributs.maxLifepoints)-h.getLifePoints())/2);
			return true;
			//TODO add a sound
		}
		return false;
	}
	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean ok, Plateau plateau) {
		g.setLineWidth(2f*Main.ratioSpace);
		g.setAntiAlias(true);
		// Draw on target
		if(Player.mouseOver==-1){			
			Objet t = Spell.realTarget(new Checkpoint(x,y), launcher, launcher.getAttribut(Attributs.range),true, plateau);
			if(! (t instanceof Character)){
				g.setColor(Color.red);
			} 
			g.drawOval(t.getX()-this.getAttribut(Attributs.size)/2,t.getY()-this.getAttribut(Attributs.size)/2, this.getAttribut(Attributs.size), this.getAttribut(Attributs.size));
		}
		g.setAntiAlias(false);
	}

}


