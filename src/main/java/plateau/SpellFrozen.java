package plateau;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import control.Player;
import data.Attributs;

import main.Main;
import plateau.Character;
import plateau.Checkpoint;
import plateau.Objet;
import plateau.Plateau;
import stats.StatsHandler;

import utils.ObjetsList;
import utils.Utils;

public strictfp class SpellFrozen extends Spell{


	public SpellFrozen(){
		this.name = ObjetsList.Frozen;
	}

	public boolean launch(Objet target, Character launcher, Plateau plateau){
		Objet h = target;
		if(h instanceof Character && h.getTeam()!=team && Utils.distance(target, launcher) <  this.getAttribut(Attributs.range)){
			((Character)h).frozen = this.getAttribut(Attributs.totalTime);
			return true;
		}
		return false;
	}


	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean ok, Plateau plateau) {
		g.setLineWidth(2f*Main.ratioSpace);
		g.setAntiAlias(true);
		// Draw on target
		if(Player.mouseOver==-1 
				|| plateau.getById(Player.mouseOver)==null 
				|| plateau.getById(Player.mouseOver).team.id==launcher.team.id
				|| Utils.distance(plateau.getById(Player.mouseOver), launcher) >  this.getAttribut(Attributs.range)){			
			Objet t = Spell.realTarget(new Checkpoint(x,y), launcher, this.getAttribut(Attributs.range),true, plateau);
			if(! (t instanceof Character)){
				g.setColor(Color.red);
			} 
			g.drawOval(t.getX()-this.getAttribut(Attributs.size)/2,t.getY()-this.getAttribut(Attributs.size)/2, this.getAttribut(Attributs.size), this.getAttribut(Attributs.size));
		}
		g.setColor(new Color(0.5f,0f,0f,0.5f));
		g.setLineWidth(1f);
		g.drawOval(launcher.getX()-this.getAttribut(Attributs.range), launcher.getY()-this.getAttribut(Attributs.range),
				2*this.getAttribut(Attributs.range),2*this.getAttribut(Attributs.range));
		g.setAntiAlias(false);
	}

}
